//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package testbed1.testbed1_android_service;

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
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_android_messenger.StructBoolParcelable;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_android_messenger.StructIntParcelable;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_android_messenger.StructFloatParcelable;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_android_messenger.StructStringParcelable;

import testbed1.testbed1_api.IStructArrayInterfaceEventListener;
import testbed1.testbed1_android_service.IStructArrayInterfaceServiceFactory;
import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_api.AbstractStructArrayInterface;
import testbed1.testbed1_android_messenger.StructArrayInterfaceMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class StructArrayInterfaceServiceAdapter extends Service
{
	private static final String TAG = "StructArrayInterfaceServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	private static IStructArrayInterface mBackendService;
	private static IStructArrayInterfaceServiceFactory mServiceFactory;

	//private final List<Message> mMessagesQueue = new ArrayList<>();

	public StructArrayInterfaceServiceAdapter()
	{
	}

	public static IStructArrayInterface setService(IStructArrayInterfaceServiceFactory factory)
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
		Log.i(TAG, "LIFECYCLE: onCreate(StructArrayInterfaceService) called. context = " + this);

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
		Log.i(TAG, "LIFECYCLE: StructArrayInterfaceService::onStartCommand called. context = " + this +
				", startID=" + startId);

		return START_STICKY;
	}

	// execution of the service will stop on calling this method
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		Log.i(TAG, "LIFECYCLE: onDestroy(StructArrayInterfaceService) - proc = " + ", mMessenger = " + mMessenger
		);

		if (mBackendService != null)
		{
			Log.i(TAG, "LIFECYCLE: onDestroy(StructArrayInterfaceService) - proc = " + ", remove engine event callback!");

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
	class IncomingHandler extends Handler implements IStructArrayInterfaceEventListener
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
				if (StructArrayInterfaceMessageType.fromInteger(msg.what) != StructArrayInterfaceMessageType.REGISTER_CLIENT
					&& StructArrayInterfaceMessageType.fromInteger(msg.what) != StructArrayInterfaceMessageType.UNREGISTER_CLIENT)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: StructArrayInterfaceMessageType" + StructArrayInterfaceMessageType.fromInteger(msg.what) );
					return;
				}
			}
			switch (StructArrayInterfaceMessageType.fromInteger(msg.what))
			{
				case REGISTER_CLIENT:
					addClientActivity(msg.replyTo, msg.getData().getString("connectionID", ""));
					sendInit();
					break;
				case UNREGISTER_CLIENT:
					removeClientActivity(msg.getData().getString("connectionID"));
					break;
					case PROP_PropBool:
					{
						Bundle data = msg.getData();
						data.setClassLoader(StructBoolParcelable.class.getClassLoader());
						
                    StructBool[] propBool =  StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("propBool", StructBoolParcelable.class));
						mBackendService.setPropBool(propBool);
						break;
					}
					case PROP_PropInt:
					{
						Bundle data = msg.getData();
						data.setClassLoader(StructIntParcelable.class.getClassLoader());
						
                    StructInt[] propInt =  StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("propInt", StructIntParcelable.class));
						mBackendService.setPropInt(propInt);
						break;
					}
					case PROP_PropFloat:
					{
						Bundle data = msg.getData();
						data.setClassLoader(StructFloatParcelable.class.getClassLoader());
						
                    StructFloat[] propFloat =  StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("propFloat", StructFloatParcelable.class));
						mBackendService.setPropFloat(propFloat);
						break;
					}
					case PROP_PropString:
					{
						Bundle data = msg.getData();
						data.setClassLoader(StructStringParcelable.class.getClassLoader());
						
                    StructString[] propString =  StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("propString", StructStringParcelable.class));
						mBackendService.setPropString(propString);
						break;
					}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_FuncBoolReq: {

					Bundle data = msg.getData();
					data.setClassLoader(StructBoolParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
                    StructBool[] paramBool =  StructBoolParcelable.unwrapArray((StructBoolParcelable[])data.getParcelableArray("paramBool", StructBoolParcelable.class));

					StructBool[] result =  mBackendService.funcBool(paramBool);

					Message respMsg = new Message();
					respMsg.what = StructArrayInterfaceMessageType.RPC_FuncBoolResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelableArray("result",StructBoolParcelable.wrapArray(result));
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
				case RPC_FuncIntReq: {

					Bundle data = msg.getData();
					data.setClassLoader(StructIntParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
                    StructInt[] paramInt =  StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("paramInt", StructIntParcelable.class));

					StructInt[] result =  mBackendService.funcInt(paramInt);

					Message respMsg = new Message();
					respMsg.what = StructArrayInterfaceMessageType.RPC_FuncIntResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelableArray("result",StructIntParcelable.wrapArray(result));
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
				case RPC_FuncFloatReq: {

					Bundle data = msg.getData();
					data.setClassLoader(StructFloatParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
                    StructFloat[] paramFloat =  StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("paramFloat", StructFloatParcelable.class));

					StructFloat[] result =  mBackendService.funcFloat(paramFloat);

					Message respMsg = new Message();
					respMsg.what = StructArrayInterfaceMessageType.RPC_FuncFloatResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelableArray("result",StructFloatParcelable.wrapArray(result));
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
				case RPC_FuncStringReq: {

					Bundle data = msg.getData();
					data.setClassLoader(StructStringParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
                    StructString[] paramString =  StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("paramString", StructStringParcelable.class));

					StructString[] result =  mBackendService.funcString(paramString);

					Message respMsg = new Message();
					respMsg.what = StructArrayInterfaceMessageType.RPC_FuncStringResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelableArray("result",StructStringParcelable.wrapArray(result));
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
			msg.what = StructArrayInterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			
			StructBool[] propBool = mBackendService.getPropBool();
			
		        data.putParcelableArray("propBool", StructBoolParcelable.wrapArray(propBool));
			StructInt[] propInt = mBackendService.getPropInt();
			
		        data.putParcelableArray("propInt", StructIntParcelable.wrapArray(propInt));
			StructFloat[] propFloat = mBackendService.getPropFloat();
			
		        data.putParcelableArray("propFloat", StructFloatParcelable.wrapArray(propFloat));
			StructString[] propString = mBackendService.getPropString();
			
		        data.putParcelableArray("propString", StructStringParcelable.wrapArray(propString));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropBoolChanged(StructBool[] propBool){
			Log.i(TAG, "New value for PropBool from backend" + propBool);

			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.SET_PropBool.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("propBool", StructBoolParcelable.wrapArray(propBool));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropIntChanged(StructInt[] propInt){
			Log.i(TAG, "New value for PropInt from backend" + propInt);

			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.SET_PropInt.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("propInt", StructIntParcelable.wrapArray(propInt));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropFloatChanged(StructFloat[] propFloat){
			Log.i(TAG, "New value for PropFloat from backend" + propFloat);

			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.SET_PropFloat.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("propFloat", StructFloatParcelable.wrapArray(propFloat));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropStringChanged(StructString[] propString){
			Log.i(TAG, "New value for PropString from backend" + propString);

			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.SET_PropString.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("propString", StructStringParcelable.wrapArray(propString));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigBool(StructBool[] paramBool){
			Log.i(TAG, "New singal for SigBool = "+ " " + paramBool);
			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.SIG_SigBool.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("paramBool", StructBoolParcelable.wrapArray(paramBool));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigInt(StructInt[] paramInt){
			Log.i(TAG, "New singal for SigInt = "+ " " + paramInt);
			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.SIG_SigInt.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("paramInt", StructIntParcelable.wrapArray(paramInt));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigFloat(StructFloat[] paramFloat){
			Log.i(TAG, "New singal for SigFloat = "+ " " + paramFloat);
			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.SIG_SigFloat.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("paramFloat", StructFloatParcelable.wrapArray(paramFloat));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigString(StructString[] paramString){
			Log.i(TAG, "New singal for SigString = "+ " " + paramString);
			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.SIG_SigString.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("paramString", StructStringParcelable.wrapArray(paramString));
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
