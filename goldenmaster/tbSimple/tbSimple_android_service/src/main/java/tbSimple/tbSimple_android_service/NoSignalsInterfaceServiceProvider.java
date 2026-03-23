// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimple_android_service;

import tbSimple.tbSimple_android_service.INoSignalsInterfaceServiceProvider;
import tbSimple.tbSimple_api.INoSignalsInterface;
import tbSimple.tbSimple_api.AbstractNoSignalsInterface;
import tbSimple.tbSimple_impl.NoSignalsInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NoSignalsInterfaceServiceProvider thread for the system.  This is a thread for
 * NoSignalsInterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NoSignalsInterfaceServiceProvider extends HandlerThread implements INoSignalsInterfaceServiceProvider
{
	private NoSignalsInterfaceService m_Service;
	private static final String TAG = "NoSignalsInterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NoSignalsInterfaceServiceProvider get()
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

	public synchronized  AbstractNoSignalsInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new NoSignalsInterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new NoSignalsInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final NoSignalsInterfaceServiceProvider INSTANCE = createInstance();
	}

	private NoSignalsInterfaceServiceProvider()
	{
		super("NoSignalsInterfaceServiceProvider");
	}

	@NonNull
	private static NoSignalsInterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		NoSignalsInterfaceServiceProvider t = new NoSignalsInterfaceServiceProvider();
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
