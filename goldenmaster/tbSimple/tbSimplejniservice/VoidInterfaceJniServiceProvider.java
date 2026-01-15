// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimplejniservice;

import tbSimple.tbSimple_android_service.IVoidInterfaceServiceProvider;
import tbSimple.tbSimple_api.IVoidInterface;
import tbSimple.tbSimple_api.AbstractVoidInterface;
import tbSimple.tbSimplejniservice.VoidInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton VoidInterfaceJniServiceProvider thread for the system.  This is a thread for
 * VoidInterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class VoidInterfaceJniServiceProvider extends HandlerThread implements IVoidInterfaceServiceProvider
{
	private VoidInterfaceJniService jniService;
	private static final String TAG = "VoidInterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static VoidInterfaceJniServiceProvider get()
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

	public synchronized  AbstractVoidInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new VoidInterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new VoidInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final VoidInterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private VoidInterfaceJniServiceProvider()
	{
		super("VoidInterfaceJniServiceProvider");
	}

	@NonNull
	private static VoidInterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		VoidInterfaceJniServiceProvider t = new VoidInterfaceJniServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
