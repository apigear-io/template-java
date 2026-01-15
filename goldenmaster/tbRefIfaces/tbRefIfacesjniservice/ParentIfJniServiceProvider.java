// Copyright Epic Games, Inc. All Rights Reserved.

package tbRefIfaces.tbRefIfacesjniservice;

import tbRefIfaces.tbRefIfaces_android_service.IParentIfServiceProvider;
import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_api.AbstractParentIf;
import tbRefIfaces.tbRefIfacesjniservice.ParentIfJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton ParentIfJniServiceProvider thread for the system.  This is a thread for
 * ParentIfJniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class ParentIfJniServiceProvider extends HandlerThread implements IParentIfServiceProvider
{
	private ParentIfJniService jniService;
	private static final String TAG = "ParentIfJniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static ParentIfJniServiceProvider get()
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

	public synchronized  AbstractParentIf getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new ParentIfJniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new ParentIfJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final ParentIfJniServiceProvider INSTANCE = createInstance();
	}

	private ParentIfJniServiceProvider()
	{
		super("ParentIfJniServiceProvider");
	}

	@NonNull
	private static ParentIfJniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		ParentIfJniServiceProvider t = new ParentIfJniServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
