// Copyright Epic Games, Inc. All Rights Reserved.

package testbed1.testbed1jniservice;

import testbed1.testbed1_android_service.IStructArray2InterfaceServiceFactory;
import testbed1.testbed1_api.IStructArray2Interface;
import testbed1.testbed1_api.AbstractStructArray2Interface;
import testbed1.testbed1jniservice.StructArray2InterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton StructArray2InterfaceJniServiceFactory thread for the system.  This is a thread for
 * StructArray2InterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class StructArray2InterfaceJniServiceFactory extends HandlerThread implements IStructArray2InterfaceServiceFactory
{
	private StructArray2InterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static StructArray2InterfaceJniServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: StructArray2InterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractStructArray2Interface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new StructArray2InterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new StructArray2InterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final StructArray2InterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private StructArray2InterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static StructArray2InterfaceJniServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		StructArray2InterfaceJniServiceFactory t = new StructArray2InterfaceJniServiceFactory();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
