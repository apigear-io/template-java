// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimple_android_service;

import tbSimple.tbSimple_android_service.ISimpleArrayInterfaceServiceFactory;
import tbSimple.tbSimple_api.ISimpleArrayInterface;
import tbSimple.tbSimple_api.AbstractSimpleArrayInterface;
import tbSimple.tbSimple_impl.SimpleArrayInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SimpleArrayInterfaceServiceFactory thread for the system.  This is a thread for
 * SimpleArrayInterfaceServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SimpleArrayInterfaceServiceFactory extends HandlerThread implements ISimpleArrayInterfaceServiceFactory
{
	private SimpleArrayInterfaceService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SimpleArrayInterfaceServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: SimpleArrayInterfaceServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractSimpleArrayInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new SimpleArrayInterfaceService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new SimpleArrayInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final SimpleArrayInterfaceServiceFactory INSTANCE = createInstance();
	}

	private SimpleArrayInterfaceServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static SimpleArrayInterfaceServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		SimpleArrayInterfaceServiceFactory t = new SimpleArrayInterfaceServiceFactory();
		t.start();
		return t;
	}
}
