// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame2.tbSame2jniservice;

import tbSame2.tbSame2_android_service.ISameStruct1InterfaceServiceProvider;
import tbSame2.tbSame2_api.ISameStruct1Interface;
import tbSame2.tbSame2_api.AbstractSameStruct1Interface;
import tbSame2.tbSame2jniservice.SameStruct1InterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SameStruct1InterfaceJniServiceProvider thread for the system.  This is a thread for
 * SameStruct1InterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SameStruct1InterfaceJniServiceProvider extends HandlerThread implements ISameStruct1InterfaceServiceProvider
{
	private SameStruct1InterfaceJniService jniService;
	private static final String TAG = "SameStruct1InterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SameStruct1InterfaceJniServiceProvider get()
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

	public synchronized  AbstractSameStruct1Interface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new SameStruct1InterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new SameStruct1InterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final SameStruct1InterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private SameStruct1InterfaceJniServiceProvider()
	{
		super("SameStruct1InterfaceJniServiceProvider");
	}

	@NonNull
	private static SameStruct1InterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		SameStruct1InterfaceJniServiceProvider t = new SameStruct1InterfaceJniServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
