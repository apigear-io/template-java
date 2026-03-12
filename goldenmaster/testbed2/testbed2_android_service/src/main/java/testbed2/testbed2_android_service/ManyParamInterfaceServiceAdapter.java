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

import testbed2.testbed2_api.IManyParamInterfaceEventListener;
import testbed2.testbed2_android_service.IManyParamInterfaceServiceProvider;
import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_api.AbstractManyParamInterface;
import testbed2.testbed2_api.RemoteOperationException;
import testbed2.testbed2_android_messenger.ManyParamInterfaceMessageType;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
public class ManyParamInterfaceServiceAdapter extends Service
{
	private static final String TAG = "ManyParamInterfaceServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	// Lifetime of mBackendService and its accessibility through mServiceProvider is controlled by the backend provide with setService function.
	// The ServiceAdapter is just a user of the backend. 
	// Use provided ServiceStarter classes and the start and stop functions for that.
	private static IManyParamInterface mBackendService;
	private static IManyParamInterfaceServiceProvider mServiceProvider;
	private static final Object sBackendMutex = new Object();

	public ManyParamInterfaceServiceAdapter()
	{
	}

	public static IManyParamInterface setService(IManyParamInterfaceServiceProvider serviceProvider)
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
					Log.i(TAG, "LIFECYCLE: setService(ManyParamInterface) called. For handler " + mHandler);
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
		Log.i(TAG, "LIFECYCLE: onCreate(ManyParamInterfaceService) called. context = " + this);
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
		Log.i(TAG, "LIFECYCLE: ManyParamInterfaceService::onStartCommand called. context = " + this +
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
		Log.i(TAG, "LIFECYCLE: onDestroy(ManyParamInterfaceService) - proc = " + ", mMessenger = " + mMessenger);
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
	class IncomingHandler extends Handler implements IManyParamInterfaceEventListener
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
			IManyParamInterface backend;
			synchronized (ManyParamInterfaceServiceAdapter.sBackendMutex)
			{
				backend = ManyParamInterfaceServiceAdapter.mBackendService;
			}
			ManyParamInterfaceMessageType msgType =
				ManyParamInterfaceMessageType.fromInteger(msg.what);
			if (backend == null || !backend._isReady())
			{
				if (msgType != ManyParamInterfaceMessageType.REGISTER_CLIENT
					&& msgType != ManyParamInterfaceMessageType.UNREGISTER_CLIENT
					&& msgType != ManyParamInterfaceMessageType.RPC_Func1Req
					&& msgType != ManyParamInterfaceMessageType.RPC_Func2Req
					&& msgType != ManyParamInterfaceMessageType.RPC_Func3Req
					&& msgType != ManyParamInterfaceMessageType.RPC_Func4Req)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: ManyParamInterfaceMessageType" + msgType );
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
						
			        int prop1 = data.getInt("prop1", 0);
						backend.setProp1(prop1);
						break;
					}
					case PROP_Prop2:
					{
						Bundle data = msg.getData();
						
			        int prop2 = data.getInt("prop2", 0);
						backend.setProp2(prop2);
						break;
					}
					case PROP_Prop3:
					{
						Bundle data = msg.getData();
						
			        int prop3 = data.getInt("prop3", 0);
						backend.setProp3(prop3);
						break;
					}
					case PROP_Prop4:
					{
						Bundle data = msg.getData();
						
			        int prop4 = data.getInt("prop4", 0);
						backend.setProp4(prop4);
						break;
					}
				case RPC_Func1Req: {
					Bundle data = msg.getData();
					
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        int param1 = data.getInt("param1", 0);

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = ManyParamInterfaceMessageType.RPC_Func1Resp.getValue();
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
						respMsg.what = ManyParamInterfaceMessageType.RPC_Func1Resp.getValue();
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
							int result = (int) rawResult;
							
		        resp_data.putInt("result", result);
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
					
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        int param1 = data.getInt("param1", 0);
					
			        int param2 = data.getInt("param2", 0);

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = ManyParamInterfaceMessageType.RPC_Func2Resp.getValue();
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
						respMsg.what = ManyParamInterfaceMessageType.RPC_Func2Resp.getValue();
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
							int result = (int) rawResult;
							
		        resp_data.putInt("result", result);
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
				case RPC_Func3Req: {
					Bundle data = msg.getData();
					
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        int param1 = data.getInt("param1", 0);
					
			        int param2 = data.getInt("param2", 0);
					
			        int param3 = data.getInt("param3", 0);

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = ManyParamInterfaceMessageType.RPC_Func3Resp.getValue();
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
								Log.e(TAG, "failed to send func3 not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.func3Async(param1, param2, param3).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = ManyParamInterfaceMessageType.RPC_Func3Resp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "func3 failed: " + errorMessage);
							Log.d(TAG, "func3 exception details", cause);
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
							int result = (int) rawResult;
							
		        resp_data.putInt("result", result);
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send func3 response: " + e);
							}
						} else {
							Log.w(TAG, "func3: replyTo is null, cannot send response");
						}
					});
					break;
				}
				case RPC_Func4Req: {
					Bundle data = msg.getData();
					
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        int param1 = data.getInt("param1", 0);
					
			        int param2 = data.getInt("param2", 0);
					
			        int param3 = data.getInt("param3", 0);
					
			        int param4 = data.getInt("param4", 0);

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = ManyParamInterfaceMessageType.RPC_Func4Resp.getValue();
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
								Log.e(TAG, "failed to send func4 not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.func4Async(param1, param2, param3, param4).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = ManyParamInterfaceMessageType.RPC_Func4Resp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "func4 failed: " + errorMessage);
							Log.d(TAG, "func4 exception details", cause);
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
							int result = (int) rawResult;
							
		        resp_data.putInt("result", result);
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send func4 response: " + e);
							}
						} else {
							Log.w(TAG, "func4: replyTo is null, cannot send response");
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
			msg.what = ManyParamInterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			IManyParamInterface backend;
			synchronized (ManyParamInterfaceServiceAdapter.sBackendMutex)
			{
				backend = ManyParamInterfaceServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				
				int prop1 = backend.getProp1();
				
		        data.putInt("prop1", prop1);
				int prop2 = backend.getProp2();
				
		        data.putInt("prop2", prop2);
				int prop3 = backend.getProp3();
				
		        data.putInt("prop3", prop3);
				int prop4 = backend.getProp4();
				
		        data.putInt("prop4", prop4);
				msg.setData(data);
				sendMessageToClients(msg);
			}
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
