// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimple_android_service;

import tbSimple.tbSimple_android_service.INoOperationsInterfaceServiceFactory;
import tbSimple.tbSimple_api.INoOperationsInterface;
import tbSimple.tbSimple_api.AbstractNoOperationsInterface;
import tbSimple.tbSimple_impl.NoOperationsInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NoOperationsInterfaceServiceFactory thread for the system.  This is a thread for
 * NoOperationsInterfaceServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NoOperationsInterfaceServiceFactory extends HandlerThread implements INoOperationsInterfaceServiceFactory
{
	private NoOperationsInterfaceService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NoOperationsInterfaceServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: NoOperationsInterfaceServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractNoOperationsInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new NoOperationsInterfaceService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new NoOperationsInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final NoOperationsInterfaceServiceFactory INSTANCE = createInstance();
	}

	private NoOperationsInterfaceServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static NoOperationsInterfaceServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		NoOperationsInterfaceServiceFactory t = new NoOperationsInterfaceServiceFactory();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
