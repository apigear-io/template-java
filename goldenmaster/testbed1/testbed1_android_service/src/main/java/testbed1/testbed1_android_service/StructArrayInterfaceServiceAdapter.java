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
import testbed1.testbed1_api.Enum0;
import testbed1.testbed1_android_messenger.Enum0Parcelable;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_android_messenger.StructBoolParcelable;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_android_messenger.StructFloatParcelable;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_android_messenger.StructIntParcelable;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_android_messenger.StructStringParcelable;

import testbed1.testbed1_api.IStructArrayInterfaceEventListener;
import testbed1.testbed1_android_service.IStructArrayInterfaceServiceProvider;
import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_api.AbstractStructArrayInterface;
import testbed1.testbed1_api.RemoteOperationException;
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
	// Lifetime of mBackendService and its accessibility through mServiceProvider is controlled by the backend provide with setService function.
	// The ServiceAdapter is just a user of the backend. 
	// Use provided ServiceStarter classes and the start and stop functions for that.
	private static IStructArrayInterface mBackendService;
	private static IStructArrayInterfaceServiceProvider mServiceProvider;
	private static final Object sBackendMutex = new Object();

	public StructArrayInterfaceServiceAdapter()
	{
	}

	public static IStructArrayInterface setService(IStructArrayInterfaceServiceProvider serviceProvider)
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
					Log.i(TAG, "LIFECYCLE: setService(StructArrayInterface) called. For handler " + mHandler);
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
		Log.i(TAG, "LIFECYCLE: onCreate(StructArrayInterfaceService) called. context = " + this);
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
		Log.i(TAG, "LIFECYCLE: StructArrayInterfaceService::onStartCommand called. context = " + this +
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
		Log.i(TAG, "LIFECYCLE: onDestroy(StructArrayInterfaceService) - proc = " + ", mMessenger = " + mMessenger);
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
	class IncomingHandler extends Handler implements IStructArrayInterfaceEventListener
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
			IStructArrayInterface backend;
			synchronized (StructArrayInterfaceServiceAdapter.sBackendMutex)
			{
				backend = StructArrayInterfaceServiceAdapter.mBackendService;
			}
			if (backend == null || !backend._isReady())
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
						backend.setPropBool(propBool);
						break;
					}
					case PROP_PropInt:
					{
						Bundle data = msg.getData();
						data.setClassLoader(StructIntParcelable.class.getClassLoader());
						
                    StructInt[] propInt =  StructIntParcelable.unwrapArray((StructIntParcelable[])data.getParcelableArray("propInt", StructIntParcelable.class));
						backend.setPropInt(propInt);
						break;
					}
					case PROP_PropFloat:
					{
						Bundle data = msg.getData();
						data.setClassLoader(StructFloatParcelable.class.getClassLoader());
						
                    StructFloat[] propFloat =  StructFloatParcelable.unwrapArray((StructFloatParcelable[])data.getParcelableArray("propFloat", StructFloatParcelable.class));
						backend.setPropFloat(propFloat);
						break;
					}
					case PROP_PropString:
					{
						Bundle data = msg.getData();
						data.setClassLoader(StructStringParcelable.class.getClassLoader());
						
                    StructString[] propString =  StructStringParcelable.unwrapArray((StructStringParcelable[])data.getParcelableArray("propString", StructStringParcelable.class));
						backend.setPropString(propString);
						break;
					}
					case PROP_PropEnum:
					{
						Bundle data = msg.getData();
						data.setClassLoader(Enum0Parcelable.class.getClassLoader());
						
                    Enum0[] propEnum =  Enum0Parcelable.unwrapArray((Enum0Parcelable[])data.getParcelableArray("propEnum", Enum0Parcelable.class));
						backend.setPropEnum(propEnum);
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
					Message respMsg = new Message();
					respMsg.what = StructArrayInterfaceMessageType.RPC_FuncBoolResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						StructBool[] result =  backend.funcBool(paramBool);
						
		        resp_data.putParcelableArray("result",StructBoolParcelable.wrapArray(result));
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "funcBool failed: " + errorMessage);
						Log.d(TAG, "funcBool exception details", e);
						resp_data.putBoolean("error", true);
						resp_data.putString("errorMessage", errorMessage);
						int errorCode;
						if (e instanceof RemoteOperationException) {
							errorCode = ((RemoteOperationException) e).getErrorCode();
						} else if (e instanceof IllegalArgumentException) {
							errorCode = RemoteOperationException.ERROR_INVALID_ARGUMENT;
						} else if (e instanceof UnsupportedOperationException) {
							errorCode = RemoteOperationException.ERROR_NOT_IMPLEMENTED;
						} else {
							errorCode = RemoteOperationException.ERROR_INTERNAL;
						}
						resp_data.putInt("errorCode", errorCode);
					}

					respMsg.setData(resp_data);

					if (msg.replyTo != null) {
						try {
							msg.replyTo.send(respMsg);
						} catch (RemoteException e) {
							Log.e(TAG, "failed to send funcBool response: " + e);
						}
					} else {
						Log.w(TAG, "funcBool: replyTo is null, cannot send response");
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
					Message respMsg = new Message();
					respMsg.what = StructArrayInterfaceMessageType.RPC_FuncIntResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						StructInt[] result =  backend.funcInt(paramInt);
						
		        resp_data.putParcelableArray("result",StructIntParcelable.wrapArray(result));
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "funcInt failed: " + errorMessage);
						Log.d(TAG, "funcInt exception details", e);
						resp_data.putBoolean("error", true);
						resp_data.putString("errorMessage", errorMessage);
						int errorCode;
						if (e instanceof RemoteOperationException) {
							errorCode = ((RemoteOperationException) e).getErrorCode();
						} else if (e instanceof IllegalArgumentException) {
							errorCode = RemoteOperationException.ERROR_INVALID_ARGUMENT;
						} else if (e instanceof UnsupportedOperationException) {
							errorCode = RemoteOperationException.ERROR_NOT_IMPLEMENTED;
						} else {
							errorCode = RemoteOperationException.ERROR_INTERNAL;
						}
						resp_data.putInt("errorCode", errorCode);
					}

					respMsg.setData(resp_data);

					if (msg.replyTo != null) {
						try {
							msg.replyTo.send(respMsg);
						} catch (RemoteException e) {
							Log.e(TAG, "failed to send funcInt response: " + e);
						}
					} else {
						Log.w(TAG, "funcInt: replyTo is null, cannot send response");
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
					Message respMsg = new Message();
					respMsg.what = StructArrayInterfaceMessageType.RPC_FuncFloatResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						StructFloat[] result =  backend.funcFloat(paramFloat);
						
		        resp_data.putParcelableArray("result",StructFloatParcelable.wrapArray(result));
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "funcFloat failed: " + errorMessage);
						Log.d(TAG, "funcFloat exception details", e);
						resp_data.putBoolean("error", true);
						resp_data.putString("errorMessage", errorMessage);
						int errorCode;
						if (e instanceof RemoteOperationException) {
							errorCode = ((RemoteOperationException) e).getErrorCode();
						} else if (e instanceof IllegalArgumentException) {
							errorCode = RemoteOperationException.ERROR_INVALID_ARGUMENT;
						} else if (e instanceof UnsupportedOperationException) {
							errorCode = RemoteOperationException.ERROR_NOT_IMPLEMENTED;
						} else {
							errorCode = RemoteOperationException.ERROR_INTERNAL;
						}
						resp_data.putInt("errorCode", errorCode);
					}

					respMsg.setData(resp_data);

					if (msg.replyTo != null) {
						try {
							msg.replyTo.send(respMsg);
						} catch (RemoteException e) {
							Log.e(TAG, "failed to send funcFloat response: " + e);
						}
					} else {
						Log.w(TAG, "funcFloat: replyTo is null, cannot send response");
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
					Message respMsg = new Message();
					respMsg.what = StructArrayInterfaceMessageType.RPC_FuncStringResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						StructString[] result =  backend.funcString(paramString);
						
		        resp_data.putParcelableArray("result",StructStringParcelable.wrapArray(result));
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "funcString failed: " + errorMessage);
						Log.d(TAG, "funcString exception details", e);
						resp_data.putBoolean("error", true);
						resp_data.putString("errorMessage", errorMessage);
						int errorCode;
						if (e instanceof RemoteOperationException) {
							errorCode = ((RemoteOperationException) e).getErrorCode();
						} else if (e instanceof IllegalArgumentException) {
							errorCode = RemoteOperationException.ERROR_INVALID_ARGUMENT;
						} else if (e instanceof UnsupportedOperationException) {
							errorCode = RemoteOperationException.ERROR_NOT_IMPLEMENTED;
						} else {
							errorCode = RemoteOperationException.ERROR_INTERNAL;
						}
						resp_data.putInt("errorCode", errorCode);
					}

					respMsg.setData(resp_data);

					if (msg.replyTo != null) {
						try {
							msg.replyTo.send(respMsg);
						} catch (RemoteException e) {
							Log.e(TAG, "failed to send funcString response: " + e);
						}
					} else {
						Log.w(TAG, "funcString: replyTo is null, cannot send response");
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_FuncEnumReq: {

					Bundle data = msg.getData();
					
        data.setClassLoader(Enum0Parcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
                    Enum0[] paramEnum =  Enum0Parcelable.unwrapArray((Enum0Parcelable[])data.getParcelableArray("paramEnum", Enum0Parcelable.class));
					Message respMsg = new Message();
					respMsg.what = StructArrayInterfaceMessageType.RPC_FuncEnumResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						Enum0[] result =  backend.funcEnum(paramEnum);
						
		        resp_data.putParcelableArray("result",Enum0Parcelable.wrapArray(result));
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "funcEnum failed: " + errorMessage);
						Log.d(TAG, "funcEnum exception details", e);
						resp_data.putBoolean("error", true);
						resp_data.putString("errorMessage", errorMessage);
						int errorCode;
						if (e instanceof RemoteOperationException) {
							errorCode = ((RemoteOperationException) e).getErrorCode();
						} else if (e instanceof IllegalArgumentException) {
							errorCode = RemoteOperationException.ERROR_INVALID_ARGUMENT;
						} else if (e instanceof UnsupportedOperationException) {
							errorCode = RemoteOperationException.ERROR_NOT_IMPLEMENTED;
						} else {
							errorCode = RemoteOperationException.ERROR_INTERNAL;
						}
						resp_data.putInt("errorCode", errorCode);
					}

					respMsg.setData(resp_data);

					if (msg.replyTo != null) {
						try {
							msg.replyTo.send(respMsg);
						} catch (RemoteException e) {
							Log.e(TAG, "failed to send funcEnum response: " + e);
						}
					} else {
						Log.w(TAG, "funcEnum: replyTo is null, cannot send response");
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
			msg.what = StructArrayInterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			IStructArrayInterface backend;
			synchronized (StructArrayInterfaceServiceAdapter.sBackendMutex)
			{
				backend = StructArrayInterfaceServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				
				StructBool[] propBool = backend.getPropBool();
				
		        data.putParcelableArray("propBool", StructBoolParcelable.wrapArray(propBool));
				StructInt[] propInt = backend.getPropInt();
				
		        data.putParcelableArray("propInt", StructIntParcelable.wrapArray(propInt));
				StructFloat[] propFloat = backend.getPropFloat();
				
		        data.putParcelableArray("propFloat", StructFloatParcelable.wrapArray(propFloat));
				StructString[] propString = backend.getPropString();
				
		        data.putParcelableArray("propString", StructStringParcelable.wrapArray(propString));
				Enum0[] propEnum = backend.getPropEnum();
				
		        data.putParcelableArray("propEnum", Enum0Parcelable.wrapArray(propEnum));
				msg.setData(data);
				sendMessageToClients(msg);
			}
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
		public void onPropEnumChanged(Enum0[] propEnum){
			Log.i(TAG, "New value for PropEnum from backend" + propEnum);

			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.SET_PropEnum.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("propEnum", Enum0Parcelable.wrapArray(propEnum));
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
		public void onSigEnum(Enum0[] paramEnum){
			Log.i(TAG, "New singal for SigEnum = "+ " " + paramEnum);
			Message msg = new Message();
			msg.what = StructArrayInterfaceMessageType.SIG_SigEnum.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("paramEnum", Enum0Parcelable.wrapArray(paramEnum));
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
