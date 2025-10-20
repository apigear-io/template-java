// Copyright Epic Games, Inc. All Rights Reserved.

package tbRefIfaces.tbRefIfaces_android_service;

import tbRefIfaces.tbRefIfaces_android_service.ISimpleLocalIfServiceFactory;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_api.AbstractSimpleLocalIf;
import tbRefIfaces.tbRefIfaces_impl.SimpleLocalIfService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SimpleLocalIfServiceFactory thread for the system.  This is a thread for
 * SimpleLocalIfServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SimpleLocalIfServiceFactory extends HandlerThread implements ISimpleLocalIfServiceFactory
{
	private SimpleLocalIfService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SimpleLocalIfServiceFactory get()
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
			Log.w("UE", "LIFECYCLE: SimpleLocalIfServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractSimpleLocalIf getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new SimpleLocalIfService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new SimpleLocalIfService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final SimpleLocalIfServiceFactory INSTANCE = createInstance();
	}

	private SimpleLocalIfServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static SimpleLocalIfServiceFactory createInstance()
	{
		Log.w("UE", "LIFECYCLE: EngineFactory::createInstance()");

		SimpleLocalIfServiceFactory t = new SimpleLocalIfServiceFactory();
		t.start();
		return t;
	}
}
