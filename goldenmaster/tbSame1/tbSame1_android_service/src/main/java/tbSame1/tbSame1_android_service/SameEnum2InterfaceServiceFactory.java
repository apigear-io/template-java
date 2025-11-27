// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame1.tbSame1_android_service;

import tbSame1.tbSame1_android_service.ISameEnum2InterfaceServiceFactory;
import tbSame1.tbSame1_api.ISameEnum2Interface;
import tbSame1.tbSame1_api.AbstractSameEnum2Interface;
import tbSame1.tbSame1_impl.SameEnum2InterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SameEnum2InterfaceServiceFactory thread for the system.  This is a thread for
 * SameEnum2InterfaceServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SameEnum2InterfaceServiceFactory extends HandlerThread implements ISameEnum2InterfaceServiceFactory
{
	private SameEnum2InterfaceService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SameEnum2InterfaceServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: SameEnum2InterfaceServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractSameEnum2Interface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new SameEnum2InterfaceService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new SameEnum2InterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final SameEnum2InterfaceServiceFactory INSTANCE = createInstance();
	}

	private SameEnum2InterfaceServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static SameEnum2InterfaceServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		SameEnum2InterfaceServiceFactory t = new SameEnum2InterfaceServiceFactory();
		t.start();
		return t;
	}
}
