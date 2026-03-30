//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package tbEnum.tbEnum_android_service;

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
import tbEnum.tbEnum_api.Enum0;
import tbEnum.tbEnum_android_messenger.Enum0Parcelable;
import tbEnum.tbEnum_api.Enum1;
import tbEnum.tbEnum_android_messenger.Enum1Parcelable;
import tbEnum.tbEnum_api.Enum2;
import tbEnum.tbEnum_android_messenger.Enum2Parcelable;
import tbEnum.tbEnum_api.Enum3;
import tbEnum.tbEnum_android_messenger.Enum3Parcelable;

import tbEnum.tbEnum_api.IEnumInterfaceEventListener;
import tbEnum.tbEnum_android_service.IEnumInterfaceServiceProvider;
import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_api.AbstractEnumInterface;
import tbEnum.tbEnum_api.RemoteOperationException;
import tbEnum.tbEnum_android_messenger.EnumInterfaceMessageType;
import tbEnum.tbEnum_android_messenger.Conversions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class EnumInterfaceServiceAdapter extends Service
{
	private static final String TAG = "EnumInterfaceServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	// Lifetime of mBackendService and its accessibility through mServiceProvider is controlled by the backend provide with setService function.
	// The ServiceAdapter is just a user of the backend. 
	// Use provided ServiceStarter classes and the start and stop functions for that.
	private static IEnumInterface mBackendService;
	private static IEnumInterfaceServiceProvider mServiceProvider;
	private static final Object sBackendMutex = new Object();

	public EnumInterfaceServiceAdapter()
	{
	}

	public static IEnumInterface setService(IEnumInterfaceServiceProvider serviceProvider)
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
					Log.i(TAG, "LIFECYCLE: setService(EnumInterface) called. For handler " + mHandler);
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
		Log.i(TAG, "LIFECYCLE: onCreate(EnumInterfaceService) called. context = " + this);
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
		Log.i(TAG, "LIFECYCLE: EnumInterfaceService::onStartCommand called. context = " + this +
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
		Log.i(TAG, "LIFECYCLE: onDestroy(EnumInterfaceService) - proc = " + ", mMessenger = " + mMessenger);
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
	class IncomingHandler extends Handler implements IEnumInterfaceEventListener
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
			IEnumInterface backend;
			synchronized (EnumInterfaceServiceAdapter.sBackendMutex)
			{
				backend = EnumInterfaceServiceAdapter.mBackendService;
			}
			EnumInterfaceMessageType msgType =
				EnumInterfaceMessageType.fromInteger(msg.what);
			if (backend == null || !backend._isReady())
			{
				if (msgType != EnumInterfaceMessageType.REGISTER_CLIENT
					&& msgType != EnumInterfaceMessageType.UNREGISTER_CLIENT
					&& msgType != EnumInterfaceMessageType.RPC_Func0Req
					&& msgType != EnumInterfaceMessageType.RPC_Func1Req
					&& msgType != EnumInterfaceMessageType.RPC_Func2Req
					&& msgType != EnumInterfaceMessageType.RPC_Func3Req)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: EnumInterfaceMessageType" + msgType );
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
					case PROP_Prop0:
					{
						Bundle data = msg.getData();
						data.setClassLoader(Enum0Parcelable.class.getClassLoader());
						
			        Enum0 prop0 = data.getParcelable("prop0", Enum0Parcelable.class).getEnum0();
						backend.setProp0(prop0);
						break;
					}
					case PROP_Prop1:
					{
						Bundle data = msg.getData();
						data.setClassLoader(Enum1Parcelable.class.getClassLoader());
						
			        Enum1 prop1 = data.getParcelable("prop1", Enum1Parcelable.class).getEnum1();
						backend.setProp1(prop1);
						break;
					}
					case PROP_Prop2:
					{
						Bundle data = msg.getData();
						data.setClassLoader(Enum2Parcelable.class.getClassLoader());
						
			        Enum2 prop2 = data.getParcelable("prop2", Enum2Parcelable.class).getEnum2();
						backend.setProp2(prop2);
						break;
					}
					case PROP_Prop3:
					{
						Bundle data = msg.getData();
						data.setClassLoader(Enum3Parcelable.class.getClassLoader());
						
			        Enum3 prop3 = data.getParcelable("prop3", Enum3Parcelable.class).getEnum3();
						backend.setProp3(prop3);
						break;
					}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_Func0Req: {

					Bundle data = msg.getData();
					
        data.setClassLoader(Enum0Parcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        Enum0 param0 = data.getParcelable("param0", Enum0Parcelable.class).getEnum0();
					Message respMsg = new Message();
					respMsg.what = EnumInterfaceMessageType.RPC_Func0Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						Enum0 result =  backend.func0(param0);
						
		        resp_data.putParcelable("result", new Enum0Parcelable(result));
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "func0 failed: " + errorMessage);
						Log.d(TAG, "func0 exception details", e);
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
							Log.e(TAG, "failed to send func0 response: " + e);
						}
					} else {
						Log.w(TAG, "func0: replyTo is null, cannot send response");
					}
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
					Message respMsg = new Message();
					respMsg.what = EnumInterfaceMessageType.RPC_Func1Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						Enum1 result =  backend.func1(param1);
						
		        resp_data.putParcelable("result", new Enum1Parcelable(result));
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "func1 failed: " + errorMessage);
						Log.d(TAG, "func1 exception details", e);
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
							Log.e(TAG, "failed to send func1 response: " + e);
						}
					} else {
						Log.w(TAG, "func1: replyTo is null, cannot send response");
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_Func2Req: {

					Bundle data = msg.getData();
					
        data.setClassLoader(Enum2Parcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        Enum2 param2 = data.getParcelable("param2", Enum2Parcelable.class).getEnum2();
					Message respMsg = new Message();
					respMsg.what = EnumInterfaceMessageType.RPC_Func2Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						Enum2 result =  backend.func2(param2);
						
		        resp_data.putParcelable("result", new Enum2Parcelable(result));
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "func2 failed: " + errorMessage);
						Log.d(TAG, "func2 exception details", e);
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
							Log.e(TAG, "failed to send func2 response: " + e);
						}
					} else {
						Log.w(TAG, "func2: replyTo is null, cannot send response");
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_Func3Req: {

					Bundle data = msg.getData();
					
        data.setClassLoader(Enum3Parcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        Enum3 param3 = data.getParcelable("param3", Enum3Parcelable.class).getEnum3();
					Message respMsg = new Message();
					respMsg.what = EnumInterfaceMessageType.RPC_Func3Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						Enum3 result =  backend.func3(param3);
						
		        resp_data.putParcelable("result", new Enum3Parcelable(result));
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "func3 failed: " + errorMessage);
						Log.d(TAG, "func3 exception details", e);
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
							Log.e(TAG, "failed to send func3 response: " + e);
						}
					} else {
						Log.w(TAG, "func3: replyTo is null, cannot send response");
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
			msg.what = EnumInterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			IEnumInterface backend;
			synchronized (EnumInterfaceServiceAdapter.sBackendMutex)
			{
				backend = EnumInterfaceServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				
				Enum0 prop0 = backend.getProp0();
				
		        data.putParcelable("prop0", new Enum0Parcelable(prop0));
				Enum1 prop1 = backend.getProp1();
				
		        data.putParcelable("prop1", new Enum1Parcelable(prop1));
				Enum2 prop2 = backend.getProp2();
				
		        data.putParcelable("prop2", new Enum2Parcelable(prop2));
				Enum3 prop3 = backend.getProp3();
				
		        data.putParcelable("prop3", new Enum3Parcelable(prop3));
				msg.setData(data);
				sendMessageToClients(msg);
			}
		}
		@Override
		public void onProp0Changed(Enum0 prop0){
			Log.i(TAG, "New value for Prop0 from backend" + prop0);

			Message msg = new Message();
			msg.what = EnumInterfaceMessageType.SET_Prop0.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("prop0", new Enum0Parcelable(prop0));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onProp1Changed(Enum1 prop1){
			Log.i(TAG, "New value for Prop1 from backend" + prop1);

			Message msg = new Message();
			msg.what = EnumInterfaceMessageType.SET_Prop1.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("prop1", new Enum1Parcelable(prop1));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onProp2Changed(Enum2 prop2){
			Log.i(TAG, "New value for Prop2 from backend" + prop2);

			Message msg = new Message();
			msg.what = EnumInterfaceMessageType.SET_Prop2.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("prop2", new Enum2Parcelable(prop2));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onProp3Changed(Enum3 prop3){
			Log.i(TAG, "New value for Prop3 from backend" + prop3);

			Message msg = new Message();
			msg.what = EnumInterfaceMessageType.SET_Prop3.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("prop3", new Enum3Parcelable(prop3));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSig0(Enum0 param0){
			Log.i(TAG, "New singal for Sig0 = "+ " " + param0);
			Message msg = new Message();
			msg.what = EnumInterfaceMessageType.SIG_Sig0.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("param0", new Enum0Parcelable(param0));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSig1(Enum1 param1){
			Log.i(TAG, "New singal for Sig1 = "+ " " + param1);
			Message msg = new Message();
			msg.what = EnumInterfaceMessageType.SIG_Sig1.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("param1", new Enum1Parcelable(param1));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSig2(Enum2 param2){
			Log.i(TAG, "New singal for Sig2 = "+ " " + param2);
			Message msg = new Message();
			msg.what = EnumInterfaceMessageType.SIG_Sig2.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("param2", new Enum2Parcelable(param2));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSig3(Enum3 param3){
			Log.i(TAG, "New singal for Sig3 = "+ " " + param3);
			Message msg = new Message();
			msg.what = EnumInterfaceMessageType.SIG_Sig3.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("param3", new Enum3Parcelable(param3));
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
