// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimplejniservice;

import tbSimple.tbSimple_android_service.INoPropertiesInterfaceServiceFactory;
import tbSimple.tbSimple_api.INoPropertiesInterface;
import tbSimple.tbSimple_api.AbstractNoPropertiesInterface;
import tbSimple.tbSimplejniservice.NoPropertiesInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NoPropertiesInterfaceJniServiceFactory thread for the system.  This is a thread for
 * NoPropertiesInterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NoPropertiesInterfaceJniServiceFactory extends HandlerThread implements INoPropertiesInterfaceServiceFactory
{
	private NoPropertiesInterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NoPropertiesInterfaceJniServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: NoPropertiesInterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractNoPropertiesInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new NoPropertiesInterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new NoPropertiesInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final NoPropertiesInterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private NoPropertiesInterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static NoPropertiesInterfaceJniServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		NoPropertiesInterfaceJniServiceFactory t = new NoPropertiesInterfaceJniServiceFactory();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
