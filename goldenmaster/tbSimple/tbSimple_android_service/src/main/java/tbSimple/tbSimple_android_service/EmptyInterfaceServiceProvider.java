// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimple_android_service;

import tbSimple.tbSimple_android_service.IEmptyInterfaceServiceProvider;
import tbSimple.tbSimple_api.IEmptyInterface;
import tbSimple.tbSimple_api.AbstractEmptyInterface;
import tbSimple.tbSimple_impl.EmptyInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton EmptyInterfaceServiceProvider thread for the system.  This is a thread for
 * EmptyInterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class EmptyInterfaceServiceProvider extends HandlerThread implements IEmptyInterfaceServiceProvider
{
	private EmptyInterfaceService m_Service;
	private static final String TAG = "EmptyInterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static EmptyInterfaceServiceProvider get()
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

	public synchronized  AbstractEmptyInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new EmptyInterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new EmptyInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final EmptyInterfaceServiceProvider INSTANCE = createInstance();
	}

	private EmptyInterfaceServiceProvider()
	{
		super("EmptyInterfaceServiceProvider");
	}

	@NonNull
	private static EmptyInterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		EmptyInterfaceServiceProvider t = new EmptyInterfaceServiceProvider();
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
