// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame2.tbSame2_android_service;

import tbSame2.tbSame2_android_service.ISameEnum1InterfaceServiceFactory;
import tbSame2.tbSame2_api.ISameEnum1Interface;
import tbSame2.tbSame2_api.AbstractSameEnum1Interface;
import tbSame2.tbSame2_impl.SameEnum1InterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SameEnum1InterfaceServiceFactory thread for the system.  This is a thread for
 * SameEnum1InterfaceServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SameEnum1InterfaceServiceFactory extends HandlerThread implements ISameEnum1InterfaceServiceFactory
{
	private SameEnum1InterfaceService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SameEnum1InterfaceServiceFactory get()
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
			Log.w("UE", "LIFECYCLE: SameEnum1InterfaceServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractSameEnum1Interface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new SameEnum1InterfaceService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new SameEnum1InterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final SameEnum1InterfaceServiceFactory INSTANCE = createInstance();
	}

	private SameEnum1InterfaceServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static SameEnum1InterfaceServiceFactory createInstance()
	{
		Log.w("UE", "LIFECYCLE: EngineFactory::createInstance()");

		SameEnum1InterfaceServiceFactory t = new SameEnum1InterfaceServiceFactory();
		t.start();
		return t;
	}
}
