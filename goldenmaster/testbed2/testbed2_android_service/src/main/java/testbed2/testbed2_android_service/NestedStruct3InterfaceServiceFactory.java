// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2_android_service;

import testbed2.testbed2_android_service.INestedStruct3InterfaceServiceFactory;
import testbed2.testbed2_api.INestedStruct3Interface;
import testbed2.testbed2_api.AbstractNestedStruct3Interface;
import testbed2.testbed2_impl.NestedStruct3InterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NestedStruct3InterfaceServiceFactory thread for the system.  This is a thread for
 * NestedStruct3InterfaceServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NestedStruct3InterfaceServiceFactory extends HandlerThread implements INestedStruct3InterfaceServiceFactory
{
	private NestedStruct3InterfaceService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NestedStruct3InterfaceServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: NestedStruct3InterfaceServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractNestedStruct3Interface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new NestedStruct3InterfaceService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new NestedStruct3InterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final NestedStruct3InterfaceServiceFactory INSTANCE = createInstance();
	}

	private NestedStruct3InterfaceServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static NestedStruct3InterfaceServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		NestedStruct3InterfaceServiceFactory t = new NestedStruct3InterfaceServiceFactory();
		t.start();
		return t;
	}
}
