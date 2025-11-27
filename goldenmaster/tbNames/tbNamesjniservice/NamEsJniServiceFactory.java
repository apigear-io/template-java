// Copyright Epic Games, Inc. All Rights Reserved.

package tbNames.tbNamesjniservice;

import tbNames.tbNames_android_service.INamEsServiceFactory;
import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_api.AbstractNamEs;
import tbNames.tbNamesjniservice.NamEsJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NamEsJniServiceFactory thread for the system.  This is a thread for
 * NamEsJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NamEsJniServiceFactory extends HandlerThread implements INamEsServiceFactory
{
	private NamEsJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NamEsJniServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: NamEsJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractNamEs getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new NamEsJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new NamEsJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final NamEsJniServiceFactory INSTANCE = createInstance();
	}

	private NamEsJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static NamEsJniServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		NamEsJniServiceFactory t = new NamEsJniServiceFactory();
		t.start();
		return t;
	}
}
