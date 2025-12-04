//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package testbed2.testbed2_android_service;

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
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_android_messenger.NestedStruct1Parcelable;
import testbed2.testbed2_api.NestedStruct2;
import testbed2.testbed2_android_messenger.NestedStruct2Parcelable;

import testbed2.testbed2_api.INestedStruct2InterfaceEventListener;
import testbed2.testbed2_android_service.INestedStruct2InterfaceServiceFactory;
import testbed2.testbed2_api.INestedStruct2Interface;
import testbed2.testbed2_api.AbstractNestedStruct2Interface;
import testbed2.testbed2_android_messenger.NestedStruct2InterfaceMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class NestedStruct2InterfaceServiceAdapter extends Service
{
	private static final String TAG = "NestedStruct2InterfaceServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private static Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	private static INestedStruct2Interface mBackendService;
	private static INestedStruct2InterfaceServiceFactory mServiceFactory;
	private static final Object mutex = new Object();

	//private final List<Message> mMessagesQueue = new ArrayList<>();

	public NestedStruct2InterfaceServiceAdapter()
	{
	}

	public static INestedStruct2Interface setService(INestedStruct2InterfaceServiceFactory factory)
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
				Log.i(TAG, "LIFECYCLE: setService(NestedStruct2Interface) called. For handler " + mHandler);
				mBackendService.addEventListener(mHandler);
			}
		}
		return mBackendService;
	}


	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.i(TAG, "LIFECYCLE: onCreate(NestedStruct2InterfaceService) called. context = " + this);
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
		Log.i(TAG, "LIFECYCLE: NestedStruct2InterfaceService::onStartCommand called. context = " + this +
				", startID=" + startId);

		return START_STICKY;
	}

	// execution of the service will stop on calling this method
	@Override
	public void onDestroy()
	{
		Log.i(TAG, "LIFECYCLE: onDestroy(NestedStruct2InterfaceService) - proc = " + ", mMessenger = " + mMessenger);

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
	class IncomingHandler extends Handler implements INestedStruct2InterfaceEventListener
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
				if (NestedStruct2InterfaceMessageType.fromInteger(msg.what) != NestedStruct2InterfaceMessageType.REGISTER_CLIENT
					&& NestedStruct2InterfaceMessageType.fromInteger(msg.what) != NestedStruct2InterfaceMessageType.UNREGISTER_CLIENT)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: NestedStruct2InterfaceMessageType" + NestedStruct2InterfaceMessageType.fromInteger(msg.what) );
					return;
				}
			}
			switch (NestedStruct2InterfaceMessageType.fromInteger(msg.what))
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
						data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
						
			        NestedStruct1 prop1 = data.getParcelable("prop1", NestedStruct1Parcelable.class).getNestedStruct1();
						mBackendService.setProp1(prop1);
						break;
					}
					case PROP_Prop2:
					{
						Bundle data = msg.getData();
						data.setClassLoader(NestedStruct2Parcelable.class.getClassLoader());
						
			        NestedStruct2 prop2 = data.getParcelable("prop2", NestedStruct2Parcelable.class).getNestedStruct2();
						mBackendService.setProp2(prop2);
						break;
					}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_Func1Req: {

					Bundle data = msg.getData();
					
        data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        NestedStruct1 param1 = data.getParcelable("param1", NestedStruct1Parcelable.class).getNestedStruct1();

					NestedStruct1 result =  mBackendService.func1(param1);

					Message respMsg = new Message();
					respMsg.what = NestedStruct2InterfaceMessageType.RPC_Func1Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelable("result", new NestedStruct1Parcelable(result));
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
					
        data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        NestedStruct1 param1 = data.getParcelable("param1", NestedStruct1Parcelable.class).getNestedStruct1();
					
			        NestedStruct2 param2 = data.getParcelable("param2", NestedStruct2Parcelable.class).getNestedStruct2();

					NestedStruct1 result =  mBackendService.func2(param1, param2);

					Message respMsg = new Message();
					respMsg.what = NestedStruct2InterfaceMessageType.RPC_Func2Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelable("result", new NestedStruct1Parcelable(result));
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
			msg.what = NestedStruct2InterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			
			NestedStruct1 prop1 = mBackendService.getProp1();
			
		        data.putParcelable("prop1", new NestedStruct1Parcelable(prop1));
			NestedStruct2 prop2 = mBackendService.getProp2();
			
		        data.putParcelable("prop2", new NestedStruct2Parcelable(prop2));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onProp1Changed(NestedStruct1 prop1){
			Log.i(TAG, "New value for Prop1 from backend" + prop1);

			Message msg = new Message();
			msg.what = NestedStruct2InterfaceMessageType.SET_Prop1.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("prop1", new NestedStruct1Parcelable(prop1));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onProp2Changed(NestedStruct2 prop2){
			Log.i(TAG, "New value for Prop2 from backend" + prop2);

			Message msg = new Message();
			msg.what = NestedStruct2InterfaceMessageType.SET_Prop2.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("prop2", new NestedStruct2Parcelable(prop2));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSig1(NestedStruct1 param1){
			Log.i(TAG, "New singal for Sig1 = "+ " " + param1);
			Message msg = new Message();
			msg.what = NestedStruct2InterfaceMessageType.SIG_Sig1.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("param1", new NestedStruct1Parcelable(param1));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSig2(NestedStruct1 param1, NestedStruct2 param2){
			Log.i(TAG, "New singal for Sig2 = "+ " " + param1+ " " + param2);
			Message msg = new Message();
			msg.what = NestedStruct2InterfaceMessageType.SIG_Sig2.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("param1", new NestedStruct1Parcelable(param1));
			
		        data.putParcelable("param2", new NestedStruct2Parcelable(param2));
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
