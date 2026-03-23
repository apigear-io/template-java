// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame1.tbSame1_android_service;

import tbSame1.tbSame1_android_service.ISameStruct2InterfaceServiceProvider;
import tbSame1.tbSame1_api.ISameStruct2Interface;
import tbSame1.tbSame1_api.AbstractSameStruct2Interface;
import tbSame1.tbSame1_impl.SameStruct2InterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SameStruct2InterfaceServiceProvider thread for the system.  This is a thread for
 * SameStruct2InterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SameStruct2InterfaceServiceProvider extends HandlerThread implements ISameStruct2InterfaceServiceProvider
{
	private SameStruct2InterfaceService m_Service;
	private static final String TAG = "SameStruct2InterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SameStruct2InterfaceServiceProvider get()
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

	public synchronized  AbstractSameStruct2Interface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new SameStruct2InterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new SameStruct2InterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final SameStruct2InterfaceServiceProvider INSTANCE = createInstance();
	}

	private SameStruct2InterfaceServiceProvider()
	{
		super("SameStruct2InterfaceServiceProvider");
	}

	@NonNull
	private static SameStruct2InterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		SameStruct2InterfaceServiceProvider t = new SameStruct2InterfaceServiceProvider();
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
