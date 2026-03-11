// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame1.tbSame1jniservice;

import tbSame1.tbSame1_android_service.ISameEnum1InterfaceServiceProvider;
import tbSame1.tbSame1_api.ISameEnum1Interface;
import tbSame1.tbSame1_api.AbstractSameEnum1Interface;
import tbSame1.tbSame1jniservice.SameEnum1InterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SameEnum1InterfaceJniServiceProvider thread for the system.  This is a thread for
 * SameEnum1InterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SameEnum1InterfaceJniServiceProvider extends HandlerThread implements ISameEnum1InterfaceServiceProvider
{
	private SameEnum1InterfaceJniService jniService;
	private static final String TAG = "SameEnum1InterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SameEnum1InterfaceJniServiceProvider get()
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
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractSameEnum1Interface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new SameEnum1InterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new SameEnum1InterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final SameEnum1InterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private SameEnum1InterfaceJniServiceProvider()
	{
		super("SameEnum1InterfaceJniServiceProvider");
	}

	@NonNull
	private static SameEnum1InterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		SameEnum1InterfaceJniServiceProvider t = new SameEnum1InterfaceJniServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		if (jniService != null)
		{
			jniService.cancelAllPending();
		}
		jniService = null;
	}
}
