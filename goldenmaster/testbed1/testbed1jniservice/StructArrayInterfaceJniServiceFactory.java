// Copyright Epic Games, Inc. All Rights Reserved.

package testbed1.testbed1jniservice;

import testbed1.testbed1_android_service.IStructArrayInterfaceServiceFactory;
import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_api.AbstractStructArrayInterface;
import testbed1.testbed1jniservice.StructArrayInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton StructArrayInterfaceJniServiceFactory thread for the system.  This is a thread for
 * StructArrayInterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class StructArrayInterfaceJniServiceFactory extends HandlerThread implements IStructArrayInterfaceServiceFactory
{
	private StructArrayInterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static StructArrayInterfaceJniServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: StructArrayInterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractStructArrayInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new StructArrayInterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new StructArrayInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final StructArrayInterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private StructArrayInterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static StructArrayInterfaceJniServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		StructArrayInterfaceJniServiceFactory t = new StructArrayInterfaceJniServiceFactory();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
