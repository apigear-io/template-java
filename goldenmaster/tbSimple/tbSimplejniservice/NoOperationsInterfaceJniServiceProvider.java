// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimplejniservice;

import tbSimple.tbSimple_android_service.INoOperationsInterfaceServiceProvider;
import tbSimple.tbSimple_api.INoOperationsInterface;
import tbSimple.tbSimple_api.AbstractNoOperationsInterface;
import tbSimple.tbSimplejniservice.NoOperationsInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NoOperationsInterfaceJniServiceProvider thread for the system.  This is a thread for
 * NoOperationsInterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NoOperationsInterfaceJniServiceProvider extends HandlerThread implements INoOperationsInterfaceServiceProvider
{
	private NoOperationsInterfaceJniService jniService;
	private static final String TAG = "NoOperationsInterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NoOperationsInterfaceJniServiceProvider get()
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

	public synchronized  AbstractNoOperationsInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new NoOperationsInterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new NoOperationsInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final NoOperationsInterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private NoOperationsInterfaceJniServiceProvider()
	{
		super("NoOperationsInterfaceJniServiceProvider");
	}

	@NonNull
	private static NoOperationsInterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		NoOperationsInterfaceJniServiceProvider t = new NoOperationsInterfaceJniServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
