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
import testbed1.testbed1_api.StructBoolWithArray;
import testbed1.testbed1_android_messenger.StructBoolWithArrayParcelable;
import testbed1.testbed1_api.StructEnumWithArray;
import testbed1.testbed1_android_messenger.StructEnumWithArrayParcelable;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_android_messenger.StructFloatParcelable;
import testbed1.testbed1_api.StructFloatWithArray;
import testbed1.testbed1_android_messenger.StructFloatWithArrayParcelable;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_android_messenger.StructIntParcelable;
import testbed1.testbed1_api.StructIntWithArray;
import testbed1.testbed1_android_messenger.StructIntWithArrayParcelable;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_android_messenger.StructStringParcelable;
import testbed1.testbed1_api.StructStringWithArray;
import testbed1.testbed1_android_messenger.StructStringWithArrayParcelable;

import testbed1.testbed1_api.IStructArray2InterfaceEventListener;
import testbed1.testbed1_android_service.IStructArray2InterfaceServiceProvider;
import testbed1.testbed1_api.IStructArray2Interface;
import testbed1.testbed1_api.AbstractStructArray2Interface;
import testbed1.testbed1_api.RemoteOperationException;
import testbed1.testbed1_android_messenger.StructArray2InterfaceMessageType;
import testbed1.testbed1_android_messenger.Conversions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class StructArray2InterfaceServiceAdapter extends Service
{
	private static final String TAG = "StructArray2InterfaceServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	// Lifetime of mBackendService and its accessibility through mServiceProvider is controlled by the backend provide with setService function.
	// The ServiceAdapter is just a user of the backend. 
	// Use provided ServiceStarter classes and the start and stop functions for that.
	private static IStructArray2Interface mBackendService;
	private static IStructArray2InterfaceServiceProvider mServiceProvider;
	private static final Object sBackendMutex = new Object();

	public StructArray2InterfaceServiceAdapter()
	{
	}

	public static IStructArray2Interface setService(IStructArray2InterfaceServiceProvider serviceProvider)
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
					Log.i(TAG, "LIFECYCLE: setService(StructArray2Interface) called. For handler " + mHandler);
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
		Log.i(TAG, "LIFECYCLE: onCreate(StructArray2InterfaceService) called. context = " + this);
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
		Log.i(TAG, "LIFECYCLE: StructArray2InterfaceService::onStartCommand called. context = " + this +
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
		Log.i(TAG, "LIFECYCLE: onDestroy(StructArray2InterfaceService) - proc = " + ", mMessenger = " + mMessenger);
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
	class IncomingHandler extends Handler implements IStructArray2InterfaceEventListener
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
			IStructArray2Interface backend;
			synchronized (StructArray2InterfaceServiceAdapter.sBackendMutex)
			{
				backend = StructArray2InterfaceServiceAdapter.mBackendService;
			}
			StructArray2InterfaceMessageType msgType =
				StructArray2InterfaceMessageType.fromInteger(msg.what);
			if (backend == null || !backend._isReady())
			{
				if (msgType != StructArray2InterfaceMessageType.REGISTER_CLIENT
					&& msgType != StructArray2InterfaceMessageType.UNREGISTER_CLIENT
					&& msgType != StructArray2InterfaceMessageType.RPC_FuncBoolReq
					&& msgType != StructArray2InterfaceMessageType.RPC_FuncIntReq
					&& msgType != StructArray2InterfaceMessageType.RPC_FuncFloatReq
					&& msgType != StructArray2InterfaceMessageType.RPC_FuncStringReq
					&& msgType != StructArray2InterfaceMessageType.RPC_FuncEnumReq)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: StructArray2InterfaceMessageType" + msgType );
					return;
				}
			}
			switch (msgType)
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
						data.setClassLoader(StructBoolWithArrayParcelable.class.getClassLoader());
						
			        StructBoolWithArray propBool = data.getParcelable("propBool", StructBoolWithArrayParcelable.class).getStructBoolWithArray();
						backend.setPropBool(propBool);
						break;
					}
					case PROP_PropInt:
					{
						Bundle data = msg.getData();
						data.setClassLoader(StructIntWithArrayParcelable.class.getClassLoader());
						
			        StructIntWithArray propInt = data.getParcelable("propInt", StructIntWithArrayParcelable.class).getStructIntWithArray();
						backend.setPropInt(propInt);
						break;
					}
					case PROP_PropFloat:
					{
						Bundle data = msg.getData();
						data.setClassLoader(StructFloatWithArrayParcelable.class.getClassLoader());
						
			        StructFloatWithArray propFloat = data.getParcelable("propFloat", StructFloatWithArrayParcelable.class).getStructFloatWithArray();
						backend.setPropFloat(propFloat);
						break;
					}
					case PROP_PropString:
					{
						Bundle data = msg.getData();
						data.setClassLoader(StructStringWithArrayParcelable.class.getClassLoader());
						
			        StructStringWithArray propString = data.getParcelable("propString", StructStringWithArrayParcelable.class).getStructStringWithArray();
						backend.setPropString(propString);
						break;
					}
					case PROP_PropEnum:
					{
						Bundle data = msg.getData();
						data.setClassLoader(StructEnumWithArrayParcelable.class.getClassLoader());
						
			        StructEnumWithArray propEnum = data.getParcelable("propEnum", StructEnumWithArrayParcelable.class).getStructEnumWithArray();
						backend.setPropEnum(propEnum);
						break;
					}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_FuncBoolReq: {

					Bundle data = msg.getData();
					
        data.setClassLoader(StructBoolWithArrayParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        StructBoolWithArray paramBool = data.getParcelable("paramBool", StructBoolWithArrayParcelable.class).getStructBoolWithArray();
					Message respMsg = new Message();
					respMsg.what = StructArray2InterfaceMessageType.RPC_FuncBoolResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						List<StructBool> result =  backend.funcBool(paramBool);
						
		        resp_data.putParcelableArray("result",StructBoolParcelable.wrapArray(Conversions.toArray(result, new StructBool[0])));
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
					
        data.setClassLoader(StructIntWithArrayParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        StructIntWithArray paramInt = data.getParcelable("paramInt", StructIntWithArrayParcelable.class).getStructIntWithArray();
					Message respMsg = new Message();
					respMsg.what = StructArray2InterfaceMessageType.RPC_FuncIntResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						List<StructInt> result =  backend.funcInt(paramInt);
						
		        resp_data.putParcelableArray("result",StructIntParcelable.wrapArray(Conversions.toArray(result, new StructInt[0])));
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
					
        data.setClassLoader(StructFloatWithArrayParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        StructFloatWithArray paramFloat = data.getParcelable("paramFloat", StructFloatWithArrayParcelable.class).getStructFloatWithArray();
					Message respMsg = new Message();
					respMsg.what = StructArray2InterfaceMessageType.RPC_FuncFloatResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						List<StructFloat> result =  backend.funcFloat(paramFloat);
						
		        resp_data.putParcelableArray("result",StructFloatParcelable.wrapArray(Conversions.toArray(result, new StructFloat[0])));
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
					
        data.setClassLoader(StructStringWithArrayParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        StructStringWithArray paramString = data.getParcelable("paramString", StructStringWithArrayParcelable.class).getStructStringWithArray();
					Message respMsg = new Message();
					respMsg.what = StructArray2InterfaceMessageType.RPC_FuncStringResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						List<StructString> result =  backend.funcString(paramString);
						
		        resp_data.putParcelableArray("result",StructStringParcelable.wrapArray(Conversions.toArray(result, new StructString[0])));
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
					
        data.setClassLoader(StructEnumWithArrayParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        StructEnumWithArray paramEnum = data.getParcelable("paramEnum", StructEnumWithArrayParcelable.class).getStructEnumWithArray();
					Message respMsg = new Message();
					respMsg.what = StructArray2InterfaceMessageType.RPC_FuncEnumResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						List<Enum0> result =  backend.funcEnum(paramEnum);
						
		        resp_data.putParcelableArray("result",Enum0Parcelable.wrapArray(Conversions.toArray(result, new Enum0[0])));
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
			msg.what = StructArray2InterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			IStructArray2Interface backend;
			synchronized (StructArray2InterfaceServiceAdapter.sBackendMutex)
			{
				backend = StructArray2InterfaceServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				
				StructBoolWithArray propBool = backend.getPropBool();
				
		        data.putParcelable("propBool", new StructBoolWithArrayParcelable(propBool));
				StructIntWithArray propInt = backend.getPropInt();
				
		        data.putParcelable("propInt", new StructIntWithArrayParcelable(propInt));
				StructFloatWithArray propFloat = backend.getPropFloat();
				
		        data.putParcelable("propFloat", new StructFloatWithArrayParcelable(propFloat));
				StructStringWithArray propString = backend.getPropString();
				
		        data.putParcelable("propString", new StructStringWithArrayParcelable(propString));
				StructEnumWithArray propEnum = backend.getPropEnum();
				
		        data.putParcelable("propEnum", new StructEnumWithArrayParcelable(propEnum));
				msg.setData(data);
				sendMessageToClients(msg);
			}
		}
		@Override
		public void onPropBoolChanged(StructBoolWithArray propBool){
			Log.i(TAG, "New value for PropBool from backend" + propBool);

			Message msg = new Message();
			msg.what = StructArray2InterfaceMessageType.SET_PropBool.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("propBool", new StructBoolWithArrayParcelable(propBool));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropIntChanged(StructIntWithArray propInt){
			Log.i(TAG, "New value for PropInt from backend" + propInt);

			Message msg = new Message();
			msg.what = StructArray2InterfaceMessageType.SET_PropInt.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("propInt", new StructIntWithArrayParcelable(propInt));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropFloatChanged(StructFloatWithArray propFloat){
			Log.i(TAG, "New value for PropFloat from backend" + propFloat);

			Message msg = new Message();
			msg.what = StructArray2InterfaceMessageType.SET_PropFloat.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("propFloat", new StructFloatWithArrayParcelable(propFloat));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropStringChanged(StructStringWithArray propString){
			Log.i(TAG, "New value for PropString from backend" + propString);

			Message msg = new Message();
			msg.what = StructArray2InterfaceMessageType.SET_PropString.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("propString", new StructStringWithArrayParcelable(propString));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropEnumChanged(StructEnumWithArray propEnum){
			Log.i(TAG, "New value for PropEnum from backend" + propEnum);

			Message msg = new Message();
			msg.what = StructArray2InterfaceMessageType.SET_PropEnum.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("propEnum", new StructEnumWithArrayParcelable(propEnum));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigBool(StructBoolWithArray paramBool){
			Log.i(TAG, "New singal for SigBool = "+ " " + paramBool);
			Message msg = new Message();
			msg.what = StructArray2InterfaceMessageType.SIG_SigBool.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("paramBool", new StructBoolWithArrayParcelable(paramBool));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigInt(StructIntWithArray paramInt){
			Log.i(TAG, "New singal for SigInt = "+ " " + paramInt);
			Message msg = new Message();
			msg.what = StructArray2InterfaceMessageType.SIG_SigInt.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("paramInt", new StructIntWithArrayParcelable(paramInt));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigFloat(StructFloatWithArray paramFloat){
			Log.i(TAG, "New singal for SigFloat = "+ " " + paramFloat);
			Message msg = new Message();
			msg.what = StructArray2InterfaceMessageType.SIG_SigFloat.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("paramFloat", new StructFloatWithArrayParcelable(paramFloat));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigString(StructStringWithArray paramString){
			Log.i(TAG, "New singal for SigString = "+ " " + paramString);
			Message msg = new Message();
			msg.what = StructArray2InterfaceMessageType.SIG_SigString.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("paramString", new StructStringWithArrayParcelable(paramString));
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
