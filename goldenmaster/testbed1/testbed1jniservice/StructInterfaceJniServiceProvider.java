// Copyright Epic Games, Inc. All Rights Reserved.

package testbed1.testbed1jniservice;

import testbed1.testbed1_android_service.IStructInterfaceServiceProvider;
import testbed1.testbed1_api.IStructInterface;
import testbed1.testbed1_api.AbstractStructInterface;
import testbed1.testbed1jniservice.StructInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton StructInterfaceJniServiceProvider thread for the system.  This is a thread for
 * StructInterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class StructInterfaceJniServiceProvider extends HandlerThread implements IStructInterfaceServiceProvider
{
	private StructInterfaceJniService jniService;
	private static final String TAG = "StructInterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static StructInterfaceJniServiceProvider get()
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

	public synchronized  AbstractStructInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new StructInterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new StructInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final StructInterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private StructInterfaceJniServiceProvider()
	{
		super("StructInterfaceJniServiceProvider");
	}

	@NonNull
	private static StructInterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		StructInterfaceJniServiceProvider t = new StructInterfaceJniServiceProvider();
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
