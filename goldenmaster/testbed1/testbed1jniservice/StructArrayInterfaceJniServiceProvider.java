// Copyright Epic Games, Inc. All Rights Reserved.

package testbed1.testbed1jniservice;

import testbed1.testbed1_android_service.IStructArrayInterfaceServiceProvider;
import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_api.AbstractStructArrayInterface;
import testbed1.testbed1jniservice.StructArrayInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton StructArrayInterfaceJniServiceProvider thread for the system.  This is a thread for
 * StructArrayInterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class StructArrayInterfaceJniServiceProvider extends HandlerThread implements IStructArrayInterfaceServiceProvider
{
	private StructArrayInterfaceJniService jniService;
	private static final String TAG = "StructArrayInterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static StructArrayInterfaceJniServiceProvider get()
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

	public synchronized  AbstractStructArrayInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new StructArrayInterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new StructArrayInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final StructArrayInterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private StructArrayInterfaceJniServiceProvider()
	{
		super("StructArrayInterfaceJniServiceProvider");
	}

	@NonNull
	private static StructArrayInterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		StructArrayInterfaceJniServiceProvider t = new StructArrayInterfaceJniServiceProvider();
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
