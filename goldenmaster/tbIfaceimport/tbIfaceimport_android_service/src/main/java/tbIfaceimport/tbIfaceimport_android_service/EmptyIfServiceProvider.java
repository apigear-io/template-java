// Copyright Epic Games, Inc. All Rights Reserved.

package tbIfaceimport.tbIfaceimport_android_service;

import tbIfaceimport.tbIfaceimport_android_service.IEmptyIfServiceProvider;
import tbIfaceimport.tbIfaceimport_api.IEmptyIf;
import tbIfaceimport.tbIfaceimport_api.AbstractEmptyIf;
import tbIfaceimport.tbIfaceimport_impl.EmptyIfService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton EmptyIfServiceProvider thread for the system.  This is a thread for
 * EmptyIfServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class EmptyIfServiceProvider extends HandlerThread implements IEmptyIfServiceProvider
{
	private EmptyIfService m_Service;
	private static final String TAG = "EmptyIfServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static EmptyIfServiceProvider get()
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

	public synchronized  AbstractEmptyIf getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new EmptyIfService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new EmptyIfService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final EmptyIfServiceProvider INSTANCE = createInstance();
	}

	private EmptyIfServiceProvider()
	{
		super("EmptyIfServiceProvider");
	}

	@NonNull
	private static EmptyIfServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		EmptyIfServiceProvider t = new EmptyIfServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		m_Service = null;
	}
}
