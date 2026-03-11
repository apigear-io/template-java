// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2jniservice;

import testbed2.testbed2_android_service.INestedStruct2InterfaceServiceProvider;
import testbed2.testbed2_api.INestedStruct2Interface;
import testbed2.testbed2_api.AbstractNestedStruct2Interface;
import testbed2.testbed2jniservice.NestedStruct2InterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NestedStruct2InterfaceJniServiceProvider thread for the system.  This is a thread for
 * NestedStruct2InterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NestedStruct2InterfaceJniServiceProvider extends HandlerThread implements INestedStruct2InterfaceServiceProvider
{
	private NestedStruct2InterfaceJniService jniService;
	private static final String TAG = "NestedStruct2InterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NestedStruct2InterfaceJniServiceProvider get()
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

	public synchronized  AbstractNestedStruct2Interface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new NestedStruct2InterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new NestedStruct2InterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final NestedStruct2InterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private NestedStruct2InterfaceJniServiceProvider()
	{
		super("NestedStruct2InterfaceJniServiceProvider");
	}

	@NonNull
	private static NestedStruct2InterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		NestedStruct2InterfaceJniServiceProvider t = new NestedStruct2InterfaceJniServiceProvider();
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
