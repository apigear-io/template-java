// Copyright Epic Games, Inc. All Rights Reserved.

package testbed1.testbed1_android_service;

import testbed1.testbed1_android_service.IStructArrayInterfaceServiceProvider;
import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_api.AbstractStructArrayInterface;
import testbed1.testbed1_impl.StructArrayInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton StructArrayInterfaceServiceProvider thread for the system.  This is a thread for
 * StructArrayInterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class StructArrayInterfaceServiceProvider extends HandlerThread implements IStructArrayInterfaceServiceProvider
{
	private StructArrayInterfaceService m_Service;
	private static final String TAG = "StructArrayInterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static StructArrayInterfaceServiceProvider get()
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

	public synchronized  AbstractStructArrayInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new StructArrayInterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new StructArrayInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final StructArrayInterfaceServiceProvider INSTANCE = createInstance();
	}

	private StructArrayInterfaceServiceProvider()
	{
		super("StructArrayInterfaceServiceProvider");
	}

	@NonNull
	private static StructArrayInterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		StructArrayInterfaceServiceProvider t = new StructArrayInterfaceServiceProvider();
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
