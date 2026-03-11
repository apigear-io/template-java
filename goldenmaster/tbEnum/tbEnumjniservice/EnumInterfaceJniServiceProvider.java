// Copyright Epic Games, Inc. All Rights Reserved.

package tbEnum.tbEnumjniservice;

import tbEnum.tbEnum_android_service.IEnumInterfaceServiceProvider;
import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_api.AbstractEnumInterface;
import tbEnum.tbEnumjniservice.EnumInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton EnumInterfaceJniServiceProvider thread for the system.  This is a thread for
 * EnumInterfaceJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class EnumInterfaceJniServiceProvider extends HandlerThread implements IEnumInterfaceServiceProvider
{
	private EnumInterfaceJniService jniService;
	private static final String TAG = "EnumInterfaceJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static EnumInterfaceJniServiceProvider get()
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

	public synchronized  AbstractEnumInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new EnumInterfaceJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new EnumInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final EnumInterfaceJniServiceProvider INSTANCE = createInstance();
	}

	private EnumInterfaceJniServiceProvider()
	{
		super("EnumInterfaceJniServiceProvider");
	}

	@NonNull
	private static EnumInterfaceJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		EnumInterfaceJniServiceProvider t = new EnumInterfaceJniServiceProvider();
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
