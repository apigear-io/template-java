// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2jniservice;

import testbed2.testbed2_android_service.INestedStruct1InterfaceServiceProvider;
import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_api.AbstractNestedStruct1Interface;
import testbed2.testbed2jniservice.NestedStruct1InterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NestedStruct1InterfaceJniServiceProvider thread for the system.  This is a thread for
 * NestedStruct1InterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NestedStruct1InterfaceJniServiceProvider extends HandlerThread implements INestedStruct1InterfaceServiceProvider
{
	private NestedStruct1InterfaceJniService jniService;
	private static final String TAG = "NestedStruct1InterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NestedStruct1InterfaceJniServiceProvider get()
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

	public synchronized  AbstractNestedStruct1Interface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new NestedStruct1InterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new NestedStruct1InterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final NestedStruct1InterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private NestedStruct1InterfaceJniServiceProvider()
	{
		super("NestedStruct1InterfaceJniServiceProvider");
	}

	@NonNull
	private static NestedStruct1InterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		NestedStruct1InterfaceJniServiceProvider t = new NestedStruct1InterfaceJniServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
