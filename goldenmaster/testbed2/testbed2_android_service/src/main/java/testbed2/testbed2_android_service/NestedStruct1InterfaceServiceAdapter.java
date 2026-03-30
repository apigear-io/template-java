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

import testbed2.testbed2_api.INestedStruct1InterfaceEventListener;
import testbed2.testbed2_android_service.INestedStruct1InterfaceServiceProvider;
import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_api.AbstractNestedStruct1Interface;
import testbed2.testbed2_api.RemoteOperationException;
import testbed2.testbed2_android_messenger.NestedStruct1InterfaceMessageType;
import testbed2.testbed2_android_messenger.Conversions;

import java.util.ArrayList;
import java.util.List;
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
	// Lifetime of mBackendService and its accessibility through mServiceProvider is controlled by the backend provide with setService function.
	// The ServiceAdapter is just a user of the backend. 
	// Use provided ServiceStarter classes and the start and stop functions for that.
	private static INestedStruct1Interface mBackendService;
	private static INestedStruct1InterfaceServiceProvider mServiceProvider;
	private static final Object sBackendMutex = new Object();

	public NestedStruct1InterfaceServiceAdapter()
	{
	}

	public static INestedStruct1Interface setService(INestedStruct1InterfaceServiceProvider serviceProvider)
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
					Log.i(TAG, "LIFECYCLE: setService(NestedStruct1Interface) called. For handler " + mHandler);
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
		Log.i(TAG, "LIFECYCLE: onCreate(NestedStruct1InterfaceService) called. context = " + this);
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
		Log.i(TAG, "LIFECYCLE: NestedStruct1InterfaceService::onStartCommand called. context = " + this +
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
		Log.i(TAG, "LIFECYCLE: onDestroy(NestedStruct1InterfaceService) - proc = " + ", mMessenger = " + mMessenger);
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
	class IncomingHandler extends Handler implements INestedStruct1InterfaceEventListener
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
			INestedStruct1Interface backend;
			synchronized (NestedStruct1InterfaceServiceAdapter.sBackendMutex)
			{
				backend = NestedStruct1InterfaceServiceAdapter.mBackendService;
			}
			NestedStruct1InterfaceMessageType msgType =
				NestedStruct1InterfaceMessageType.fromInteger(msg.what);
			if (backend == null || !backend._isReady())
			{
				if (msgType != NestedStruct1InterfaceMessageType.REGISTER_CLIENT
					&& msgType != NestedStruct1InterfaceMessageType.UNREGISTER_CLIENT
					&& msgType != NestedStruct1InterfaceMessageType.RPC_FuncNoReturnValueReq
					&& msgType != NestedStruct1InterfaceMessageType.RPC_FuncNoParamsReq
					&& msgType != NestedStruct1InterfaceMessageType.RPC_Func1Req)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: NestedStruct1InterfaceMessageType" + msgType );
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
					case PROP_Prop1:
					{
						Bundle data = msg.getData();
						data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
						
			        NestedStruct1 prop1 = data.getParcelable("prop1", NestedStruct1Parcelable.class).getNestedStruct1();
						backend.setProp1(prop1);
						break;
					}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_FuncNoReturnValueReq: {

					Bundle data = msg.getData();
					
        data.setClassLoader(NestedStruct1Parcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        NestedStruct1 param1 = data.getParcelable("param1", NestedStruct1Parcelable.class).getNestedStruct1();
					Message respMsg = new Message();
					respMsg.what = NestedStruct1InterfaceMessageType.RPC_FuncNoReturnValueResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						 backend.funcNoReturnValue(param1);
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "funcNoReturnValue failed: " + errorMessage);
						Log.d(TAG, "funcNoReturnValue exception details", e);
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
							Log.e(TAG, "failed to send funcNoReturnValue response: " + e);
						}
					} else {
						Log.w(TAG, "funcNoReturnValue: replyTo is null, cannot send response");
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_FuncNoParamsReq: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					Message respMsg = new Message();
					respMsg.what = NestedStruct1InterfaceMessageType.RPC_FuncNoParamsResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						NestedStruct1 result =  backend.funcNoParams();
						
		        resp_data.putParcelable("result", new NestedStruct1Parcelable(result));
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "funcNoParams failed: " + errorMessage);
						Log.d(TAG, "funcNoParams exception details", e);
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
							Log.e(TAG, "failed to send funcNoParams response: " + e);
						}
					} else {
						Log.w(TAG, "funcNoParams: replyTo is null, cannot send response");
					}
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
					Message respMsg = new Message();
					respMsg.what = NestedStruct1InterfaceMessageType.RPC_Func1Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						NestedStruct1 result =  backend.func1(param1);
						
		        resp_data.putParcelable("result", new NestedStruct1Parcelable(result));
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
			msg.what = NestedStruct1InterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			INestedStruct1Interface backend;
			synchronized (NestedStruct1InterfaceServiceAdapter.sBackendMutex)
			{
				backend = NestedStruct1InterfaceServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				
				NestedStruct1 prop1 = backend.getProp1();
				
		        data.putParcelable("prop1", new NestedStruct1Parcelable(prop1));
				msg.setData(data);
				sendMessageToClients(msg);
			}
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
