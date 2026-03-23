// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimple_android_service;

import tbSimple.tbSimple_android_service.INoOperationsInterfaceServiceProvider;
import tbSimple.tbSimple_api.INoOperationsInterface;
import tbSimple.tbSimple_api.AbstractNoOperationsInterface;
import tbSimple.tbSimple_impl.NoOperationsInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NoOperationsInterfaceServiceProvider thread for the system.  This is a thread for
 * NoOperationsInterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NoOperationsInterfaceServiceProvider extends HandlerThread implements INoOperationsInterfaceServiceProvider
{
	private NoOperationsInterfaceService m_Service;
	private static final String TAG = "NoOperationsInterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NoOperationsInterfaceServiceProvider get()
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
			clear();
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractNoOperationsInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new NoOperationsInterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new NoOperationsInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final NoOperationsInterfaceServiceProvider INSTANCE = createInstance();
	}

	private NoOperationsInterfaceServiceProvider()
	{
		super("NoOperationsInterfaceServiceProvider");
	}

	@NonNull
	private static NoOperationsInterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		NoOperationsInterfaceServiceProvider t = new NoOperationsInterfaceServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		if (m_Service != null)
		{
			m_Service._shutdown();
		}
		m_Service = null;
	}
}
