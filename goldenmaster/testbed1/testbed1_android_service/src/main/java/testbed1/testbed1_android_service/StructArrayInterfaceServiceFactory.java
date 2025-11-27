// Copyright Epic Games, Inc. All Rights Reserved.

package testbed1.testbed1_android_service;

import testbed1.testbed1_android_service.IStructArrayInterfaceServiceFactory;
import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_api.AbstractStructArrayInterface;
import testbed1.testbed1_impl.StructArrayInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton StructArrayInterfaceServiceFactory thread for the system.  This is a thread for
 * StructArrayInterfaceServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class StructArrayInterfaceServiceFactory extends HandlerThread implements IStructArrayInterfaceServiceFactory
{
	private StructArrayInterfaceService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static StructArrayInterfaceServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: StructArrayInterfaceServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractStructArrayInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new StructArrayInterfaceService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new StructArrayInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final StructArrayInterfaceServiceFactory INSTANCE = createInstance();
	}

	private StructArrayInterfaceServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static StructArrayInterfaceServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		StructArrayInterfaceServiceFactory t = new StructArrayInterfaceServiceFactory();
		t.start();
		return t;
	}
}
