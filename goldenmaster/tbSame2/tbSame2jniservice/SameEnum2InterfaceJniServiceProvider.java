// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame2.tbSame2jniservice;

import tbSame2.tbSame2_android_service.ISameEnum2InterfaceServiceProvider;
import tbSame2.tbSame2_api.ISameEnum2Interface;
import tbSame2.tbSame2_api.AbstractSameEnum2Interface;
import tbSame2.tbSame2jniservice.SameEnum2InterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SameEnum2InterfaceJniServiceProvider thread for the system.  This is a thread for
 * SameEnum2InterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SameEnum2InterfaceJniServiceProvider extends HandlerThread implements ISameEnum2InterfaceServiceProvider
{
	private SameEnum2InterfaceJniService jniService;
	private static final String TAG = "SameEnum2InterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SameEnum2InterfaceJniServiceProvider get()
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

	public synchronized  AbstractSameEnum2Interface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new SameEnum2InterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new SameEnum2InterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final SameEnum2InterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private SameEnum2InterfaceJniServiceProvider()
	{
		super("SameEnum2InterfaceJniServiceProvider");
	}

	@NonNull
	private static SameEnum2InterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		SameEnum2InterfaceJniServiceProvider t = new SameEnum2InterfaceJniServiceProvider();
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
