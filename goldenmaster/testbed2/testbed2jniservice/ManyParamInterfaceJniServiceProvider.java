// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2jniservice;

import testbed2.testbed2_android_service.IManyParamInterfaceServiceProvider;
import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_api.AbstractManyParamInterface;
import testbed2.testbed2jniservice.ManyParamInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton ManyParamInterfaceJniServiceProvider thread for the system.  This is a thread for
 * ManyParamInterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class ManyParamInterfaceJniServiceProvider extends HandlerThread implements IManyParamInterfaceServiceProvider
{
	private ManyParamInterfaceJniService jniService;
	private static final String TAG = "ManyParamInterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static ManyParamInterfaceJniServiceProvider get()
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

	public synchronized  AbstractManyParamInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new ManyParamInterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new ManyParamInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final ManyParamInterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private ManyParamInterfaceJniServiceProvider()
	{
		super("ManyParamInterfaceJniServiceProvider");
	}

	@NonNull
	private static ManyParamInterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		ManyParamInterfaceJniServiceProvider t = new ManyParamInterfaceJniServiceProvider();
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
