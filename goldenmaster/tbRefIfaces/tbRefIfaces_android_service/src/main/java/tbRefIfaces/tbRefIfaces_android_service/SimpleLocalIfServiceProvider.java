// Copyright Epic Games, Inc. All Rights Reserved.

package tbRefIfaces.tbRefIfaces_android_service;

import tbRefIfaces.tbRefIfaces_android_service.ISimpleLocalIfServiceProvider;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_api.AbstractSimpleLocalIf;
import tbRefIfaces.tbRefIfaces_impl.SimpleLocalIfService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SimpleLocalIfServiceProvider thread for the system.  This is a thread for
 * SimpleLocalIfServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SimpleLocalIfServiceProvider extends HandlerThread implements ISimpleLocalIfServiceProvider
{
	private SimpleLocalIfService m_Service;
	private static final String TAG = "SimpleLocalIfServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SimpleLocalIfServiceProvider get()
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

	public synchronized  AbstractSimpleLocalIf getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new SimpleLocalIfService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new SimpleLocalIfService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final SimpleLocalIfServiceProvider INSTANCE = createInstance();
	}

	private SimpleLocalIfServiceProvider()
	{
		super("SimpleLocalIfServiceProvider");
	}

	@NonNull
	private static SimpleLocalIfServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		SimpleLocalIfServiceProvider t = new SimpleLocalIfServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
