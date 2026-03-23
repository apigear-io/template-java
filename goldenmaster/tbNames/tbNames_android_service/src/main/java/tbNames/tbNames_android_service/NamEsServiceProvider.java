// Copyright Epic Games, Inc. All Rights Reserved.

package tbNames.tbNames_android_service;

import tbNames.tbNames_android_service.INamEsServiceProvider;
import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_api.AbstractNamEs;
import tbNames.tbNames_impl.NamEsService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NamEsServiceProvider thread for the system.  This is a thread for
 * NamEsServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NamEsServiceProvider extends HandlerThread implements INamEsServiceProvider
{
	private NamEsService m_Service;
	private static final String TAG = "NamEsServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NamEsServiceProvider get()
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

	public synchronized  AbstractNamEs getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new NamEsService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new NamEsService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final NamEsServiceProvider INSTANCE = createInstance();
	}

	private NamEsServiceProvider()
	{
		super("NamEsServiceProvider");
	}

	@NonNull
	private static NamEsServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		NamEsServiceProvider t = new NamEsServiceProvider();
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
