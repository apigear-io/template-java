// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimplejniservice;

import tbSimple.tbSimple_android_service.ISimpleInterfaceServiceProvider;
import tbSimple.tbSimple_api.ISimpleInterface;
import tbSimple.tbSimple_api.AbstractSimpleInterface;
import tbSimple.tbSimplejniservice.SimpleInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SimpleInterfaceJniServiceProvider thread for the system.  This is a thread for
 * SimpleInterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SimpleInterfaceJniServiceProvider extends HandlerThread implements ISimpleInterfaceServiceProvider
{
	private SimpleInterfaceJniService jniService;
	private static final String TAG = "SimpleInterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SimpleInterfaceJniServiceProvider get()
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

	public synchronized  AbstractSimpleInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new SimpleInterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new SimpleInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final SimpleInterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private SimpleInterfaceJniServiceProvider()
	{
		super("SimpleInterfaceJniServiceProvider");
	}

	@NonNull
	private static SimpleInterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		SimpleInterfaceJniServiceProvider t = new SimpleInterfaceJniServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
