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
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_android_messenger.Enum1Parcelable;
import tbSame2.tbSame2_api.Enum2;
import tbSame2.tbSame2_android_messenger.Enum2Parcelable;

import tbSame2.tbSame2_api.ISameEnum2InterfaceEventListener;
import tbSame2.tbSame2_android_service.ISameEnum2InterfaceServiceProvider;
import tbSame2.tbSame2_api.ISameEnum2Interface;
import tbSame2.tbSame2_api.AbstractSameEnum2Interface;
import tbSame2.tbSame2_api.RemoteOperationException;
import tbSame2.tbSame2_android_messenger.SameEnum2InterfaceMessageType;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
public class SameEnum2InterfaceServiceAdapter extends Service
{
	private static final String TAG = "SameEnum2InterfaceServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	// Lifetime of mBackendService and its accessibility through mServiceProvider is controlled by the backend provide with setService function.
	// The ServiceAdapter is just a user of the backend. 
	// Use provided ServiceStarter classes and the start and stop functions for that.
	private static ISameEnum2Interface mBackendService;
	private static ISameEnum2InterfaceServiceProvider mServiceProvider;
	private static final Object sBackendMutex = new Object();

	public SameEnum2InterfaceServiceAdapter()
	{
	}

	public static ISameEnum2Interface setService(ISameEnum2InterfaceServiceProvider serviceProvider)
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
					Log.i(TAG, "LIFECYCLE: setService(SameEnum2Interface) called. For handler " + mHandler);
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
		Log.i(TAG, "LIFECYCLE: onCreate(SameEnum2InterfaceService) called. context = " + this);
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
		Log.i(TAG, "LIFECYCLE: SameEnum2InterfaceService::onStartCommand called. context = " + this +
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
		Log.i(TAG, "LIFECYCLE: onDestroy(SameEnum2InterfaceService) - proc = " + ", mMessenger = " + mMessenger);
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
	class IncomingHandler extends Handler implements ISameEnum2InterfaceEventListener
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
			ISameEnum2Interface backend;
			synchronized (SameEnum2InterfaceServiceAdapter.sBackendMutex)
			{
				backend = SameEnum2InterfaceServiceAdapter.mBackendService;
			}
			SameEnum2InterfaceMessageType msgType =
				SameEnum2InterfaceMessageType.fromInteger(msg.what);
			if (backend == null || !backend._isReady())
			{
				if (msgType != SameEnum2InterfaceMessageType.REGISTER_CLIENT
					&& msgType != SameEnum2InterfaceMessageType.UNREGISTER_CLIENT
					&& msgType != SameEnum2InterfaceMessageType.RPC_Func1Req
					&& msgType != SameEnum2InterfaceMessageType.RPC_Func2Req)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: SameEnum2InterfaceMessageType" + msgType );
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
				case RPC_Func1Req: {
					Bundle data = msg.getData();
					
        data.setClassLoader(Enum1Parcelable.class.getClassLoader());
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        Enum1 param1 = data.getParcelable("param1", Enum1Parcelable.class).getEnum1();

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = SameEnum2InterfaceMessageType.RPC_Func1Resp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);
						resp_data.putBoolean("error", true);
						resp_data.putString("errorMessage", "service not ready");
						resp_data.putInt("errorCode", RemoteOperationException.ERROR_SERVICE_NOT_READY);
						respMsg.setData(resp_data);
						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send func1 not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.func1Async(param1).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = SameEnum2InterfaceMessageType.RPC_Func1Resp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "func1 failed: " + errorMessage);
							Log.d(TAG, "func1 exception details", cause);
							resp_data.putBoolean("error", true);
							resp_data.putString("errorMessage", errorMessage);
							int errorCode;
							if (cause instanceof RemoteOperationException) {
								errorCode = ((RemoteOperationException) cause).getErrorCode();
							} else if (cause instanceof IllegalArgumentException) {
								errorCode = RemoteOperationException.ERROR_INVALID_ARGUMENT;
							} else if (cause instanceof UnsupportedOperationException) {
								errorCode = RemoteOperationException.ERROR_NOT_IMPLEMENTED;
							} else {
								errorCode = RemoteOperationException.ERROR_INTERNAL;
							}
							resp_data.putInt("errorCode", errorCode);
						} else {
							Enum1 result = (Enum1) rawResult;
							
		        resp_data.putParcelable("result", new Enum1Parcelable(result));
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send func1 response: " + e);
							}
						} else {
							Log.w(TAG, "func1: replyTo is null, cannot send response");
						}
					});
					break;
				}
				case RPC_Func2Req: {
					Bundle data = msg.getData();
					
    // all structs, even from other modules, are known at compile time (see gradle files) and share the same PathClassLoader,
    // therefore, any class loader provide access to the same PathClassLoader.
        data.setClassLoader(Enum1Parcelable.class.getClassLoader());
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        Enum1 param1 = data.getParcelable("param1", Enum1Parcelable.class).getEnum1();
					
			        Enum2 param2 = data.getParcelable("param2", Enum2Parcelable.class).getEnum2();

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = SameEnum2InterfaceMessageType.RPC_Func2Resp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);
						resp_data.putBoolean("error", true);
						resp_data.putString("errorMessage", "service not ready");
						resp_data.putInt("errorCode", RemoteOperationException.ERROR_SERVICE_NOT_READY);
						respMsg.setData(resp_data);
						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send func2 not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.func2Async(param1, param2).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = SameEnum2InterfaceMessageType.RPC_Func2Resp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "func2 failed: " + errorMessage);
							Log.d(TAG, "func2 exception details", cause);
							resp_data.putBoolean("error", true);
							resp_data.putString("errorMessage", errorMessage);
							int errorCode;
							if (cause instanceof RemoteOperationException) {
								errorCode = ((RemoteOperationException) cause).getErrorCode();
							} else if (cause instanceof IllegalArgumentException) {
								errorCode = RemoteOperationException.ERROR_INVALID_ARGUMENT;
							} else if (cause instanceof UnsupportedOperationException) {
								errorCode = RemoteOperationException.ERROR_NOT_IMPLEMENTED;
							} else {
								errorCode = RemoteOperationException.ERROR_INTERNAL;
							}
							resp_data.putInt("errorCode", errorCode);
						} else {
							Enum1 result = (Enum1) rawResult;
							
		        resp_data.putParcelable("result", new Enum1Parcelable(result));
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send func2 response: " + e);
							}
						} else {
							Log.w(TAG, "func2: replyTo is null, cannot send response");
						}
					});
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
			msg.what = SameEnum2InterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			ISameEnum2Interface backend;
			synchronized (SameEnum2InterfaceServiceAdapter.sBackendMutex)
			{
				backend = SameEnum2InterfaceServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				
				Enum1 prop1 = backend.getProp1();
				
		        data.putParcelable("prop1", new Enum1Parcelable(prop1));
				Enum2 prop2 = backend.getProp2();
				
		        data.putParcelable("prop2", new Enum2Parcelable(prop2));
				msg.setData(data);
				sendMessageToClients(msg);
			}
		}
		@Override
		public void onProp1Changed(Enum1 prop1){
			Log.i(TAG, "New value for Prop1 from backend" + prop1);

			Message msg = new Message();
			msg.what = SameEnum2InterfaceMessageType.SET_Prop1.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("prop1", new Enum1Parcelable(prop1));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onProp2Changed(Enum2 prop2){
			Log.i(TAG, "New value for Prop2 from backend" + prop2);

			Message msg = new Message();
			msg.what = SameEnum2InterfaceMessageType.SET_Prop2.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("prop2", new Enum2Parcelable(prop2));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSig1(Enum1 param1){
			Log.i(TAG, "New singal for Sig1 = "+ " " + param1);
			Message msg = new Message();
			msg.what = SameEnum2InterfaceMessageType.SIG_Sig1.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("param1", new Enum1Parcelable(param1));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSig2(Enum1 param1, Enum2 param2){
			Log.i(TAG, "New singal for Sig2 = "+ " " + param1+ " " + param2);
			Message msg = new Message();
			msg.what = SameEnum2InterfaceMessageType.SIG_Sig2.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("param1", new Enum1Parcelable(param1));
			
		        data.putParcelable("param2", new Enum2Parcelable(param2));
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
