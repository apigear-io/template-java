// Copyright Epic Games, Inc. All Rights Reserved.

package tbNames.tbNames_android_service;

import tbNames.tbNames_android_service.INamEsServiceFactory;
import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_api.AbstractNamEs;
import tbNames.tbNames_impl.NamEsService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NamEsServiceFactory thread for the system.  This is a thread for
 * NamEsServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NamEsServiceFactory extends HandlerThread implements INamEsServiceFactory
{
	private NamEsService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NamEsServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: NamEsServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractNamEs getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new NamEsService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new NamEsService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final NamEsServiceFactory INSTANCE = createInstance();
	}

	private NamEsServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static NamEsServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		NamEsServiceFactory t = new NamEsServiceFactory();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
