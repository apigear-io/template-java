// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame1.tbSame1_android_service;

import tbSame1.tbSame1_android_service.ISameEnum2InterfaceServiceProvider;
import tbSame1.tbSame1_api.ISameEnum2Interface;
import tbSame1.tbSame1_api.AbstractSameEnum2Interface;
import tbSame1.tbSame1_impl.SameEnum2InterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SameEnum2InterfaceServiceProvider thread for the system.  This is a thread for
 * SameEnum2InterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SameEnum2InterfaceServiceProvider extends HandlerThread implements ISameEnum2InterfaceServiceProvider
{
	private SameEnum2InterfaceService m_Service;
	private static final String TAG = "SameEnum2InterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SameEnum2InterfaceServiceProvider get()
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

	public synchronized  AbstractSameEnum2Interface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new SameEnum2InterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new SameEnum2InterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final SameEnum2InterfaceServiceProvider INSTANCE = createInstance();
	}

	private SameEnum2InterfaceServiceProvider()
	{
		super("SameEnum2InterfaceServiceProvider");
	}

	@NonNull
	private static SameEnum2InterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		SameEnum2InterfaceServiceProvider t = new SameEnum2InterfaceServiceProvider();
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
