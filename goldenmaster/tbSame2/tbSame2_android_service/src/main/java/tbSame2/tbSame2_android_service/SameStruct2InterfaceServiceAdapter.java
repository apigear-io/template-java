//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package tbSame2.tbSame2_android_service;

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
import tbSame2.tbSame2_api.Struct1;
import tbSame2.tbSame2_android_messenger.Struct1Parcelable;
import tbSame2.tbSame2_api.Struct2;
import tbSame2.tbSame2_android_messenger.Struct2Parcelable;

import tbSame2.tbSame2_api.ISameStruct2InterfaceEventListener;
import tbSame2.tbSame2_android_service.ISameStruct2InterfaceServiceProvider;
import tbSame2.tbSame2_api.ISameStruct2Interface;
import tbSame2.tbSame2_api.AbstractSameStruct2Interface;
import tbSame2.tbSame2_android_messenger.SameStruct2InterfaceMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class SameStruct2InterfaceServiceAdapter extends Service
{
	private static final String TAG = "SameStruct2InterfaceServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	// Lifetime of mBackendService and its accessibility through mServiceProvider is controlled by the backend provide with setService function.
	// The ServiceAdapter is just a user of the backend. 
	// Use provided ServiceStarter classes and the start and stop functions for that.
	private static ISameStruct2Interface mBackendService;
	private static ISameStruct2InterfaceServiceProvider mServiceProvider;
	private static final Object sBackendMutex = new Object();

	public SameStruct2InterfaceServiceAdapter()
	{
	}

	public static ISameStruct2Interface setService(ISameStruct2InterfaceServiceProvider serviceProvider)
	{
		Log.i(TAG, "Setting serviceProvider: " + serviceProvider);
		if (mServiceProvider != serviceProvider)
		{
			mServiceProvider = serviceProvider;
		}
		synchronized (sBackendMutex)
		{
			if (mHandler != null && mBackendService != null)
			{
				// remove old event listener (backend is about to change)
				mBackendService.removeEventListener(mHandler);
			}
			if (mServiceProvider != null)
			{
				mBackendService = mServiceProvider.getServiceInstance();
				if (mHandler != null)
				{
					Log.i(TAG, "LIFECYCLE: setService(SameStruct2Interface) called. For handler " + mHandler);
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
		Log.i(TAG, "LIFECYCLE: onCreate(SameStruct2InterfaceService) called. context = " + this);
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
		Log.i(TAG, "LIFECYCLE: SameStruct2InterfaceService::onStartCommand called. context = " + this +
				", startID=" + startId);
		// START_STICKY means the android may or may not at some point restart the service.
		// It will also not give any feedback if it did, hence we don't want the android to try.
		// Check the LifecycleController classes for the notification about service lifecycle events.
		// Use it to start it again.
		return START_NOT_STICKY;
	}

	// execution of the service will stop on calling this method
	@Override
	public void onDestroy()
	{
		Log.i(TAG, "LIFECYCLE: onDestroy(SameStruct2InterfaceService) - proc = " + ", mMessenger = " + mMessenger);
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
	class IncomingHandler extends Handler implements ISameStruct2InterfaceEventListener
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
			ISameStruct2Interface backend;
			synchronized (SameStruct2InterfaceServiceAdapter.sBackendMutex)
			{
				backend = SameStruct2InterfaceServiceAdapter.mBackendService;
			}
			if (backend == null || !backend._isReady())
			{
				if (SameStruct2InterfaceMessageType.fromInteger(msg.what) != SameStruct2InterfaceMessageType.REGISTER_CLIENT
					&& SameStruct2InterfaceMessageType.fromInteger(msg.what) != SameStruct2InterfaceMessageType.UNREGISTER_CLIENT)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: SameStruct2InterfaceMessageType" + SameStruct2InterfaceMessageType.fromInteger(msg.what) );
					return;
				}
			}
			switch (SameStruct2InterfaceMessageType.fromInteger(msg.what))
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
						data.setClassLoader(Struct2Parcelable.class.getClassLoader());
						
			        Struct2 prop1 = data.getParcelable("prop1", Struct2Parcelable.class).getStruct2();
						backend.setProp1(prop1);
						break;
					}
					case PROP_Prop2:
					{
						Bundle data = msg.getData();
						data.setClassLoader(Struct2Parcelable.class.getClassLoader());
						
			        Struct2 prop2 = data.getParcelable("prop2", Struct2Parcelable.class).getStruct2();
						backend.setProp2(prop2);
						break;
					}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_Func1Req: {

					Bundle data = msg.getData();
					
        data.setClassLoader(Struct1Parcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        Struct1 param1 = data.getParcelable("param1", Struct1Parcelable.class).getStruct1();
					Struct1 result =  backend.func1(param1);

					Message respMsg = new Message();
					respMsg.what = SameStruct2InterfaceMessageType.RPC_Func1Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelable("result", new Struct1Parcelable(result));
					respMsg.setData(resp_data);

					try {
						msg.replyTo.send(respMsg);
					} catch (RemoteException e) {
						throw new RuntimeException(e);
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_Func2Req: {

					Bundle data = msg.getData();
					
    // all structs, even from other modules, are known at compile time (see gradle files) and share the same PathClassLoader,
    // therefore, any class loader provide access to the same PathClassLoader.
        data.setClassLoader(Struct1Parcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        Struct1 param1 = data.getParcelable("param1", Struct1Parcelable.class).getStruct1();
					
			        Struct2 param2 = data.getParcelable("param2", Struct2Parcelable.class).getStruct2();
					Struct1 result =  backend.func2(param1, param2);

					Message respMsg = new Message();
					respMsg.what = SameStruct2InterfaceMessageType.RPC_Func2Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelable("result", new Struct1Parcelable(result));
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
			msg.what = SameStruct2InterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			ISameStruct2Interface backend;
			synchronized (SameStruct2InterfaceServiceAdapter.sBackendMutex)
			{
				backend = SameStruct2InterfaceServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				
				Struct2 prop1 = backend.getProp1();
				
		        data.putParcelable("prop1", new Struct2Parcelable(prop1));
				Struct2 prop2 = backend.getProp2();
				
		        data.putParcelable("prop2", new Struct2Parcelable(prop2));
				msg.setData(data);
				sendMessageToClients(msg);
			}
		}
		@Override
		public void onProp1Changed(Struct2 prop1){
			Log.i(TAG, "New value for Prop1 from backend" + prop1);

			Message msg = new Message();
			msg.what = SameStruct2InterfaceMessageType.SET_Prop1.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("prop1", new Struct2Parcelable(prop1));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onProp2Changed(Struct2 prop2){
			Log.i(TAG, "New value for Prop2 from backend" + prop2);

			Message msg = new Message();
			msg.what = SameStruct2InterfaceMessageType.SET_Prop2.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("prop2", new Struct2Parcelable(prop2));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSig1(Struct1 param1){
			Log.i(TAG, "New singal for Sig1 = "+ " " + param1);
			Message msg = new Message();
			msg.what = SameStruct2InterfaceMessageType.SIG_Sig1.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("param1", new Struct1Parcelable(param1));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSig2(Struct1 param1, Struct2 param2){
			Log.i(TAG, "New singal for Sig2 = "+ " " + param1+ " " + param2);
			Message msg = new Message();
			msg.what = SameStruct2InterfaceMessageType.SIG_Sig2.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("param1", new Struct1Parcelable(param1));
			
		        data.putParcelable("param2", new Struct2Parcelable(param2));
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
