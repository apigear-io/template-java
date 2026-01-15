// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimple_android_service;

import tbSimple.tbSimple_android_service.ISimpleInterfaceServiceProvider;
import tbSimple.tbSimple_api.ISimpleInterface;
import tbSimple.tbSimple_api.AbstractSimpleInterface;
import tbSimple.tbSimple_impl.SimpleInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SimpleInterfaceServiceProvider thread for the system.  This is a thread for
 * SimpleInterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SimpleInterfaceServiceProvider extends HandlerThread implements ISimpleInterfaceServiceProvider
{
	private SimpleInterfaceService m_Service;
	private static final String TAG = "SimpleInterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SimpleInterfaceServiceProvider get()
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

	public synchronized  AbstractSimpleInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new SimpleInterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new SimpleInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final SimpleInterfaceServiceProvider INSTANCE = createInstance();
	}

	private SimpleInterfaceServiceProvider()
	{
		super("SimpleInterfaceServiceProvider");
	}

	@NonNull
	private static SimpleInterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		SimpleInterfaceServiceProvider t = new SimpleInterfaceServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
