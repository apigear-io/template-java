// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimple_android_service;

import tbSimple.tbSimple_android_service.INoSignalsInterfaceServiceFactory;
import tbSimple.tbSimple_api.INoSignalsInterface;
import tbSimple.tbSimple_api.AbstractNoSignalsInterface;
import tbSimple.tbSimple_impl.NoSignalsInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NoSignalsInterfaceServiceFactory thread for the system.  This is a thread for
 * NoSignalsInterfaceServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NoSignalsInterfaceServiceFactory extends HandlerThread implements INoSignalsInterfaceServiceFactory
{
	private NoSignalsInterfaceService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NoSignalsInterfaceServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: NoSignalsInterfaceServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractNoSignalsInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new NoSignalsInterfaceService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new NoSignalsInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final NoSignalsInterfaceServiceFactory INSTANCE = createInstance();
	}

	private NoSignalsInterfaceServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static NoSignalsInterfaceServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		NoSignalsInterfaceServiceFactory t = new NoSignalsInterfaceServiceFactory();
		t.start();
		return t;
	}
}
