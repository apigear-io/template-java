// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2jniservice;

import testbed2.testbed2_android_service.INestedStruct3InterfaceServiceFactory;
import testbed2.testbed2_api.INestedStruct3Interface;
import testbed2.testbed2_api.AbstractNestedStruct3Interface;
import testbed2.testbed2jniservice.NestedStruct3InterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NestedStruct3InterfaceJniServiceFactory thread for the system.  This is a thread for
 * NestedStruct3InterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NestedStruct3InterfaceJniServiceFactory extends HandlerThread implements INestedStruct3InterfaceServiceFactory
{
	private NestedStruct3InterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NestedStruct3InterfaceJniServiceFactory get()
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
			Log.w("UE", "LIFECYCLE: NestedStruct3InterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractNestedStruct3Interface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new NestedStruct3InterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new NestedStruct3InterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final NestedStruct3InterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private NestedStruct3InterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static NestedStruct3InterfaceJniServiceFactory createInstance()
	{
		Log.w("UE", "LIFECYCLE: EngineFactory::createInstance()");

		NestedStruct3InterfaceJniServiceFactory t = new NestedStruct3InterfaceJniServiceFactory();
		t.start();
		return t;
	}
}
