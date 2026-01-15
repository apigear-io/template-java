// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimple_android_service;

import tbSimple.tbSimple_android_service.INoPropertiesInterfaceServiceProvider;
import tbSimple.tbSimple_api.INoPropertiesInterface;
import tbSimple.tbSimple_api.AbstractNoPropertiesInterface;
import tbSimple.tbSimple_impl.NoPropertiesInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton NoPropertiesInterfaceServiceProvider thread for the system.  This is a thread for
 * NoPropertiesInterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class NoPropertiesInterfaceServiceProvider extends HandlerThread implements INoPropertiesInterfaceServiceProvider
{
	private NoPropertiesInterfaceService m_Service;
	private static final String TAG = "NoPropertiesInterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static NoPropertiesInterfaceServiceProvider get()
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
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  AbstractNoPropertiesInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new NoPropertiesInterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new NoPropertiesInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final NoPropertiesInterfaceServiceProvider INSTANCE = createInstance();
	}

	private NoPropertiesInterfaceServiceProvider()
	{
		super("NoPropertiesInterfaceServiceProvider");
	}

	@NonNull
	private static NoPropertiesInterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		NoPropertiesInterfaceServiceProvider t = new NoPropertiesInterfaceServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
