// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame2.tbSame2_android_service;

import tbSame2.tbSame2_android_service.ISameEnum1InterfaceServiceProvider;
import tbSame2.tbSame2_api.ISameEnum1Interface;
import tbSame2.tbSame2_api.AbstractSameEnum1Interface;
import tbSame2.tbSame2_impl.SameEnum1InterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SameEnum1InterfaceServiceProvider thread for the system.  This is a thread for
 * SameEnum1InterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SameEnum1InterfaceServiceProvider extends HandlerThread implements ISameEnum1InterfaceServiceProvider
{
	private SameEnum1InterfaceService m_Service;
	private static final String TAG = "SameEnum1InterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SameEnum1InterfaceServiceProvider get()
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

	public synchronized  AbstractSameEnum1Interface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new SameEnum1InterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new SameEnum1InterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final SameEnum1InterfaceServiceProvider INSTANCE = createInstance();
	}

	private SameEnum1InterfaceServiceProvider()
	{
		super("SameEnum1InterfaceServiceProvider");
	}

	@NonNull
	private static SameEnum1InterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		SameEnum1InterfaceServiceProvider t = new SameEnum1InterfaceServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
