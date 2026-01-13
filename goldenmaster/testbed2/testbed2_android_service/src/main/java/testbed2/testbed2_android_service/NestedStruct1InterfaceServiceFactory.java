// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2_android_service;

import testbed2.testbed2_android_service.INestedStruct1InterfaceServiceFactory;
import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_api.AbstractNestedStruct1Interface;
import testbed2.testbed2_impl.NestedStruct1InterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NestedStruct1InterfaceServiceFactory thread for the system.  This is a thread for
 * NestedStruct1InterfaceServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NestedStruct1InterfaceServiceFactory extends HandlerThread implements INestedStruct1InterfaceServiceFactory
{
	private NestedStruct1InterfaceService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NestedStruct1InterfaceServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: NestedStruct1InterfaceServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractNestedStruct1Interface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new NestedStruct1InterfaceService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new NestedStruct1InterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final NestedStruct1InterfaceServiceFactory INSTANCE = createInstance();
	}

	private NestedStruct1InterfaceServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static NestedStruct1InterfaceServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		NestedStruct1InterfaceServiceFactory t = new NestedStruct1InterfaceServiceFactory();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
