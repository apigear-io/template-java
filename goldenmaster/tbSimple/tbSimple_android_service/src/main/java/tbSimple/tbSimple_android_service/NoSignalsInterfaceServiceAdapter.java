//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package tbSimple.tbSimple_android_service;

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

import tbSimple.tbSimple_api.INoSignalsInterfaceEventListener;
import tbSimple.tbSimple_android_service.INoSignalsInterfaceServiceProvider;
import tbSimple.tbSimple_api.INoSignalsInterface;
import tbSimple.tbSimple_api.AbstractNoSignalsInterface;
import tbSimple.tbSimple_api.RemoteOperationException;
import tbSimple.tbSimple_android_messenger.NoSignalsInterfaceMessageType;
import tbSimple.tbSimple_android_messenger.Conversions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class NoSignalsInterfaceServiceAdapter extends Service
{
	private static final String TAG = "NoSignalsInterfaceServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	// Lifetime of mBackendService and its accessibility through mServiceProvider is controlled by the backend provide with setService function.
	// The ServiceAdapter is just a user of the backend. 
	// Use provided ServiceStarter classes and the start and stop functions for that.
	private static INoSignalsInterface mBackendService;
	private static INoSignalsInterfaceServiceProvider mServiceProvider;
	private static final Object sBackendMutex = new Object();

	public NoSignalsInterfaceServiceAdapter()
	{
	}

	public static INoSignalsInterface setService(INoSignalsInterfaceServiceProvider serviceProvider)
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
					Log.i(TAG, "LIFECYCLE: setService(NoSignalsInterface) called. For handler " + mHandler);
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
		Log.i(TAG, "LIFECYCLE: onCreate(NoSignalsInterfaceService) called. context = " + this);
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
		Log.i(TAG, "LIFECYCLE: NoSignalsInterfaceService::onStartCommand called. context = " + this +
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
		Log.i(TAG, "LIFECYCLE: onDestroy(NoSignalsInterfaceService) - proc = " + ", mMessenger = " + mMessenger);
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
	class IncomingHandler extends Handler implements INoSignalsInterfaceEventListener
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
			INoSignalsInterface backend;
			synchronized (NoSignalsInterfaceServiceAdapter.sBackendMutex)
			{
				backend = NoSignalsInterfaceServiceAdapter.mBackendService;
			}
			NoSignalsInterfaceMessageType msgType =
				NoSignalsInterfaceMessageType.fromInteger(msg.what);
			if (backend == null || !backend._isReady())
			{
				if (msgType != NoSignalsInterfaceMessageType.REGISTER_CLIENT
					&& msgType != NoSignalsInterfaceMessageType.UNREGISTER_CLIENT
					&& msgType != NoSignalsInterfaceMessageType.RPC_FuncVoidReq
					&& msgType != NoSignalsInterfaceMessageType.RPC_FuncBoolReq)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: NoSignalsInterfaceMessageType" + msgType );
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
						
			        boolean propBool = data.getBoolean("propBool", false);
						backend.setPropBool(propBool);
						break;
					}
					case PROP_PropInt:
					{
						Bundle data = msg.getData();
						
			        int propInt = data.getInt("propInt", 0);
						backend.setPropInt(propInt);
						break;
					}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_FuncVoidReq: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					Message respMsg = new Message();
					respMsg.what = NoSignalsInterfaceMessageType.RPC_FuncVoidResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						 backend.funcVoid();
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "funcVoid failed: " + errorMessage);
						Log.d(TAG, "funcVoid exception details", e);
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
							Log.e(TAG, "failed to send funcVoid response: " + e);
						}
					} else {
						Log.w(TAG, "funcVoid: replyTo is null, cannot send response");
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_FuncBoolReq: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        boolean paramBool = data.getBoolean("paramBool", false);
					Message respMsg = new Message();
					respMsg.what = NoSignalsInterfaceMessageType.RPC_FuncBoolResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						boolean result =  backend.funcBool(paramBool);
						
		        resp_data.putBoolean("result", result);
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
			msg.what = NoSignalsInterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			INoSignalsInterface backend;
			synchronized (NoSignalsInterfaceServiceAdapter.sBackendMutex)
			{
				backend = NoSignalsInterfaceServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				
				boolean propBool = backend.getPropBool();
				
		        data.putBoolean("propBool", propBool);
				int propInt = backend.getPropInt();
				
		        data.putInt("propInt", propInt);
				msg.setData(data);
				sendMessageToClients(msg);
			}
		}
		@Override
		public void onPropBoolChanged(boolean propBool){
			Log.i(TAG, "New value for PropBool from backend" + propBool);

			Message msg = new Message();
			msg.what = NoSignalsInterfaceMessageType.SET_PropBool.getValue();
			Bundle data = new Bundle();
			
		        data.putBoolean("propBool", propBool);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropIntChanged(int propInt){
			Log.i(TAG, "New value for PropInt from backend" + propInt);

			Message msg = new Message();
			msg.what = NoSignalsInterfaceMessageType.SET_PropInt.getValue();
			Bundle data = new Bundle();
			
		        data.putInt("propInt", propInt);
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
