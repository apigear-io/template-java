// Copyright Epic Games, Inc. All Rights Reserved.

package counter.counter_android_service;

import counter.counter_android_service.ICounterServiceFactory;
import counter.counter_api.ICounter;
import counter.counter_api.AbstractCounter;
import counter.counter_impl.CounterService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton CounterServiceFactory thread for the system.  This is a thread for
 * CounterServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class CounterServiceFactory extends HandlerThread implements ICounterServiceFactory
{
	private CounterService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static CounterServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: CounterServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractCounter getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new CounterService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new CounterService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final CounterServiceFactory INSTANCE = createInstance();
	}

	private CounterServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static CounterServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		CounterServiceFactory t = new CounterServiceFactory();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
