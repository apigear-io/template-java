// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimplejniservice;

import tbSimple.tbSimple_android_service.ISimpleArrayInterfaceServiceFactory;
import tbSimple.tbSimple_api.ISimpleArrayInterface;
import tbSimple.tbSimple_api.AbstractSimpleArrayInterface;
import tbSimple.tbSimplejniservice.SimpleArrayInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SimpleArrayInterfaceJniServiceFactory thread for the system.  This is a thread for
 * SimpleArrayInterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SimpleArrayInterfaceJniServiceFactory extends HandlerThread implements ISimpleArrayInterfaceServiceFactory
{
	private SimpleArrayInterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SimpleArrayInterfaceJniServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: SimpleArrayInterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractSimpleArrayInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new SimpleArrayInterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new SimpleArrayInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final SimpleArrayInterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private SimpleArrayInterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static SimpleArrayInterfaceJniServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		SimpleArrayInterfaceJniServiceFactory t = new SimpleArrayInterfaceJniServiceFactory();
		t.start();
		return t;
	}
}
