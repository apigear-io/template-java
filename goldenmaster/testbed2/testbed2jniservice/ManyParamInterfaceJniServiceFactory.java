// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2jniservice;

import testbed2.testbed2_android_service.IManyParamInterfaceServiceFactory;
import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_api.AbstractManyParamInterface;
import testbed2.testbed2jniservice.ManyParamInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton ManyParamInterfaceJniServiceFactory thread for the system.  This is a thread for
 * ManyParamInterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class ManyParamInterfaceJniServiceFactory extends HandlerThread implements IManyParamInterfaceServiceFactory
{
	private ManyParamInterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static ManyParamInterfaceJniServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: ManyParamInterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractManyParamInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new ManyParamInterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new ManyParamInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final ManyParamInterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private ManyParamInterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static ManyParamInterfaceJniServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		ManyParamInterfaceJniServiceFactory t = new ManyParamInterfaceJniServiceFactory();
		t.start();
		return t;
	}
}
