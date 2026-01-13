// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimplejniservice;

import tbSimple.tbSimple_android_service.INoOperationsInterfaceServiceFactory;
import tbSimple.tbSimple_api.INoOperationsInterface;
import tbSimple.tbSimple_api.AbstractNoOperationsInterface;
import tbSimple.tbSimplejniservice.NoOperationsInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NoOperationsInterfaceJniServiceFactory thread for the system.  This is a thread for
 * NoOperationsInterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NoOperationsInterfaceJniServiceFactory extends HandlerThread implements INoOperationsInterfaceServiceFactory
{
	private NoOperationsInterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NoOperationsInterfaceJniServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: NoOperationsInterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractNoOperationsInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new NoOperationsInterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new NoOperationsInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final NoOperationsInterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private NoOperationsInterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static NoOperationsInterfaceJniServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		NoOperationsInterfaceJniServiceFactory t = new NoOperationsInterfaceJniServiceFactory();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
