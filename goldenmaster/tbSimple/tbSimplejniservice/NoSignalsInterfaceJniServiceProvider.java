// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimplejniservice;

import tbSimple.tbSimple_android_service.INoSignalsInterfaceServiceProvider;
import tbSimple.tbSimple_api.INoSignalsInterface;
import tbSimple.tbSimple_api.AbstractNoSignalsInterface;
import tbSimple.tbSimplejniservice.NoSignalsInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NoSignalsInterfaceJniServiceProvider thread for the system.  This is a thread for
 * NoSignalsInterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NoSignalsInterfaceJniServiceProvider extends HandlerThread implements INoSignalsInterfaceServiceProvider
{
	private NoSignalsInterfaceJniService jniService;
	private static final String TAG = "NoSignalsInterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NoSignalsInterfaceJniServiceProvider get()
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

	public synchronized  AbstractNoSignalsInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new NoSignalsInterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new NoSignalsInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final NoSignalsInterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private NoSignalsInterfaceJniServiceProvider()
	{
		super("NoSignalsInterfaceJniServiceProvider");
	}

	@NonNull
	private static NoSignalsInterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		NoSignalsInterfaceJniServiceProvider t = new NoSignalsInterfaceJniServiceProvider();
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
