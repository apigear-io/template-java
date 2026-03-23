// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimplejniservice;

import tbSimple.tbSimple_android_service.INoPropertiesInterfaceServiceProvider;
import tbSimple.tbSimple_api.INoPropertiesInterface;
import tbSimple.tbSimple_api.AbstractNoPropertiesInterface;
import tbSimple.tbSimplejniservice.NoPropertiesInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NoPropertiesInterfaceJniServiceProvider thread for the system.  This is a thread for
 * NoPropertiesInterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NoPropertiesInterfaceJniServiceProvider extends HandlerThread implements INoPropertiesInterfaceServiceProvider
{
	private NoPropertiesInterfaceJniService jniService;
	private static final String TAG = "NoPropertiesInterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NoPropertiesInterfaceJniServiceProvider get()
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

	public synchronized  AbstractNoPropertiesInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new NoPropertiesInterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new NoPropertiesInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final NoPropertiesInterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private NoPropertiesInterfaceJniServiceProvider()
	{
		super("NoPropertiesInterfaceJniServiceProvider");
	}

	@NonNull
	private static NoPropertiesInterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		NoPropertiesInterfaceJniServiceProvider t = new NoPropertiesInterfaceJniServiceProvider();
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
