// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2_android_service;

import testbed2.testbed2_android_service.INestedStruct3InterfaceServiceProvider;
import testbed2.testbed2_api.INestedStruct3Interface;
import testbed2.testbed2_api.AbstractNestedStruct3Interface;
import testbed2.testbed2_impl.NestedStruct3InterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NestedStruct3InterfaceServiceProvider thread for the system.  This is a thread for
 * NestedStruct3InterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NestedStruct3InterfaceServiceProvider extends HandlerThread implements INestedStruct3InterfaceServiceProvider
{
	private NestedStruct3InterfaceService m_Service;
	private static final String TAG = "NestedStruct3InterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NestedStruct3InterfaceServiceProvider get()
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

	public synchronized  AbstractNestedStruct3Interface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new NestedStruct3InterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new NestedStruct3InterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final NestedStruct3InterfaceServiceProvider INSTANCE = createInstance();
	}

	private NestedStruct3InterfaceServiceProvider()
	{
		super("NestedStruct3InterfaceServiceProvider");
	}

	@NonNull
	private static NestedStruct3InterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		NestedStruct3InterfaceServiceProvider t = new NestedStruct3InterfaceServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
