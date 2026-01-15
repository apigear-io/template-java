// Copyright Epic Games, Inc. All Rights Reserved.

package tbRefIfaces.tbRefIfaces_android_service;

import tbRefIfaces.tbRefIfaces_android_service.IParentIfServiceProvider;
import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_api.AbstractParentIf;
import tbRefIfaces.tbRefIfaces_impl.ParentIfService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton ParentIfServiceProvider thread for the system.  This is a thread for
 * ParentIfServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class ParentIfServiceProvider extends HandlerThread implements IParentIfServiceProvider
{
	private ParentIfService m_Service;
	private static final String TAG = "ParentIfServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static ParentIfServiceProvider get()
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
			Log.i(TAG, "LIFECYCLE: onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractParentIf getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new ParentIfService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new ParentIfService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final ParentIfServiceProvider INSTANCE = createInstance();
	}

	private ParentIfServiceProvider()
	{
		super("ParentIfServiceProvider");
	}

	@NonNull
	private static ParentIfServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		ParentIfServiceProvider t = new ParentIfServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
