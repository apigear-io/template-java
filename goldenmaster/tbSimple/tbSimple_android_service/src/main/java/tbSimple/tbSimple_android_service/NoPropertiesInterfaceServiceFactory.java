// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimple_android_service;

import tbSimple.tbSimple_android_service.INoPropertiesInterfaceServiceFactory;
import tbSimple.tbSimple_api.INoPropertiesInterface;
import tbSimple.tbSimple_api.AbstractNoPropertiesInterface;
import tbSimple.tbSimple_impl.NoPropertiesInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NoPropertiesInterfaceServiceFactory thread for the system.  This is a thread for
 * NoPropertiesInterfaceServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NoPropertiesInterfaceServiceFactory extends HandlerThread implements INoPropertiesInterfaceServiceFactory
{
	private NoPropertiesInterfaceService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NoPropertiesInterfaceServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: NoPropertiesInterfaceServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractNoPropertiesInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new NoPropertiesInterfaceService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new NoPropertiesInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final NoPropertiesInterfaceServiceFactory INSTANCE = createInstance();
	}

	private NoPropertiesInterfaceServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static NoPropertiesInterfaceServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		NoPropertiesInterfaceServiceFactory t = new NoPropertiesInterfaceServiceFactory();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
