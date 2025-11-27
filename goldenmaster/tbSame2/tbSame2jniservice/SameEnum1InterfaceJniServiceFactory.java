// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame2.tbSame2jniservice;

import tbSame2.tbSame2_android_service.ISameEnum1InterfaceServiceFactory;
import tbSame2.tbSame2_api.ISameEnum1Interface;
import tbSame2.tbSame2_api.AbstractSameEnum1Interface;
import tbSame2.tbSame2jniservice.SameEnum1InterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SameEnum1InterfaceJniServiceFactory thread for the system.  This is a thread for
 * SameEnum1InterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SameEnum1InterfaceJniServiceFactory extends HandlerThread implements ISameEnum1InterfaceServiceFactory
{
	private SameEnum1InterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SameEnum1InterfaceJniServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: SameEnum1InterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractSameEnum1Interface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new SameEnum1InterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new SameEnum1InterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final SameEnum1InterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private SameEnum1InterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static SameEnum1InterfaceJniServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		SameEnum1InterfaceJniServiceFactory t = new SameEnum1InterfaceJniServiceFactory();
		t.start();
		return t;
	}
}
