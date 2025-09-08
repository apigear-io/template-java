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

import testbed2.testbed2_api.IManyParamInterfaceEventListener;
import testbed2.testbed2_android_service.IManyParamInterfaceServiceFactory;
import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_api.AbstractManyParamInterface;
import testbed2.testbed2_android_messenger.ManyParamInterfaceMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class ManyParamInterfaceServiceAdapter extends Service
{
	private static final String TAG = "ManyParamInterfaceServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	private static IManyParamInterface mBackendService;
	private static IManyParamInterfaceServiceFactory mServiceFactory;

	//private final List<Message> mMessagesQueue = new ArrayList<>();

	public ManyParamInterfaceServiceAdapter()
	{
	}

	public static IManyParamInterface setService(IManyParamInterfaceServiceFactory factory)
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
		Log.i(TAG, "LIFECYCLE: onCreate(ManyParamInterfaceService) called. context = " + this);

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
		Log.i(TAG, "LIFECYCLE: ManyParamInterfaceService::onStartCommand called. context = " + this +
				", startID=" + startId);

		return START_STICKY;
	}

	// execution of the service will stop on calling this method
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		Log.i(TAG, "LIFECYCLE: onDestroy(ManyParamInterfaceService) - proc = " + ", mMessenger = " + mMessenger
		);

		if (mBackendService != null)
		{
			Log.i(TAG, "LIFECYCLE: onDestroy(ManyParamInterfaceService) - proc = " + ", remove engine event callback!");

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
	class IncomingHandler extends Handler implements IManyParamInterfaceEventListener
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
				if (ManyParamInterfaceMessageType.fromInteger(msg.what) != ManyParamInterfaceMessageType.REGISTER_CLIENT
					&& ManyParamInterfaceMessageType.fromInteger(msg.what) != ManyParamInterfaceMessageType.UNREGISTER_CLIENT)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: ManyParamInterfaceMessageType" + ManyParamInterfaceMessageType.fromInteger(msg.what) );
					return;
				}
			}
			switch (ManyParamInterfaceMessageType.fromInteger(msg.what))
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
						
			        int prop1 = data.getInt("prop1", 0);
						mBackendService.setProp1(prop1);
						break;
					}
					case PROP_Prop2:
					{
						Bundle data = msg.getData();
						
			        int prop2 = data.getInt("prop2", 0);
						mBackendService.setProp2(prop2);
						break;
					}
					case PROP_Prop3:
					{
						Bundle data = msg.getData();
						
			        int prop3 = data.getInt("prop3", 0);
						mBackendService.setProp3(prop3);
						break;
					}
					case PROP_Prop4:
					{
						Bundle data = msg.getData();
						
			        int prop4 = data.getInt("prop4", 0);
						mBackendService.setProp4(prop4);
						break;
					}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_Func1Req: {

					Bundle data = msg.getData();
					int callId = data.getInt("callId");
					
			        int param1 = data.getInt("param1", 0);

					int result =  mBackendService.func1(param1);

					Message respMsg = new Message();
					respMsg.what = ManyParamInterfaceMessageType.RPC_Func1Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putInt("result", result);
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
					int callId = data.getInt("callId");
					
			        int param1 = data.getInt("param1", 0);
					
			        int param2 = data.getInt("param2", 0);

					int result =  mBackendService.func2(param1, param2);

					Message respMsg = new Message();
					respMsg.what = ManyParamInterfaceMessageType.RPC_Func2Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putInt("result", result);
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
				case RPC_Func3Req: {

					Bundle data = msg.getData();
					int callId = data.getInt("callId");
					
			        int param1 = data.getInt("param1", 0);
					
			        int param2 = data.getInt("param2", 0);
					
			        int param3 = data.getInt("param3", 0);

					int result =  mBackendService.func3(param1, param2, param3);

					Message respMsg = new Message();
					respMsg.what = ManyParamInterfaceMessageType.RPC_Func3Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putInt("result", result);
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
				case RPC_Func4Req: {

					Bundle data = msg.getData();
					int callId = data.getInt("callId");
					
			        int param1 = data.getInt("param1", 0);
					
			        int param2 = data.getInt("param2", 0);
					
			        int param3 = data.getInt("param3", 0);
					
			        int param4 = data.getInt("param4", 0);

					int result =  mBackendService.func4(param1, param2, param3, param4);

					Message respMsg = new Message();
					respMsg.what = ManyParamInterfaceMessageType.RPC_Func4Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putInt("result", result);
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
			msg.what = ManyParamInterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			
			int prop1 = mBackendService.getProp1();
			
		        data.putInt("prop1", prop1);
			int prop2 = mBackendService.getProp2();
			
		        data.putInt("prop2", prop2);
			int prop3 = mBackendService.getProp3();
			
		        data.putInt("prop3", prop3);
			int prop4 = mBackendService.getProp4();
			
		        data.putInt("prop4", prop4);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onProp1Changed(int prop1){
			Log.i(TAG, "New value for Prop1 from backend" + prop1);

			Message msg = new Message();
			msg.what = ManyParamInterfaceMessageType.SET_Prop1.getValue();
			Bundle data = new Bundle();
			
		        data.putInt("prop1", prop1);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onProp2Changed(int prop2){
			Log.i(TAG, "New value for Prop2 from backend" + prop2);

			Message msg = new Message();
			msg.what = ManyParamInterfaceMessageType.SET_Prop2.getValue();
			Bundle data = new Bundle();
			
		        data.putInt("prop2", prop2);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onProp3Changed(int prop3){
			Log.i(TAG, "New value for Prop3 from backend" + prop3);

			Message msg = new Message();
			msg.what = ManyParamInterfaceMessageType.SET_Prop3.getValue();
			Bundle data = new Bundle();
			
		        data.putInt("prop3", prop3);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onProp4Changed(int prop4){
			Log.i(TAG, "New value for Prop4 from backend" + prop4);

			Message msg = new Message();
			msg.what = ManyParamInterfaceMessageType.SET_Prop4.getValue();
			Bundle data = new Bundle();
			
		        data.putInt("prop4", prop4);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSig1(int param1){
			Log.i(TAG, "New singal for Sig1 = "+ " " + param1);
			Message msg = new Message();
			msg.what = ManyParamInterfaceMessageType.SIG_Sig1.getValue();
			Bundle data = new Bundle();
			
		        data.putInt("param1", param1);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSig2(int param1, int param2){
			Log.i(TAG, "New singal for Sig2 = "+ " " + param1+ " " + param2);
			Message msg = new Message();
			msg.what = ManyParamInterfaceMessageType.SIG_Sig2.getValue();
			Bundle data = new Bundle();
			
		        data.putInt("param1", param1);
			
		        data.putInt("param2", param2);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSig3(int param1, int param2, int param3){
			Log.i(TAG, "New singal for Sig3 = "+ " " + param1+ " " + param2+ " " + param3);
			Message msg = new Message();
			msg.what = ManyParamInterfaceMessageType.SIG_Sig3.getValue();
			Bundle data = new Bundle();
			
		        data.putInt("param1", param1);
			
		        data.putInt("param2", param2);
			
		        data.putInt("param3", param3);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSig4(int param1, int param2, int param3, int param4){
			Log.i(TAG, "New singal for Sig4 = "+ " " + param1+ " " + param2+ " " + param3+ " " + param4);
			Message msg = new Message();
			msg.what = ManyParamInterfaceMessageType.SIG_Sig4.getValue();
			Bundle data = new Bundle();
			
		        data.putInt("param1", param1);
			
		        data.putInt("param2", param2);
			
		        data.putInt("param3", param3);
			
		        data.putInt("param4", param4);
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
