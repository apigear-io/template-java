//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame1.tbSame1_android_service;

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
import tbSame1.tbSame1_api.Enum1;
import tbSame1.tbSame1_android_messenger.Enum1Parcelable;

import tbSame1.tbSame1_api.ISameEnum1InterfaceEventListener;
import tbSame1.tbSame1_android_service.ISameEnum1InterfaceServiceFactory;
import tbSame1.tbSame1_api.ISameEnum1Interface;
import tbSame1.tbSame1_api.AbstractSameEnum1Interface;
import tbSame1.tbSame1_android_messenger.SameEnum1InterfaceMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class SameEnum1InterfaceServiceAdapter extends Service
{
	private static final String TAG = "SameEnum1InterfaceServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	// Lifetime of mBackendService and its accessibility through mServiceFactory is controlled by the backend provide with setService function.
	// The ServiceAdapter is just a user of the backend. 
	// Use provided ServiceStarter classes and the start and stop functions for that.
	private static ISameEnum1Interface mBackendService;
	private static ISameEnum1InterfaceServiceFactory mServiceFactory;
	private static final Object sBackendMutex = new Object();

	public SameEnum1InterfaceServiceAdapter()
	{
	}

	public static ISameEnum1Interface setService(ISameEnum1InterfaceServiceFactory factory)
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
					Log.i(TAG, "LIFECYCLE: setService(SameEnum1Interface) called. For handler " + mHandler);
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
		Log.i(TAG, "LIFECYCLE: onCreate(SameEnum1InterfaceService) called. context = " + this);
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
		Log.i(TAG, "LIFECYCLE: SameEnum1InterfaceService::onStartCommand called. context = " + this +
				", startID=" + startId);

		return START_STICKY;
	}

	// execution of the service will stop on calling this method
	@Override
	public void onDestroy()
	{
		Log.i(TAG, "LIFECYCLE: onDestroy(SameEnum1InterfaceService) - proc = " + ", mMessenger = " + mMessenger);
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
	class IncomingHandler extends Handler implements ISameEnum1InterfaceEventListener
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
			ISameEnum1Interface backend;
			synchronized (SameEnum1InterfaceServiceAdapter.sBackendMutex)
			{
				backend = SameEnum1InterfaceServiceAdapter.mBackendService;
			}
			if (backend == null || !backend._isReady())
			{
				if (SameEnum1InterfaceMessageType.fromInteger(msg.what) != SameEnum1InterfaceMessageType.REGISTER_CLIENT
					&& SameEnum1InterfaceMessageType.fromInteger(msg.what) != SameEnum1InterfaceMessageType.UNREGISTER_CLIENT)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: SameEnum1InterfaceMessageType" + SameEnum1InterfaceMessageType.fromInteger(msg.what) );
					return;
				}
			}
			switch (SameEnum1InterfaceMessageType.fromInteger(msg.what))
			{
				case REGISTER_CLIENT:
					addClientActivity(msg.replyTo, msg.getData().getString("connectionID", ""));
					sendInit();
					break;
				case UNREGISTER_CLIENT:
					removeClientActivity(msg.getData().getString("connectionID"));
					break;
					case PROP_Prop1:
					{
						Bundle data = msg.getData();
						data.setClassLoader(Enum1Parcelable.class.getClassLoader());
						
			        Enum1 prop1 = data.getParcelable("prop1", Enum1Parcelable.class).getEnum1();
						backend.setProp1(prop1);
						break;
					}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_Func1Req: {

					Bundle data = msg.getData();
					
        data.setClassLoader(Enum1Parcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        Enum1 param1 = data.getParcelable("param1", Enum1Parcelable.class).getEnum1();
					Enum1 result =  backend.func1(param1);

					Message respMsg = new Message();
					respMsg.what = SameEnum1InterfaceMessageType.RPC_Func1Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelable("result", new Enum1Parcelable(result));
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
			msg.what = SameEnum1InterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			ISameEnum1Interface backend;
			synchronized (SameEnum1InterfaceServiceAdapter.sBackendMutex)
			{
				backend = SameEnum1InterfaceServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				
				Enum1 prop1 = backend.getProp1();
				
		        data.putParcelable("prop1", new Enum1Parcelable(prop1));
				msg.setData(data);
				sendMessageToClients(msg);
			}
		}
		@Override
		public void onProp1Changed(Enum1 prop1){
			Log.i(TAG, "New value for Prop1 from backend" + prop1);

			Message msg = new Message();
			msg.what = SameEnum1InterfaceMessageType.SET_Prop1.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("prop1", new Enum1Parcelable(prop1));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSig1(Enum1 param1){
			Log.i(TAG, "New singal for Sig1 = "+ " " + param1);
			Message msg = new Message();
			msg.what = SameEnum1InterfaceMessageType.SIG_Sig1.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("param1", new Enum1Parcelable(param1));
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
