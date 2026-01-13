// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame1.tbSame1jniservice;

import tbSame1.tbSame1_android_service.ISameStruct1InterfaceServiceFactory;
import tbSame1.tbSame1_api.ISameStruct1Interface;
import tbSame1.tbSame1_api.AbstractSameStruct1Interface;
import tbSame1.tbSame1jniservice.SameStruct1InterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SameStruct1InterfaceJniServiceFactory thread for the system.  This is a thread for
 * SameStruct1InterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SameStruct1InterfaceJniServiceFactory extends HandlerThread implements ISameStruct1InterfaceServiceFactory
{
	private SameStruct1InterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SameStruct1InterfaceJniServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: SameStruct1InterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractSameStruct1Interface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new SameStruct1InterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new SameStruct1InterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final SameStruct1InterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private SameStruct1InterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static SameStruct1InterfaceJniServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		SameStruct1InterfaceJniServiceFactory t = new SameStruct1InterfaceJniServiceFactory();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
