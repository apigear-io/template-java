// Copyright Epic Games, Inc. All Rights Reserved.

package {{camel .Module.Name}}.{{camel .Module.Name}}_android_service;

import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.I{{Camel .Interface.Name }}ServiceFactory;
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.Abstract{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_impl.{{Camel .Interface.Name}}Service;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton {{Camel .Interface.Name}}ServiceFactory thread for the system.  This is a thread for
 * {{Camel .Interface.Name}}ServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class {{Camel .Interface.Name}}ServiceFactory extends HandlerThread implements I{{Camel .Interface.Name }}ServiceFactory
{
	private {{Camel .Interface.Name}}Service m_Service;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static {{Camel .Interface.Name}}ServiceFactory get()
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
			Log.w("UE", "LIFECYCLE: {{Camel .Interface.Name}}ServiceFactory::onDestroy() - stop instance thread, service = " + m_Service);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  Abstract{{Camel .Interface.Name }} getServiceInstance()
	{
		if (m_Service == null)
		{
			m_Service = new {{Camel .Interface.Name}}Service();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new {{Camel .Interface.Name}}Service");
		}

		return m_Service;
	}

	private static class Singleton
	{
		private static final {{Camel .Interface.Name}}ServiceFactory INSTANCE = createInstance();
	}

	private {{Camel .Interface.Name}}ServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static {{Camel .Interface.Name}}ServiceFactory createInstance()
	{
		Log.w("UE", "LIFECYCLE: EngineFactory::createInstance()");

		{{Camel .Interface.Name}}ServiceFactory t = new {{Camel .Interface.Name}}ServiceFactory();
		t.start();
		return t;
	}
}
