// Copyright Epic Games, Inc. All Rights Reserved.

package tbEnum.tbEnumjniservice;

import tbEnum.tbEnum_android_service.IEnumInterfaceServiceFactory;
import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_api.AbstractEnumInterface;
import tbEnum.tbEnumjniservice.EnumInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton EnumInterfaceJniServiceFactory thread for the system.  This is a thread for
 * EnumInterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class EnumInterfaceJniServiceFactory extends HandlerThread implements IEnumInterfaceServiceFactory
{
	private EnumInterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static EnumInterfaceJniServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: EnumInterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractEnumInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new EnumInterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new EnumInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final EnumInterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private EnumInterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static EnumInterfaceJniServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		EnumInterfaceJniServiceFactory t = new EnumInterfaceJniServiceFactory();
		t.start();
		return t;
	}
}
