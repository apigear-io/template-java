// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimplejniservice;

import tbSimple.tbSimple_android_service.ISimpleArrayInterfaceServiceProvider;
import tbSimple.tbSimple_api.ISimpleArrayInterface;
import tbSimple.tbSimple_api.AbstractSimpleArrayInterface;
import tbSimple.tbSimplejniservice.SimpleArrayInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SimpleArrayInterfaceJniServiceProvider thread for the system.  This is a thread for
 * SimpleArrayInterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SimpleArrayInterfaceJniServiceProvider extends HandlerThread implements ISimpleArrayInterfaceServiceProvider
{
	private SimpleArrayInterfaceJniService jniService;
	private static final String TAG = "SimpleArrayInterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SimpleArrayInterfaceJniServiceProvider get()
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

	public synchronized  AbstractSimpleArrayInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new SimpleArrayInterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new SimpleArrayInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final SimpleArrayInterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private SimpleArrayInterfaceJniServiceProvider()
	{
		super("SimpleArrayInterfaceJniServiceProvider");
	}

	@NonNull
	private static SimpleArrayInterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		SimpleArrayInterfaceJniServiceProvider t = new SimpleArrayInterfaceJniServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
