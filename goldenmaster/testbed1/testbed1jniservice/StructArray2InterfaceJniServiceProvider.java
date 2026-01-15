// Copyright Epic Games, Inc. All Rights Reserved.

package testbed1.testbed1jniservice;

import testbed1.testbed1_android_service.IStructArray2InterfaceServiceProvider;
import testbed1.testbed1_api.IStructArray2Interface;
import testbed1.testbed1_api.AbstractStructArray2Interface;
import testbed1.testbed1jniservice.StructArray2InterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton StructArray2InterfaceJniServiceProvider thread for the system.  This is a thread for
 * StructArray2InterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class StructArray2InterfaceJniServiceProvider extends HandlerThread implements IStructArray2InterfaceServiceProvider
{
	private StructArray2InterfaceJniService jniService;
	private static final String TAG = "StructArray2InterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static StructArray2InterfaceJniServiceProvider get()
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

	public synchronized  AbstractStructArray2Interface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new StructArray2InterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new StructArray2InterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final StructArray2InterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private StructArray2InterfaceJniServiceProvider()
	{
		super("StructArray2InterfaceJniServiceProvider");
	}

	@NonNull
	private static StructArray2InterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		StructArray2InterfaceJniServiceProvider t = new StructArray2InterfaceJniServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
