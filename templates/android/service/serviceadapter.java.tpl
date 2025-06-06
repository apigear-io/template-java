//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package {{camel .Module.Name}}.android.service

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

//import message type and parcelabe types
import {{dot .Module.Name}}.api.I{{Camel .Interface.Name }}EventListener;
import {{camel .Module.Name}}.android.service.I{{Camel .Interface.Name}}ServiceFactory;
import {{dot .Module.Name}}.api.I{{Camel .Interface.Name }};

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class {{Camel .Interface.Name }}ServiceAdapter extends Service
{
	private static final String TAG = "{{Camel .Interface.Name }}ServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private IncomingHandler mHandler;
	private static I{{Camel .Interface.Name }} mBackendService;
	private static I{{Camel .Interface.Name}}ServiceFactory mServiceFactory;

	//private final List<Message> mMessagesQueue = new ArrayList<>();

	public {{Camel .Interface.Name }}ServiceAdapter()
	{
	}

	public static void setServiceFactory(I{{Camel .Interface.Name}}ServiceFactory factory)
	{
		Log.i(TAG, "Setting factory: " + factory);
		if (mServiceFactory  != factory)
		{
			mServiceFactory = factory;
		}
		mBackendService = mServiceFactory.getServiceInstance();
	}


	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.i(TAG, "LIFECYCLE: onCreate({{Camel .Interface.Name }}Service) called. context = " + this);

		mHandler = new IncomingHandler(this);
		mMessenger = new Messenger(mHandler);
	}

	// execution of service will start on calling this method
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.i(TAG, "LIFECYCLE: {{Camel .Interface.Name }}Service::onStartCommand called. context = " + this +
				", startID=" + startId);

		return START_STICKY;
	}

	// execution of the service will stop on calling this method
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		Log.i(TAG, "LIFECYCLE: onDestroy({{Camel .Interface.Name }}Service) - proc = " + .Interface.Name(this)
				+ ", mMessenger = " + mMessenger
		);

		if (mBackendService != null)
		{
			Log.i(TAG, "LIFECYCLE: onDestroy({{Camel .Interface.Name }}Service) - proc = " + .Interface.Name(this) +
					", remove engine event callback!");

			mBackendService.removeEventListener(this);
			mBackendService = null;
		}
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		Log.i(TAG, "LIFECYCLE: onBind(intent) - proc=" + .Interface.Name(this)
				+ ", intent=" + intent);

		Log.i(TAG, "binding attachId=" + attachId);
		return mMessenger.getBinder();
	}


	@Override
	public boolean onUnbind(Intent intent)
	{
		Log.i(TAG, "LIFECYCLE: onUnbind(intent) - proc=" + .Interface.Name(this)
				+ ", mMessenger=" + mMessenger
				+ ", intent=" + intent);

		return super.onUnbind(intent);
	}

	private static String Name(Context context)
	{
		return context.getPackageName() + ",context=" + context;
	}
	//TODO Listener for handling messanger

	/**
	 * Handler of incoming messages from clients.
	 */
	class IncomingHandler extends Handler  //TODO   implements Listener
	{
		private final Service mApplicationContext;
		private final ConcurrentHashMap<String, Messenger> mActivityClients = new ConcurrentHashMap<>();

		IncomingHandler(Service context)
		{
			super(Looper.getMainLooper());
			mApplicationContext = context;
		}

		public void setUpService()
		{
			if (mBackendService == null)
			{
				if (!mServiceFactory)
				{
					Log.e(TAG, "setUpService: no service factory set, cannot create service backend");
				}
				mBackendService = mServiceFactory.getServiceInstance();
			}
			mBackendService.addEventListener(this);
		};

		private void sendMessageToActivityClients(Message msg)
		{
			for (Map.Entry<String, Messenger> client : mActivityClients.entrySet())
			{
				Messenger reply = client.getValue();
				if (reply != null)
				{
					try
					{
						reply.send(msg);
					} catch (RemoteException e)
					{
						Log.e(TAG, "Can't send reply " + e);
					}
				}
			}
		}

		@Override
		public void handleMessage(Message msg)
		{
			Log.i(TAG, "Handle msg " + msg);
			//TODO switch on messages
		}

		@Override
		protected void finalize() throws Throwable
		{
			super.finalize();
			Log.i(TAG, "LIFECYCLE: IncomingHandler(finalize)");
		}

		private void addClientActivity(Messenger serviceReply, String connectionID)
		{
			if (serviceReply != null)
			{
				mActivityClients.put(connectionID, serviceReply);
				Log.i(TAG, "Register event listener with connectionID = " + connectionID);
			}
		}

		private void removeClientActivity(String connectionID)
		{
			mActivityClients.remove(connectionID);
			Log.i(TAG, "UnRegister event listener with connectionID = " + connectionID);
		}
	}
}
