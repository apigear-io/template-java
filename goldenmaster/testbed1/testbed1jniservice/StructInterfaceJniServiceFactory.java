// Copyright Epic Games, Inc. All Rights Reserved.

package testbed1.testbed1jniservice;

import testbed1.testbed1_android_service.IStructInterfaceServiceFactory;
import testbed1.testbed1_api.IStructInterface;
import testbed1.testbed1_api.AbstractStructInterface;
import testbed1.testbed1jniservice.StructInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton StructInterfaceJniServiceFactory thread for the system.  This is a thread for
 * StructInterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class StructInterfaceJniServiceFactory extends HandlerThread implements IStructInterfaceServiceFactory
{
	private StructInterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static StructInterfaceJniServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: StructInterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractStructInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new StructInterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new StructInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final StructInterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private StructInterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static StructInterfaceJniServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		StructInterfaceJniServiceFactory t = new StructInterfaceJniServiceFactory();
		t.start();
		return t;
	}
}
