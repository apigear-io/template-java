// Copyright Epic Games, Inc. All Rights Reserved.

package tbEnum.tbEnum_android_service;

import tbEnum.tbEnum_android_service.IEnumInterfaceServiceFactory;
import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_api.AbstractEnumInterface;
import tbEnum.tbEnum_impl.EnumInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton EnumInterfaceServiceFactory thread for the system.  This is a thread for
 * EnumInterfaceServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class EnumInterfaceServiceFactory extends HandlerThread implements IEnumInterfaceServiceFactory
{
	private EnumInterfaceService m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static EnumInterfaceServiceFactory get()
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
			Log.w("UE", "LIFECYCLE: EnumInterfaceServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractEnumInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new EnumInterfaceService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new EnumInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final EnumInterfaceServiceFactory INSTANCE = createInstance();
	}

	private EnumInterfaceServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static EnumInterfaceServiceFactory createInstance()
	{
		Log.w("UE", "LIFECYCLE: EngineFactory::createInstance()");

		EnumInterfaceServiceFactory t = new EnumInterfaceServiceFactory();
		t.start();
		return t;
	}
}
