// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2_android_service;

import testbed2.testbed2_android_service.IManyParamInterfaceServiceProvider;
import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_api.AbstractManyParamInterface;
import testbed2.testbed2_impl.ManyParamInterfaceService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton ManyParamInterfaceServiceProvider thread for the system.  This is a thread for
 * ManyParamInterfaceServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class ManyParamInterfaceServiceProvider extends HandlerThread implements IManyParamInterfaceServiceProvider
{
	private ManyParamInterfaceService m_Service;
	private static final String TAG = "ManyParamInterfaceServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static ManyParamInterfaceServiceProvider get()
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

	public synchronized  AbstractManyParamInterface getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new ManyParamInterfaceService();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new ManyParamInterfaceService");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final ManyParamInterfaceServiceProvider INSTANCE = createInstance();
	}

	private ManyParamInterfaceServiceProvider()
	{
		super("ManyParamInterfaceServiceProvider");
	}

	@NonNull
	private static ManyParamInterfaceServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		ManyParamInterfaceServiceProvider t = new ManyParamInterfaceServiceProvider();
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
