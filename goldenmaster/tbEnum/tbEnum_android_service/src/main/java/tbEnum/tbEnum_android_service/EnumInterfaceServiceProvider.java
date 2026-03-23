// Copyright Epic Games, Inc. All Rights Reserved.

package tbEnum.tbEnum_android_service;

import tbEnum.tbEnum_android_service.IEnumInterfaceServiceProvider;
import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_api.AbstractEnumInterface;
import tbEnum.tbEnum_impl.EnumInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton EnumInterfaceServiceProvider thread for the system.  This is a thread for
 * EnumInterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class EnumInterfaceServiceProvider extends HandlerThread implements IEnumInterfaceServiceProvider
{
	private EnumInterfaceService m_Service;
	private static final String TAG = "EnumInterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static EnumInterfaceServiceProvider get()
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
			Log.i(TAG, "LIFECYCLE: onDestroy() - stop instance thread, service = " + m_Service);
			clear();
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractEnumInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new EnumInterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new EnumInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final EnumInterfaceServiceProvider INSTANCE = createInstance();
	}

	private EnumInterfaceServiceProvider()
	{
		super("EnumInterfaceServiceProvider");
	}

	@NonNull
	private static EnumInterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		EnumInterfaceServiceProvider t = new EnumInterfaceServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		if (m_Service != null)
		{
			m_Service._shutdown();
		}
		m_Service = null;
	}
}
