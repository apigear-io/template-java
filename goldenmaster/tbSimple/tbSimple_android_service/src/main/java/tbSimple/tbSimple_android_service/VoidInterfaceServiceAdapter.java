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

import tbSimple.tbSimple_api.IVoidInterfaceEventListener;
import tbSimple.tbSimple_android_service.IVoidInterfaceServiceFactory;
import tbSimple.tbSimple_api.IVoidInterface;
import tbSimple.tbSimple_api.AbstractVoidInterface;
import tbSimple.tbSimple_android_messenger.VoidInterfaceMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class VoidInterfaceServiceAdapter extends Service
{
	private static final String TAG = "VoidInterfaceServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private static Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	private static IVoidInterface mBackendService;
	private static IVoidInterfaceServiceFactory mServiceFactory;
	private static final Object mutex = new Object();

	//private final List<Message> mMessagesQueue = new ArrayList<>();

	public VoidInterfaceServiceAdapter()
	{
	}

	public static IVoidInterface setService(IVoidInterfaceServiceFactory factory)
	{
		Log.i(TAG, "Setting factory: " + factory);
		if (mServiceFactory  != factory)
		{
			mServiceFactory = factory;
		}
		synchronized (mutex)
		{
			if (mHandler != null && mBackendService != null)
			{
				// remove old event listener (backend is about to change)
				mBackendService.removeEventListener(mHandler);
			}
			mBackendService = mServiceFactory.getServiceInstance();
			if (mHandler != null)
			{
				Log.i(TAG, "LIFECYCLE: setService(VoidInterface) called. For handler " + mHandler);
				mBackendService.addEventListener(mHandler);
			}
		}
		return mBackendService;
	}


	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.i(TAG, "LIFECYCLE: onCreate(VoidInterfaceService) called. context = " + this);
		synchronized (mutex) {
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
		Log.i(TAG, "LIFECYCLE: VoidInterfaceService::onStartCommand called. context = " + this +
				", startID=" + startId);

		return START_STICKY;
	}

	// execution of the service will stop on calling this method
	@Override
	public void onDestroy()
	{
		Log.i(TAG, "LIFECYCLE: onDestroy(VoidInterfaceService) - proc = " + ", mMessenger = " + mMessenger);

		if (mHandler != null)
		{
			if (mBackendService != null)
			{
				mBackendService.removeEventListener(mHandler);
			}
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
		mBackendService = null;
		mMessenger = null;

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
	class IncomingHandler extends Handler implements IVoidInterfaceEventListener
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
			if (mBackendService == null || !mBackendService._isReady())
			{
				if (VoidInterfaceMessageType.fromInteger(msg.what) != VoidInterfaceMessageType.REGISTER_CLIENT
					&& VoidInterfaceMessageType.fromInteger(msg.what) != VoidInterfaceMessageType.UNREGISTER_CLIENT)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: VoidInterfaceMessageType" + VoidInterfaceMessageType.fromInteger(msg.what) );
					return;
				}
			}
			switch (VoidInterfaceMessageType.fromInteger(msg.what))
			{
				case REGISTER_CLIENT:
					addClientActivity(msg.replyTo, msg.getData().getString("connectionID", ""));
					sendInit();
					break;
				case UNREGISTER_CLIENT:
					removeClientActivity(msg.getData().getString("connectionID"));
					break;
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_FuncVoidReq: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");

					 mBackendService.funcVoid();

					Message respMsg = new Message();
					respMsg.what = VoidInterfaceMessageType.RPC_FuncVoidResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					respMsg.setData(resp_data);

					try {
						msg.replyTo.send(respMsg);
					} catch (RemoteException e) {
						throw new RuntimeException(e);
					}
					break;

				}
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
			msg.what = VoidInterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigVoid(){
			Log.i(TAG, "New singal for SigVoid = ");
			Message msg = new Message();
			msg.what = VoidInterfaceMessageType.SIG_SigVoid.getValue();
			Bundle data = new Bundle();
			msg.setData(data);
			sendMessageToClients(msg);
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
