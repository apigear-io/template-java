// Copyright Epic Games, Inc. All Rights Reserved.

package {{camel .Module.Name}}.unreal.{{camel .Module.Name}}service;

import {{camel .Module.Name}}.android.service.I{{Camel .Interface.Name }}ServiceFactory;
import {{dot .Module.Name}}.api.I{{Camel .Interface.Name }};
import {{dot .Module.Name}}.api.Abstract{{Camel .Interface.Name }};
import {{camel .Module.Name}}.unreal.{{camel .Module.Name}}service.Unreal{{Camel .Interface.Name}}Service
import android.util.Log;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Shared singleton Unreal{{Camel .Interface.Name}}ServiceFactory thread for the system.  This is a thread for
 * Unreal{{Camel .Interface.Name}}ServiceFactory connectivity.
 * Various connectivity manager objects can use this singleton as a common
 * resource for their handlers instead of creating separate threads of their own.
 *
 * @hide
 */
public class Unreal{{Camel .Interface.Name}}ServiceFactory extends HandlerThread implements I{{Camel .Interface.Name }}ServiceFactory
{
	private Unreal{{Camel .Interface.Name}}Service unrealService;

	// A class implementing the lazy holder idiom: the unique static instance
	// of ConnectivityThread is instantiated in a thread-safe way (guaranteed by
	// the language specs) the first time that Singleton is referenced in get()
	// or getInstanceLooper().

	public static Unreal{{Camel .Interface.Name}}ServiceFactory get()
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
			Log.w("UE", "LIFECYCLE: Unreal{{Camel .Interface.Name}}ServiceFactory::onDestroy() - stop instance thread, service = " + unrealService);
			Singleton.INSTANCE.quit();
		}
	}

	public synchronized  Abstract{{Camel .Interface.Name }} getServiceInstance()
	{
		if (unrealService == null)
		{
			unrealService = new Unreal{{Camel .Interface.Name}}Service();
			Log.d("UE", "LIFECYCLE: EngineFactory::GetInstance(with OBBFilename) - CREATED new Unreal{{Camel .Interface.Name}}Service");
		}

		return unrealService;
	}

	private static class Singleton
	{
		private static final Unreal{{Camel .Interface.Name}}ServiceFactory INSTANCE = createInstance();
	}

	private Unreal{{Camel .Interface.Name}}ServiceFactory()
	{
		super("EngineFactory");
	}

	@NonNull
	private static Unreal{{Camel .Interface.Name}}ServiceFactory createInstance()
	{
		Log.w("UE", "LIFECYCLE: EngineFactory::createInstance()");

		Unreal{{Camel .Interface.Name}}ServiceFactory t = new Unreal{{Camel .Interface.Name}}ServiceFactory();
		t.start();
		return t;
	}
}
