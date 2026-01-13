//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimple_android_service;

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

import tbSimple.tbSimple_api.IEmptyInterfaceEventListener;
import tbSimple.tbSimple_android_service.IEmptyInterfaceServiceFactory;
import tbSimple.tbSimple_api.IEmptyInterface;
import tbSimple.tbSimple_api.AbstractEmptyInterface;
import tbSimple.tbSimple_android_messenger.EmptyInterfaceMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class EmptyInterfaceServiceAdapter extends Service
{
	private static final String TAG = "EmptyInterfaceServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	// Lifetime of mBackendService and its accessibility through mServiceFactory is controlled by the backend provide with setService function.
	// The ServiceAdapter is just a user of the backend. 
	// Use provided ServiceStarter classes and the start and stop functions for that.
	private static IEmptyInterface mBackendService;
	private static IEmptyInterfaceServiceFactory mServiceFactory;
	private static final Object sBackendMutex = new Object();

	public EmptyInterfaceServiceAdapter()
	{
	}

	public static IEmptyInterface setService(IEmptyInterfaceServiceFactory factory)
	{
		Log.i(TAG, "Setting factory: " + factory);
		if (mServiceFactory != factory)
		{
			mServiceFactory = factory;
		}
		synchronized (sBackendMutex)
		{
			if (mHandler != null && mBackendService != null)
			{
				// remove old event listener (backend is about to change)
				mBackendService.removeEventListener(mHandler);
			}
			if (mServiceFactory != null)
			{
				mBackendService = mServiceFactory.getServiceInstance();
				if (mHandler != null)
				{
					Log.i(TAG, "LIFECYCLE: setService(EmptyInterface) called. For handler " + mHandler);
					mBackendService.addEventListener(mHandler);
				}
			}
			else
			{
				mBackendService = null;
			}
		}
		return mBackendService;
	}


	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.i(TAG, "LIFECYCLE: onCreate(EmptyInterfaceService) called. context = " + this);
		synchronized (sBackendMutex) {
			if (mHandler != null && mBackendService != null)
			{
				// The handler (event listener) is about to change.
				mBackendService.removeEventListener(mHandler);
			}
			if (mHandler != null)
			{
				mHandler.removeCallbacksAndMessages(null);
			}
			mHandler = new IncomingHandler();
			mMessenger = new Messenger(mHandler);
			if (mBackendService != null)
			{
				Log.i(TAG, "LIFECYCLE: Add event listern to a backend called for handler " + mHandler);
				mBackendService.addEventListener(mHandler);
			}
		}

	}

	// execution of service will start on calling this method
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.i(TAG, "LIFECYCLE: EmptyInterfaceService::onStartCommand called. context = " + this +
				", startID=" + startId);

		return START_STICKY;
	}

	// execution of the service will stop on calling this method
	@Override
	public void onDestroy()
	{
		Log.i(TAG, "LIFECYCLE: onDestroy(EmptyInterfaceService) - proc = " + ", mMessenger = " + mMessenger);
		synchronized (sBackendMutex)
		{
			if (mHandler != null)
			{
				if (mBackendService != null)
				{
					mBackendService.removeEventListener(mHandler);
				}
				mHandler.removeCallbacksAndMessages(null);
				mHandler = null;
			}
			mMessenger = null;
		}

		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		Log.i(TAG, "LIFECYCLE: onBind(intent) - proc=" +  ", intent=" + intent);

		//Log.i(TAG, "binding attachId=" + attachId);
		return mMessenger.getBinder();
	}


	@Override
	public boolean onUnbind(Intent intent)
	{
		Log.i(TAG, "LIFECYCLE: onUnbind(intent) - proc=" + ", mMessenger=" + mMessenger
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
	class IncomingHandler extends Handler implements IEmptyInterfaceEventListener
	{
		private final ConcurrentHashMap<String, Messenger> mClients = new ConcurrentHashMap<>();

		IncomingHandler()
		{
			super(Looper.getMainLooper());
		}

		private void sendMessageToClients(Message msg)
		{
			for (Map.Entry<String, Messenger> client : mClients.entrySet())
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
			IEmptyInterface backend;
			synchronized (EmptyInterfaceServiceAdapter.sBackendMutex)
			{
				backend = EmptyInterfaceServiceAdapter.mBackendService;
			}
			if (backend == null || !backend._isReady())
			{
				if (EmptyInterfaceMessageType.fromInteger(msg.what) != EmptyInterfaceMessageType.REGISTER_CLIENT
					&& EmptyInterfaceMessageType.fromInteger(msg.what) != EmptyInterfaceMessageType.UNREGISTER_CLIENT)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: EmptyInterfaceMessageType" + EmptyInterfaceMessageType.fromInteger(msg.what) );
					return;
				}
			}
			switch (EmptyInterfaceMessageType.fromInteger(msg.what))
			{
				case REGISTER_CLIENT:
					addClientActivity(msg.replyTo, msg.getData().getString("connectionID", ""));
					sendInit();
					break;
				case UNREGISTER_CLIENT:
					removeClientActivity(msg.getData().getString("connectionID"));
					break;
				default:
					Log.e(TAG, "Receive Unsupported message: " + msg.what);
					super.handleMessage(msg);
					break;
				}
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
				mClients.put(connectionID, serviceReply);
				Log.i(TAG, "Register event listener with connectionID = " + connectionID);
			}
		}

		private void removeClientActivity(String connectionID)
		{
			mClients.remove(connectionID);
			Log.i(TAG, "UnRegister event listener with connectionID = " + connectionID);
		}

		private void sendInit()
		{
			Message msg = new Message();
			msg.what = EmptyInterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			IEmptyInterface backend;
			synchronized (EmptyInterfaceServiceAdapter.sBackendMutex)
			{
				backend = EmptyInterfaceServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				
				msg.setData(data);
				sendMessageToClients(msg);
			}
		}
		@Override
		public void on_readyStatusChanged(boolean isReady) {
			if (isReady){
				Log.i(TAG, "Backend ready ");
			}
			else {
				Log.i(TAG, "Backend not ready ");
			}
		}
	}
}
