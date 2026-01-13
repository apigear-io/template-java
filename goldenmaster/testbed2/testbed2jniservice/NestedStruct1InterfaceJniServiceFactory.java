// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2jniservice;

import testbed2.testbed2_android_service.INestedStruct1InterfaceServiceFactory;
import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_api.AbstractNestedStruct1Interface;
import testbed2.testbed2jniservice.NestedStruct1InterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NestedStruct1InterfaceJniServiceFactory thread for the system.  This is a thread for
 * NestedStruct1InterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NestedStruct1InterfaceJniServiceFactory extends HandlerThread implements INestedStruct1InterfaceServiceFactory
{
	private NestedStruct1InterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NestedStruct1InterfaceJniServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: NestedStruct1InterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractNestedStruct1Interface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new NestedStruct1InterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new NestedStruct1InterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final NestedStruct1InterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private NestedStruct1InterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static NestedStruct1InterfaceJniServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		NestedStruct1InterfaceJniServiceFactory t = new NestedStruct1InterfaceJniServiceFactory();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
