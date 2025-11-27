// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimplejniservice;

import tbSimple.tbSimple_android_service.INoSignalsInterfaceServiceFactory;
import tbSimple.tbSimple_api.INoSignalsInterface;
import tbSimple.tbSimple_api.AbstractNoSignalsInterface;
import tbSimple.tbSimplejniservice.NoSignalsInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NoSignalsInterfaceJniServiceFactory thread for the system.  This is a thread for
 * NoSignalsInterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NoSignalsInterfaceJniServiceFactory extends HandlerThread implements INoSignalsInterfaceServiceFactory
{
	private NoSignalsInterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NoSignalsInterfaceJniServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: NoSignalsInterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractNoSignalsInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new NoSignalsInterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new NoSignalsInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final NoSignalsInterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private NoSignalsInterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static NoSignalsInterfaceJniServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		NoSignalsInterfaceJniServiceFactory t = new NoSignalsInterfaceJniServiceFactory();
		t.start();
		return t;
	}
}
