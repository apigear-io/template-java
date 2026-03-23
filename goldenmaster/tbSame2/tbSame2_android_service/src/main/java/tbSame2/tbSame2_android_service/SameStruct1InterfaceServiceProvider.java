// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame2.tbSame2_android_service;

import tbSame2.tbSame2_android_service.ISameStruct1InterfaceServiceProvider;
import tbSame2.tbSame2_api.ISameStruct1Interface;
import tbSame2.tbSame2_api.AbstractSameStruct1Interface;
import tbSame2.tbSame2_impl.SameStruct1InterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SameStruct1InterfaceServiceProvider thread for the system.  This is a thread for
 * SameStruct1InterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SameStruct1InterfaceServiceProvider extends HandlerThread implements ISameStruct1InterfaceServiceProvider
{
	private SameStruct1InterfaceService m_Service;
	private static final String TAG = "SameStruct1InterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SameStruct1InterfaceServiceProvider get()
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

	public synchronized  AbstractSameStruct1Interface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new SameStruct1InterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new SameStruct1InterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final SameStruct1InterfaceServiceProvider INSTANCE = createInstance();
	}

	private SameStruct1InterfaceServiceProvider()
	{
		super("SameStruct1InterfaceServiceProvider");
	}

	@NonNull
	private static SameStruct1InterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		SameStruct1InterfaceServiceProvider t = new SameStruct1InterfaceServiceProvider();
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
