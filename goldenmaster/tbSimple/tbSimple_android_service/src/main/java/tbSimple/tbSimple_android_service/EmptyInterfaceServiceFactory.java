// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimple_android_service;

import tbSimple.tbSimple_android_service.IEmptyInterfaceServiceFactory;
import tbSimple.tbSimple_api.IEmptyInterface;
import tbSimple.tbSimple_api.AbstractEmptyInterface;
import tbSimple.tbSimple_impl.EmptyInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton EmptyInterfaceServiceFactory thread for the system.  This is a thread for
 * EmptyInterfaceServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class EmptyInterfaceServiceFactory extends HandlerThread implements IEmptyInterfaceServiceFactory
{
	private EmptyInterfaceService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static EmptyInterfaceServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: EmptyInterfaceServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractEmptyInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new EmptyInterfaceService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new EmptyInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final EmptyInterfaceServiceFactory INSTANCE = createInstance();
	}

	private EmptyInterfaceServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static EmptyInterfaceServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		EmptyInterfaceServiceFactory t = new EmptyInterfaceServiceFactory();
		t.start();
		return t;
	}
}
