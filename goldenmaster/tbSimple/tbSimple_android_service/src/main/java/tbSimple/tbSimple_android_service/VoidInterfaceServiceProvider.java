// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimple_android_service;

import tbSimple.tbSimple_android_service.IVoidInterfaceServiceProvider;
import tbSimple.tbSimple_api.IVoidInterface;
import tbSimple.tbSimple_api.AbstractVoidInterface;
import tbSimple.tbSimple_impl.VoidInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton VoidInterfaceServiceProvider thread for the system.  This is a thread for
 * VoidInterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class VoidInterfaceServiceProvider extends HandlerThread implements IVoidInterfaceServiceProvider
{
	private VoidInterfaceService m_Service;
	private static final String TAG = "VoidInterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static VoidInterfaceServiceProvider get()
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

	public synchronized  AbstractVoidInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new VoidInterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new VoidInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final VoidInterfaceServiceProvider INSTANCE = createInstance();
	}

	private VoidInterfaceServiceProvider()
	{
		super("VoidInterfaceServiceProvider");
	}

	@NonNull
	private static VoidInterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		VoidInterfaceServiceProvider t = new VoidInterfaceServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
