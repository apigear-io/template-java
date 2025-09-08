// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimplejniservice;

import tbSimple.tbSimple_android_service.IEmptyInterfaceServiceFactory;
import tbSimple.tbSimple_api.IEmptyInterface;
import tbSimple.tbSimple_api.AbstractEmptyInterface;
import tbSimple.tbSimplejniservice.EmptyInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton EmptyInterfaceJniServiceFactory thread for the system.  This is a thread for
 * EmptyInterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class EmptyInterfaceJniServiceFactory extends HandlerThread implements IEmptyInterfaceServiceFactory
{
	private EmptyInterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static EmptyInterfaceJniServiceFactory get()
	{
		return Singleton.INSTANCE;
	}

	public static Looper getInstanceLooper()
	{
		return Singleton.INSTANCE.getLooper();
	}

	public void onDestroy()
	{
		synchronized (this)
		{
			Log.w("UE", "LIFECYCLE: EmptyInterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractEmptyInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new EmptyInterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new EmptyInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final EmptyInterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private EmptyInterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static EmptyInterfaceJniServiceFactory createInstance()
	{
		Log.w("UE", "LIFECYCLE: EngineFactory::createInstance()");

		EmptyInterfaceJniServiceFactory t = new EmptyInterfaceJniServiceFactory();
		t.start();
		return t;
	}
}
