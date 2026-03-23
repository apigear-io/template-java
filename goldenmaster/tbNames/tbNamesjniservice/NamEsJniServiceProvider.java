// Copyright Epic Games, Inc. All Rights Reserved.

package tbNames.tbNamesjniservice;

import tbNames.tbNames_android_service.INamEsServiceProvider;
import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_api.AbstractNamEs;
import tbNames.tbNamesjniservice.NamEsJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NamEsJniServiceProvider thread for the system.  This is a thread for
 * NamEsJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NamEsJniServiceProvider extends HandlerThread implements INamEsServiceProvider
{
	private NamEsJniService jniService;
	private static final String TAG = "NamEsJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NamEsJniServiceProvider get()
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

	public synchronized  AbstractNamEs getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new NamEsJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new NamEsJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final NamEsJniServiceProvider INSTANCE = createInstance();
	}

	private NamEsJniServiceProvider()
	{
		super("NamEsJniServiceProvider");
	}

	@NonNull
	private static NamEsJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		NamEsJniServiceProvider t = new NamEsJniServiceProvider();
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
