// Copyright Epic Games, Inc. All Rights Reserved.

package testbed1.testbed1_android_service;

import testbed1.testbed1_android_service.IStructInterfaceServiceProvider;
import testbed1.testbed1_api.IStructInterface;
import testbed1.testbed1_api.AbstractStructInterface;
import testbed1.testbed1_impl.StructInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton StructInterfaceServiceProvider thread for the system.  This is a thread for
 * StructInterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class StructInterfaceServiceProvider extends HandlerThread implements IStructInterfaceServiceProvider
{
	private StructInterfaceService m_Service;
	private static final String TAG = "StructInterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static StructInterfaceServiceProvider get()
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

	public synchronized  AbstractStructInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new StructInterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new StructInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final StructInterfaceServiceProvider INSTANCE = createInstance();
	}

	private StructInterfaceServiceProvider()
	{
		super("StructInterfaceServiceProvider");
	}

	@NonNull
	private static StructInterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		StructInterfaceServiceProvider t = new StructInterfaceServiceProvider();
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
