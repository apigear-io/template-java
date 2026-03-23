// Copyright Epic Games, Inc. All Rights Reserved.

package {{camel .Module.Name}}.{{camel .Module.Name}}_android_service;

import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.I{{Camel .Interface.Name }}ServiceProvider;
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.Abstract{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_impl.{{Camel .Interface.Name}}Service;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton {{Camel .Interface.Name}}ServiceProvider thread for the system.  This is a thread for
 * {{Camel .Interface.Name}}ServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class {{Camel .Interface.Name}}ServiceProvider extends HandlerThread implements I{{Camel .Interface.Name }}ServiceProvider
{
	private {{Camel .Interface.Name}}Service m_Service;
	private static final String TAG = "{{Camel .Interface.Name }}ServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static {{Camel .Interface.Name}}ServiceProvider get()
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

	public synchronized  Abstract{{Camel .Interface.Name }} getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new {{Camel .Interface.Name}}Service();
			Log.d(TAG, "LIFECYCLE: :GetInstance(with OBBFilename) - CREATED new {{Camel .Interface.Name}}Service");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final {{Camel .Interface.Name}}ServiceProvider INSTANCE = createInstance();
	}

	private {{Camel .Interface.Name}}ServiceProvider()
	{
		super("{{Camel .Interface.Name}}ServiceProvider");
	}

	@NonNull
	private static {{Camel .Interface.Name}}ServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: ServiceProvider::createInstance()");

		{{Camel .Interface.Name}}ServiceProvider t = new {{Camel .Interface.Name}}ServiceProvider();
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
