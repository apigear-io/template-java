// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2_android_service;

import testbed2.testbed2_android_service.INestedStruct1InterfaceServiceProvider;
import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_api.AbstractNestedStruct1Interface;
import testbed2.testbed2_impl.NestedStruct1InterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NestedStruct1InterfaceServiceProvider thread for the system.  This is a thread for
 * NestedStruct1InterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NestedStruct1InterfaceServiceProvider extends HandlerThread implements INestedStruct1InterfaceServiceProvider
{
	private NestedStruct1InterfaceService m_Service;
	private static final String TAG = "NestedStruct1InterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NestedStruct1InterfaceServiceProvider get()
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

	public synchronized  AbstractNestedStruct1Interface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new NestedStruct1InterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new NestedStruct1InterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final NestedStruct1InterfaceServiceProvider INSTANCE = createInstance();
	}

	private NestedStruct1InterfaceServiceProvider()
	{
		super("NestedStruct1InterfaceServiceProvider");
	}

	@NonNull
	private static NestedStruct1InterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		NestedStruct1InterfaceServiceProvider t = new NestedStruct1InterfaceServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
