// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame2.tbSame2_android_service;

import tbSame2.tbSame2_android_service.ISameStruct2InterfaceServiceFactory;
import tbSame2.tbSame2_api.ISameStruct2Interface;
import tbSame2.tbSame2_api.AbstractSameStruct2Interface;
import tbSame2.tbSame2_impl.SameStruct2InterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SameStruct2InterfaceServiceFactory thread for the system.  This is a thread for
 * SameStruct2InterfaceServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SameStruct2InterfaceServiceFactory extends HandlerThread implements ISameStruct2InterfaceServiceFactory
{
	private SameStruct2InterfaceService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SameStruct2InterfaceServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: SameStruct2InterfaceServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractSameStruct2Interface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new SameStruct2InterfaceService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new SameStruct2InterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final SameStruct2InterfaceServiceFactory INSTANCE = createInstance();
	}

	private SameStruct2InterfaceServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static SameStruct2InterfaceServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		SameStruct2InterfaceServiceFactory t = new SameStruct2InterfaceServiceFactory();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
