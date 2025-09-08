// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2_android_service;

import testbed2.testbed2_android_service.IManyParamInterfaceServiceFactory;
import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_api.AbstractManyParamInterface;
import testbed2.testbed2_impl.ManyParamInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton ManyParamInterfaceServiceFactory thread for the system.  This is a thread for
 * ManyParamInterfaceServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class ManyParamInterfaceServiceFactory extends HandlerThread implements IManyParamInterfaceServiceFactory
{
	private ManyParamInterfaceService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static ManyParamInterfaceServiceFactory get()
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
			Log.w("UE", "LIFECYCLE: ManyParamInterfaceServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractManyParamInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new ManyParamInterfaceService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new ManyParamInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final ManyParamInterfaceServiceFactory INSTANCE = createInstance();
	}

	private ManyParamInterfaceServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static ManyParamInterfaceServiceFactory createInstance()
	{
		Log.w("UE", "LIFECYCLE: EngineFactory::createInstance()");

		ManyParamInterfaceServiceFactory t = new ManyParamInterfaceServiceFactory();
		t.start();
		return t;
	}
}
