// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame2.tbSame2_android_service;

import tbSame2.tbSame2_android_service.ISameStruct1InterfaceServiceFactory;
import tbSame2.tbSame2_api.ISameStruct1Interface;
import tbSame2.tbSame2_api.AbstractSameStruct1Interface;
import tbSame2.tbSame2_impl.SameStruct1InterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SameStruct1InterfaceServiceFactory thread for the system.  This is a thread for
 * SameStruct1InterfaceServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SameStruct1InterfaceServiceFactory extends HandlerThread implements ISameStruct1InterfaceServiceFactory
{
	private SameStruct1InterfaceService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SameStruct1InterfaceServiceFactory get()
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
			Log.w("UE", "LIFECYCLE: SameStruct1InterfaceServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractSameStruct1Interface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new SameStruct1InterfaceService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new SameStruct1InterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final SameStruct1InterfaceServiceFactory INSTANCE = createInstance();
	}

	private SameStruct1InterfaceServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static SameStruct1InterfaceServiceFactory createInstance()
	{
		Log.w("UE", "LIFECYCLE: EngineFactory::createInstance()");

		SameStruct1InterfaceServiceFactory t = new SameStruct1InterfaceServiceFactory();
		t.start();
		return t;
	}
}
