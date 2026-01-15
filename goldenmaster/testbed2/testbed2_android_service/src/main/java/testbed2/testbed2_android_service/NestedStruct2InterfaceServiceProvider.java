// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2_android_service;

import testbed2.testbed2_android_service.INestedStruct2InterfaceServiceProvider;
import testbed2.testbed2_api.INestedStruct2Interface;
import testbed2.testbed2_api.AbstractNestedStruct2Interface;
import testbed2.testbed2_impl.NestedStruct2InterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NestedStruct2InterfaceServiceProvider thread for the system.  This is a thread for
 * NestedStruct2InterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NestedStruct2InterfaceServiceProvider extends HandlerThread implements INestedStruct2InterfaceServiceProvider
{
	private NestedStruct2InterfaceService m_Service;
	private static final String TAG = "NestedStruct2InterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NestedStruct2InterfaceServiceProvider get()
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

	public synchronized  AbstractNestedStruct2Interface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new NestedStruct2InterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new NestedStruct2InterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final NestedStruct2InterfaceServiceProvider INSTANCE = createInstance();
	}

	private NestedStruct2InterfaceServiceProvider()
	{
		super("NestedStruct2InterfaceServiceProvider");
	}

	@NonNull
	private static NestedStruct2InterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		NestedStruct2InterfaceServiceProvider t = new NestedStruct2InterfaceServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
