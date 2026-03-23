// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame2.tbSame2jniservice;

import tbSame2.tbSame2_android_service.ISameStruct2InterfaceServiceProvider;
import tbSame2.tbSame2_api.ISameStruct2Interface;
import tbSame2.tbSame2_api.AbstractSameStruct2Interface;
import tbSame2.tbSame2jniservice.SameStruct2InterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SameStruct2InterfaceJniServiceProvider thread for the system.  This is a thread for
 * SameStruct2InterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SameStruct2InterfaceJniServiceProvider extends HandlerThread implements ISameStruct2InterfaceServiceProvider
{
	private SameStruct2InterfaceJniService jniService;
	private static final String TAG = "SameStruct2InterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SameStruct2InterfaceJniServiceProvider get()
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

	public synchronized  AbstractSameStruct2Interface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new SameStruct2InterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new SameStruct2InterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final SameStruct2InterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private SameStruct2InterfaceJniServiceProvider()
	{
		super("SameStruct2InterfaceJniServiceProvider");
	}

	@NonNull
	private static SameStruct2InterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		SameStruct2InterfaceJniServiceProvider t = new SameStruct2InterfaceJniServiceProvider();
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
