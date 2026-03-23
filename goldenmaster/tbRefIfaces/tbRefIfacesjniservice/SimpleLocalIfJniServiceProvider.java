// Copyright Epic Games, Inc. All Rights Reserved.

package tbRefIfaces.tbRefIfacesjniservice;

import tbRefIfaces.tbRefIfaces_android_service.ISimpleLocalIfServiceProvider;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_api.AbstractSimpleLocalIf;
import tbRefIfaces.tbRefIfacesjniservice.SimpleLocalIfJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SimpleLocalIfJniServiceProvider thread for the system.  This is a thread for
 * SimpleLocalIfJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SimpleLocalIfJniServiceProvider extends HandlerThread implements ISimpleLocalIfServiceProvider
{
	private SimpleLocalIfJniService jniService;
	private static final String TAG = "SimpleLocalIfJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SimpleLocalIfJniServiceProvider get()
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

	public synchronized  AbstractSimpleLocalIf getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new SimpleLocalIfJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new SimpleLocalIfJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final SimpleLocalIfJniServiceProvider INSTANCE = createInstance();
	}

	private SimpleLocalIfJniServiceProvider()
	{
		super("SimpleLocalIfJniServiceProvider");
	}

	@NonNull
	private static SimpleLocalIfJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		SimpleLocalIfJniServiceProvider t = new SimpleLocalIfJniServiceProvider();
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
