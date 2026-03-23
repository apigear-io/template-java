// Copyright Epic Games, Inc. All Rights Reserved.

package counter.counterjniservice;

import counter.counter_android_service.ICounterServiceProvider;
import counter.counter_api.ICounter;
import counter.counter_api.AbstractCounter;
import counter.counterjniservice.CounterJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton CounterJniServiceProvider thread for the system.  This is a thread for
 * CounterJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class CounterJniServiceProvider extends HandlerThread implements ICounterServiceProvider
{
	private CounterJniService jniService;
	private static final String TAG = "CounterJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static CounterJniServiceProvider get()
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
			Log.i(TAG, "LIFECYCLE: onDestroy() - stop instance thread, service = " + jniService);
			clear();
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractCounter getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new CounterJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new CounterJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final CounterJniServiceProvider INSTANCE = createInstance();
	}

	private CounterJniServiceProvider()
	{
		super("CounterJniServiceProvider");
	}

	@NonNull
	private static CounterJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		CounterJniServiceProvider t = new CounterJniServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		if (jniService != null)
		{
			jniService._shutdown();
		}
		jniService = null;
	}
}
