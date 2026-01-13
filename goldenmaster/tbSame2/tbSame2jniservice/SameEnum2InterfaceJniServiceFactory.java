// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame2.tbSame2jniservice;

import tbSame2.tbSame2_android_service.ISameEnum2InterfaceServiceFactory;
import tbSame2.tbSame2_api.ISameEnum2Interface;
import tbSame2.tbSame2_api.AbstractSameEnum2Interface;
import tbSame2.tbSame2jniservice.SameEnum2InterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SameEnum2InterfaceJniServiceFactory thread for the system.  This is a thread for
 * SameEnum2InterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SameEnum2InterfaceJniServiceFactory extends HandlerThread implements ISameEnum2InterfaceServiceFactory
{
	private SameEnum2InterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SameEnum2InterfaceJniServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: SameEnum2InterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractSameEnum2Interface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new SameEnum2InterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new SameEnum2InterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final SameEnum2InterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private SameEnum2InterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static SameEnum2InterfaceJniServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		SameEnum2InterfaceJniServiceFactory t = new SameEnum2InterfaceJniServiceFactory();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
