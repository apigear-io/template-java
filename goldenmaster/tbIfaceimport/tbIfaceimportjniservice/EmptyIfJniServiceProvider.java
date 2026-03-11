// Copyright Epic Games, Inc. All Rights Reserved.

package tbIfaceimport.tbIfaceimportjniservice;

import tbIfaceimport.tbIfaceimport_android_service.IEmptyIfServiceProvider;
import tbIfaceimport.tbIfaceimport_api.IEmptyIf;
import tbIfaceimport.tbIfaceimport_api.AbstractEmptyIf;
import tbIfaceimport.tbIfaceimportjniservice.EmptyIfJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton EmptyIfJniServiceProvider thread for the system.  This is a thread for
 * EmptyIfJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class EmptyIfJniServiceProvider extends HandlerThread implements IEmptyIfServiceProvider
{
	private EmptyIfJniService jniService;
	private static final String TAG = "EmptyIfJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static EmptyIfJniServiceProvider get()
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

	public synchronized  AbstractEmptyIf getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new EmptyIfJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new EmptyIfJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final EmptyIfJniServiceProvider INSTANCE = createInstance();
	}

	private EmptyIfJniServiceProvider()
	{
		super("EmptyIfJniServiceProvider");
	}

	@NonNull
	private static EmptyIfJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		EmptyIfJniServiceProvider t = new EmptyIfJniServiceProvider();
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
