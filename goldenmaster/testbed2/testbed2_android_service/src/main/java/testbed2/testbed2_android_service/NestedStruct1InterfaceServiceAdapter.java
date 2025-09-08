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
import testbed2.testbed2_api.Struct1;
import testbed2.testbed2_android_messenger.Struct1Parcelable;
import testbed2.testbed2_api.Struct2;
import testbed2.testbed2_android_messenger.Struct2Parcelable;
import testbed2.testbed2_api.Struct3;
import testbed2.testbed2_android_messenger.Struct3Parcelable;
import testbed2.testbed2_api.Struct4;
import testbed2.testbed2_android_messenger.Struct4Parcelable;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_android_messenger.NestedStruct1Parcelable;
import testbed2.testbed2_api.NestedStruct2;
import testbed2.testbed2_android_messenger.NestedStruct2Parcelable;
import testbed2.testbed2_api.NestedStruct3;
import testbed2.testbed2_android_messenger.NestedStruct3Parcelable;
import testbed2.testbed2_api.Enum1;
import testbed2.testbed2_android_messenger.Enum1Parcelable;
import testbed2.testbed2_api.Enum2;
import testbed2.testbed2_android_messenger.Enum2Parcelable;
import testbed2.testbed2_api.Enum3;
import testbed2.testbed2_android_messenger.Enum3Parcelable;

import testbed2.testbed2_api.INestedStruct1InterfaceEventListener;
import testbed2.testbed2_android_service.INestedStruct1InterfaceServiceFactory;
import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_api.AbstractNestedStruct1Interface;
import testbed2.testbed2_android_messenger.NestedStruct1InterfaceMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class NestedStruct1InterfaceServiceAdapter extends Service
{
	private static final String TAG = "NestedStruct1InterfaceServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	private static INestedStruct1Interface mBackendService;
	private static INestedStruct1InterfaceServiceFactory mServiceFactory;

	//private final List<Message> mMessagesQueue = new ArrayList<>();

	public NestedStruct1InterfaceServiceAdapter()
	{
	}

	public static INestedStruct1Interface setService(INestedStruct1InterfaceServiceFactory factory)
	{
		Log.i(TAG, "Setting factory: " + factory);
		if (mServiceFactory  != factory)
		{
			mServiceFactory = factory;
		}
		mBackendService = mServiceFactory.getServiceInstance();
		if (mHandler != null)
		{
			mBackendService.addEventListener(mHandler);
		}
		return mBackendService;
	}


	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.i(TAG, "LIFECYCLE: onCreate(NestedStruct1InterfaceService) called. context = " + this);

		mHandler = new IncomingHandler(this);
		mMessenger = new Messenger(mHandler);
		if (mBackendService != null)
		{
			mBackendService.addEventListener(mHandler);
		}
	}

	// execution of service will start on calling this method
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.i(TAG, "LIFECYCLE: NestedStruct1InterfaceService::onStartCommand called. context = " + this +
				", startID=" + startId);

		return START_STICKY;
	}

	// execution of the service will stop on calling this method
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		Log.i(TAG, "LIFECYCLE: onDestroy(NestedStruct1InterfaceService) - proc = " + ", mMessenger = " + mMessenger
		);

		if (mBackendService != null)
		{
			Log.i(TAG, "LIFECYCLE: onDestroy(NestedStruct1InterfaceService) - proc = " + ", remove engine event callback!");

			mBackendService.removeEventListener(mHandler);
			mBackendService = null;
		}
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
	class IncomingHandler extends Handler implements INestedStruct1InterfaceEventListener
	{
		private final Service mApplicationContext;
		private final ConcurrentHashMap<String, Messenger> mClients = new ConcurrentHashMap<>();

		IncomingHandler(Service context)
		{
			super(Looper.getMainLooper());
			mApplicationContext = context;
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
				if (NestedStruct1InterfaceMessageType.fromInteger(msg.what) != NestedStruct1InterfaceMessageType.REGISTER_CLIENT
					&& NestedStruct1InterfaceMessageType.fromInteger(msg.what) != NestedStruct1InterfaceMessageType.UNREGISTER_CLIENT)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: NestedStruct1InterfaceMessageType" + NestedStruct1InterfaceMessageType.fromInteger(msg.what) );
					return;
				}
			}
			switch (NestedStruct1InterfaceMessageType.fromInteger(msg.what))
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
					respMsg.what = NestedStruct1InterfaceMessageType.RPC_Func1Resp.getValue();
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
			msg.what = NestedStruct1InterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			
			NestedStruct1 prop1 = mBackendService.getProp1();
			
		        data.putParcelable("prop1", new NestedStruct1Parcelable(prop1));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onProp1Changed(NestedStruct1 prop1){
			Log.i(TAG, "New value for Prop1 from backend" + prop1);

			Message msg = new Message();
			msg.what = NestedStruct1InterfaceMessageType.SET_Prop1.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("prop1", new NestedStruct1Parcelable(prop1));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSig1(NestedStruct1 param1){
			Log.i(TAG, "New singal for Sig1 = "+ " " + param1);
			Message msg = new Message();
			msg.what = NestedStruct1InterfaceMessageType.SIG_Sig1.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("param1", new NestedStruct1Parcelable(param1));
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
