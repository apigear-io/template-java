// Copyright Epic Games, Inc. All Rights Reserved.

package tbIfaceimport.tbIfaceimportjniservice;

import tbIfaceimport.tbIfaceimport_android_service.IEmptyIfServiceFactory;
import tbIfaceimport.tbIfaceimport_api.IEmptyIf;
import tbIfaceimport.tbIfaceimport_api.AbstractEmptyIf;
import tbIfaceimport.tbIfaceimportjniservice.EmptyIfJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton EmptyIfJniServiceFactory thread for the system.  This is a thread for
 * EmptyIfJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class EmptyIfJniServiceFactory extends HandlerThread implements IEmptyIfServiceFactory
{
	private EmptyIfJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static EmptyIfJniServiceFactory get()
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
			Log.w("UE", "LIFECYCLE: EmptyIfJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractEmptyIf getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new EmptyIfJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new EmptyIfJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final EmptyIfJniServiceFactory INSTANCE = createInstance();
	}

	private EmptyIfJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static EmptyIfJniServiceFactory createInstance()
	{
		Log.w("UE", "LIFECYCLE: EngineFactory::createInstance()");

		EmptyIfJniServiceFactory t = new EmptyIfJniServiceFactory();
		t.start();
		return t;
	}
}
