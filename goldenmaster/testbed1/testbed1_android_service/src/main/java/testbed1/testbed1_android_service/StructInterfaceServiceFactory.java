// Copyright Epic Games, Inc. All Rights Reserved.

package testbed1.testbed1_android_service;

import testbed1.testbed1_android_service.IStructInterfaceServiceFactory;
import testbed1.testbed1_api.IStructInterface;
import testbed1.testbed1_api.AbstractStructInterface;
import testbed1.testbed1_impl.StructInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton StructInterfaceServiceFactory thread for the system.  This is a thread for
 * StructInterfaceServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class StructInterfaceServiceFactory extends HandlerThread implements IStructInterfaceServiceFactory
{
	private StructInterfaceService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static StructInterfaceServiceFactory get()
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
			Log.w("UE", "LIFECYCLE: StructInterfaceServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractStructInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new StructInterfaceService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new StructInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final StructInterfaceServiceFactory INSTANCE = createInstance();
	}

	private StructInterfaceServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static StructInterfaceServiceFactory createInstance()
	{
		Log.w("UE", "LIFECYCLE: EngineFactory::createInstance()");

		StructInterfaceServiceFactory t = new StructInterfaceServiceFactory();
		t.start();
		return t;
	}
}
