// Copyright Epic Games, Inc. All Rights Reserved.

package {{camel .Module.Name}}.{{camel .Module.Name}}jniservice;

import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.I{{Camel .Interface.Name }}ServiceProvider;
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.Abstract{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}jniservice.{{Camel .Interface.Name}}JniService;
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton {{Camel .Interface.Name}}JniServiceProvider thread for the system.  This is a thread for
 * {{Camel .Interface.Name}}JniServiceProvider connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class {{Camel .Interface.Name}}JniServiceProvider extends HandlerThread implements I{{Camel .Interface.Name }}ServiceProvider
{
	private {{Camel .Interface.Name}}JniService jniService;
	private static final String TAG = "{{Camel .Interface.Name }}JniServiceProvider";

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static {{Camel .Interface.Name}}JniServiceProvider get()
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
			Log.i(TAG, "LIFECYCLE: onDestroy() - stop instance thread, service = " + jniService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  Abstract{{Camel .Interface.Name }} getServiceInstance()
	{
		if (jniService == null)
		{
			jniService = new {{Camel .Interface.Name}}JniService();
			Log.d(TAG, "LIFECYCLE: getServiceInstance - CREATED new {{Camel .Interface.Name}}JniService");
		}

		return jniService;
	}

	private static class Singleton
	{
		private static final {{Camel .Interface.Name}}JniServiceProvider INSTANCE = createInstance();
	}

	private {{Camel .Interface.Name}}JniServiceProvider()
	{
		super("{{Camel .Interface.Name}}JniServiceProvider");
	}

	@NonNull
	private static {{Camel .Interface.Name}}JniServiceProvider createInstance()
	{
		Log.i(TAG, "LIFECYCLE: createInstance()");

		{{Camel .Interface.Name}}JniServiceProvider t = new {{Camel .Interface.Name}}JniServiceProvider();
		t.start();
		return t;
	}

	public synchronized void clear()
	{
		jniService = null;
	}
}
