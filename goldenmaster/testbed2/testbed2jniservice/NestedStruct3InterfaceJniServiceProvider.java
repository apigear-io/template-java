// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2jniservice;

import testbed2.testbed2_android_service.INestedStruct3InterfaceServiceProvider;
import testbed2.testbed2_api.INestedStruct3Interface;
import testbed2.testbed2_api.AbstractNestedStruct3Interface;
import testbed2.testbed2jniservice.NestedStruct3InterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NestedStruct3InterfaceJniServiceProvider thread for the system.  This is a thread for
 * NestedStruct3InterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NestedStruct3InterfaceJniServiceProvider extends HandlerThread implements INestedStruct3InterfaceServiceProvider
{
	private NestedStruct3InterfaceJniService jniService;
	private static final String TAG = "NestedStruct3InterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NestedStruct3InterfaceJniServiceProvider get()
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

	public synchronized  AbstractNestedStruct3Interface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new NestedStruct3InterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new NestedStruct3InterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final NestedStruct3InterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private NestedStruct3InterfaceJniServiceProvider()
	{
		super("NestedStruct3InterfaceJniServiceProvider");
	}

	@NonNull
	private static NestedStruct3InterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		NestedStruct3InterfaceJniServiceProvider t = new NestedStruct3InterfaceJniServiceProvider();
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
