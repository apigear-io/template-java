// Copyright Epic Games, Inc. All Rights Reserved.

package tbRefIfaces.tbRefIfacesjniservice;

import tbRefIfaces.tbRefIfaces_android_service.IParentIfServiceFactory;
import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_api.AbstractParentIf;
import tbRefIfaces.tbRefIfacesjniservice.ParentIfJniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton ParentIfJniServiceFactory thread for the system.  This is a thread for
 * ParentIfJniServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class ParentIfJniServiceFactory extends HandlerThread implements IParentIfServiceFactory
{
	private ParentIfJniService jniService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static ParentIfJniServiceFactory get()
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
			Log.w("UE", "LIFECYCLE: ParentIfJniServiceFactory::onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractParentIf getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new ParentIfJniService();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new ParentIfJniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final ParentIfJniServiceFactory INSTANCE = createInstance();
	}

	private ParentIfJniServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static ParentIfJniServiceFactory createInstance()
	{
		Log.w("UE", "LIFECYCLE: EngineFactory::createInstance()");

		ParentIfJniServiceFactory t = new ParentIfJniServiceFactory();
		t.start();
		return t;
	}
}
