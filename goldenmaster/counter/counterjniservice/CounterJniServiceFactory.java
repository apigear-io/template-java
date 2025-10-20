// Copyright Epic Games, Inc. All Rights Reserved.

package counter.counterjniservice;

import counter.counter_android_service.ICounterServiceFactory;
import counter.counter_api.ICounter;
import counter.counter_api.AbstractCounter;
import counter.counterjniservice.CounterJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton CounterJniServiceFactory thread for the system.  This is a thread for
 * CounterJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class CounterJniServiceFactory extends HandlerThread implements ICounterServiceFactory
{
	private CounterJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static CounterJniServiceFactory get()
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
			Log.w("UE", "LIFECYCLE: CounterJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractCounter getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new CounterJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new CounterJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final CounterJniServiceFactory INSTANCE = createInstance();
	}

	private CounterJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static CounterJniServiceFactory createInstance()
	{
		Log.w("UE", "LIFECYCLE: EngineFactory::createInstance()");

		CounterJniServiceFactory t = new CounterJniServiceFactory();
		t.start();
		return t;
	}
}
