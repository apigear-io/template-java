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

import java.util.Map;
import java.util.concurrent.CompletableFuture;
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
				case RPC_FuncBoolReq: {
					Bundle data = msg.getData();
					
        data.setClassLoader(StructBoolWithArrayParcelable.class.getClassLoader());
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        StructBoolWithArray paramBool = data.getParcelable("paramBool", StructBoolWithArrayParcelable.class).getStructBoolWithArray();

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = StructArray2InterfaceMessageType.RPC_FuncBoolResp.getValue();
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
								Log.e(TAG, "failed to send funcBool not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.funcBoolAsync(paramBool).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = StructArray2InterfaceMessageType.RPC_FuncBoolResp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "funcBool failed: " + errorMessage);
							Log.d(TAG, "funcBool exception details", cause);
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
							StructBool[] result = (StructBool[]) rawResult;
							
		        resp_data.putParcelableArray("result",StructBoolParcelable.wrapArray(result));
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send funcBool response: " + e);
							}
						} else {
							Log.w(TAG, "funcBool: replyTo is null, cannot send response");
						}
					});
					break;
				}
				case RPC_FuncIntReq: {
					Bundle data = msg.getData();
					
        data.setClassLoader(StructIntWithArrayParcelable.class.getClassLoader());
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        StructIntWithArray paramInt = data.getParcelable("paramInt", StructIntWithArrayParcelable.class).getStructIntWithArray();

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = StructArray2InterfaceMessageType.RPC_FuncIntResp.getValue();
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
								Log.e(TAG, "failed to send funcInt not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.funcIntAsync(paramInt).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = StructArray2InterfaceMessageType.RPC_FuncIntResp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "funcInt failed: " + errorMessage);
							Log.d(TAG, "funcInt exception details", cause);
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
							StructInt[] result = (StructInt[]) rawResult;
							
		        resp_data.putParcelableArray("result",StructIntParcelable.wrapArray(result));
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send funcInt response: " + e);
							}
						} else {
							Log.w(TAG, "funcInt: replyTo is null, cannot send response");
						}
					});
					break;
				}
				case RPC_FuncFloatReq: {
					Bundle data = msg.getData();
					
        data.setClassLoader(StructFloatWithArrayParcelable.class.getClassLoader());
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        StructFloatWithArray paramFloat = data.getParcelable("paramFloat", StructFloatWithArrayParcelable.class).getStructFloatWithArray();

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = StructArray2InterfaceMessageType.RPC_FuncFloatResp.getValue();
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
								Log.e(TAG, "failed to send funcFloat not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.funcFloatAsync(paramFloat).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = StructArray2InterfaceMessageType.RPC_FuncFloatResp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "funcFloat failed: " + errorMessage);
							Log.d(TAG, "funcFloat exception details", cause);
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
							StructFloat[] result = (StructFloat[]) rawResult;
							
		        resp_data.putParcelableArray("result",StructFloatParcelable.wrapArray(result));
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send funcFloat response: " + e);
							}
						} else {
							Log.w(TAG, "funcFloat: replyTo is null, cannot send response");
						}
					});
					break;
				}
				case RPC_FuncStringReq: {
					Bundle data = msg.getData();
					
        data.setClassLoader(StructStringWithArrayParcelable.class.getClassLoader());
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        StructStringWithArray paramString = data.getParcelable("paramString", StructStringWithArrayParcelable.class).getStructStringWithArray();

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = StructArray2InterfaceMessageType.RPC_FuncStringResp.getValue();
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
								Log.e(TAG, "failed to send funcString not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.funcStringAsync(paramString).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = StructArray2InterfaceMessageType.RPC_FuncStringResp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "funcString failed: " + errorMessage);
							Log.d(TAG, "funcString exception details", cause);
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
							StructString[] result = (StructString[]) rawResult;
							
		        resp_data.putParcelableArray("result",StructStringParcelable.wrapArray(result));
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send funcString response: " + e);
							}
						} else {
							Log.w(TAG, "funcString: replyTo is null, cannot send response");
						}
					});
					break;
				}
				case RPC_FuncEnumReq: {
					Bundle data = msg.getData();
					
        data.setClassLoader(StructEnumWithArrayParcelable.class.getClassLoader());
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        StructEnumWithArray paramEnum = data.getParcelable("paramEnum", StructEnumWithArrayParcelable.class).getStructEnumWithArray();

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = StructArray2InterfaceMessageType.RPC_FuncEnumResp.getValue();
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
								Log.e(TAG, "failed to send funcEnum not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.funcEnumAsync(paramEnum).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = StructArray2InterfaceMessageType.RPC_FuncEnumResp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "funcEnum failed: " + errorMessage);
							Log.d(TAG, "funcEnum exception details", cause);
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
							Enum0[] result = (Enum0[]) rawResult;
							
		        resp_data.putParcelableArray("result",Enum0Parcelable.wrapArray(result));
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send funcEnum response: " + e);
							}
						} else {
							Log.w(TAG, "funcEnum: replyTo is null, cannot send response");
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
