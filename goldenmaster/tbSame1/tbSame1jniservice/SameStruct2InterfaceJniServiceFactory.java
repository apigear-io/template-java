// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame1.tbSame1jniservice;

import tbSame1.tbSame1_android_service.ISameStruct2InterfaceServiceFactory;
import tbSame1.tbSame1_api.ISameStruct2Interface;
import tbSame1.tbSame1_api.AbstractSameStruct2Interface;
import tbSame1.tbSame1jniservice.SameStruct2InterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton SameStruct2InterfaceJniServiceFactory thread for the system.  This is a thread for
 * SameStruct2InterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class SameStruct2InterfaceJniServiceFactory extends HandlerThread implements ISameStruct2InterfaceServiceFactory
{
	private SameStruct2InterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static SameStruct2InterfaceJniServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: SameStruct2InterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractSameStruct2Interface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new SameStruct2InterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new SameStruct2InterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final SameStruct2InterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private SameStruct2InterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static SameStruct2InterfaceJniServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		SameStruct2InterfaceJniServiceFactory t = new SameStruct2InterfaceJniServiceFactory();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
