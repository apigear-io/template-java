// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimple_android_service;

import tbSimple.tbSimple_android_service.ISimpleInterfaceServiceFactory;
import tbSimple.tbSimple_api.ISimpleInterface;
import tbSimple.tbSimple_api.AbstractSimpleInterface;
import tbSimple.tbSimple_impl.SimpleInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SimpleInterfaceServiceFactory thread for the system.  This is a thread for
 * SimpleInterfaceServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SimpleInterfaceServiceFactory extends HandlerThread implements ISimpleInterfaceServiceFactory
{
	private SimpleInterfaceService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SimpleInterfaceServiceFactory get()
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
			Log.w("UE", "LIFECYCLE: SimpleInterfaceServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractSimpleInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new SimpleInterfaceService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new SimpleInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final SimpleInterfaceServiceFactory INSTANCE = createInstance();
	}

	private SimpleInterfaceServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static SimpleInterfaceServiceFactory createInstance()
	{
		Log.w("UE", "LIFECYCLE: EngineFactory::createInstance()");

		SimpleInterfaceServiceFactory t = new SimpleInterfaceServiceFactory();
		t.start();
		return t;
	}
}
