// Copyright Epic Games, Inc. All Rights Reserved.

package testbed1.testbed1_android_service;

import testbed1.testbed1_android_service.IStructArray2InterfaceServiceProvider;
import testbed1.testbed1_api.IStructArray2Interface;
import testbed1.testbed1_api.AbstractStructArray2Interface;
import testbed1.testbed1_impl.StructArray2InterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton StructArray2InterfaceServiceProvider thread for the system.  This is a thread for
 * StructArray2InterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class StructArray2InterfaceServiceProvider extends HandlerThread implements IStructArray2InterfaceServiceProvider
{
	private StructArray2InterfaceService m_Service;
	private static final String TAG = "StructArray2InterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static StructArray2InterfaceServiceProvider get()
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

	public synchronized  AbstractStructArray2Interface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new StructArray2InterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new StructArray2InterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final StructArray2InterfaceServiceProvider INSTANCE = createInstance();
	}

	private StructArray2InterfaceServiceProvider()
	{
		super("StructArray2InterfaceServiceProvider");
	}

	@NonNull
	private static StructArray2InterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		StructArray2InterfaceServiceProvider t = new StructArray2InterfaceServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
