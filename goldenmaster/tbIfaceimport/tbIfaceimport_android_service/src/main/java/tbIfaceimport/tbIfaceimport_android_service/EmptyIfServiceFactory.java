// Copyright Epic Games, Inc. All Rights Reserved.

package tbIfaceimport.tbIfaceimport_android_service;

import tbIfaceimport.tbIfaceimport_android_service.IEmptyIfServiceFactory;
import tbIfaceimport.tbIfaceimport_api.IEmptyIf;
import tbIfaceimport.tbIfaceimport_api.AbstractEmptyIf;
import tbIfaceimport.tbIfaceimport_impl.EmptyIfService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton EmptyIfServiceFactory thread for the system.  This is a thread for
 * EmptyIfServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class EmptyIfServiceFactory extends HandlerThread implements IEmptyIfServiceFactory
{
	private EmptyIfService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static EmptyIfServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: EmptyIfServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractEmptyIf getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new EmptyIfService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new EmptyIfService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final EmptyIfServiceFactory INSTANCE = createInstance();
	}

	private EmptyIfServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static EmptyIfServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		EmptyIfServiceFactory t = new EmptyIfServiceFactory();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
