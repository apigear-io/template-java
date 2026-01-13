// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimplejniservice;

import tbSimple.tbSimple_android_service.IVoidInterfaceServiceFactory;
import tbSimple.tbSimple_api.IVoidInterface;
import tbSimple.tbSimple_api.AbstractVoidInterface;
import tbSimple.tbSimplejniservice.VoidInterfaceJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton VoidInterfaceJniServiceFactory thread for the system.  This is a thread for
 * VoidInterfaceJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class VoidInterfaceJniServiceFactory extends HandlerThread implements IVoidInterfaceServiceFactory
{
	private VoidInterfaceJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static VoidInterfaceJniServiceFactory get()
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
			Log.i("UE", "LIFECYCLE: VoidInterfaceJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractVoidInterface getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new VoidInterfaceJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new VoidInterfaceJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final VoidInterfaceJniServiceFactory INSTANCE = createInstance();
	}

	private VoidInterfaceJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static VoidInterfaceJniServiceFactory createInstance()
	{
		Log.i("UE", "LIFECYCLE: EngineFactory::createInstance()");

		VoidInterfaceJniServiceFactory t = new VoidInterfaceJniServiceFactory();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
