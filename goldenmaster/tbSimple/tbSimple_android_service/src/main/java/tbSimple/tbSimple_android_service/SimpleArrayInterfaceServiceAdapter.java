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

import tbSimple.tbSimple_api.ISimpleArrayInterfaceEventListener;
import tbSimple.tbSimple_android_service.ISimpleArrayInterfaceServiceProvider;
import tbSimple.tbSimple_api.ISimpleArrayInterface;
import tbSimple.tbSimple_api.AbstractSimpleArrayInterface;
import tbSimple.tbSimple_api.RemoteOperationException;
import tbSimple.tbSimple_android_messenger.SimpleArrayInterfaceMessageType;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
public class SimpleArrayInterfaceServiceAdapter extends Service
{
	private static final String TAG = "SimpleArrayInterfaceServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	// Lifetime of mBackendService and its accessibility through mServiceProvider is controlled by the backend provide with setService function.
	// The ServiceAdapter is just a user of the backend. 
	// Use provided ServiceStarter classes and the start and stop functions for that.
	private static ISimpleArrayInterface mBackendService;
	private static ISimpleArrayInterfaceServiceProvider mServiceProvider;
	private static final Object sBackendMutex = new Object();

	public SimpleArrayInterfaceServiceAdapter()
	{
	}

	public static ISimpleArrayInterface setService(ISimpleArrayInterfaceServiceProvider serviceProvider)
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
					Log.i(TAG, "LIFECYCLE: setService(SimpleArrayInterface) called. For handler " + mHandler);
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
		Log.i(TAG, "LIFECYCLE: onCreate(SimpleArrayInterfaceService) called. context = " + this);
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
		Log.i(TAG, "LIFECYCLE: SimpleArrayInterfaceService::onStartCommand called. context = " + this +
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
		Log.i(TAG, "LIFECYCLE: onDestroy(SimpleArrayInterfaceService) - proc = " + ", mMessenger = " + mMessenger);
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
	class IncomingHandler extends Handler implements ISimpleArrayInterfaceEventListener
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
			ISimpleArrayInterface backend;
			synchronized (SimpleArrayInterfaceServiceAdapter.sBackendMutex)
			{
				backend = SimpleArrayInterfaceServiceAdapter.mBackendService;
			}
			SimpleArrayInterfaceMessageType msgType =
				SimpleArrayInterfaceMessageType.fromInteger(msg.what);
			if (backend == null || !backend._isReady())
			{
				if (msgType != SimpleArrayInterfaceMessageType.REGISTER_CLIENT
					&& msgType != SimpleArrayInterfaceMessageType.UNREGISTER_CLIENT
					&& msgType != SimpleArrayInterfaceMessageType.RPC_FuncBoolReq
					&& msgType != SimpleArrayInterfaceMessageType.RPC_FuncIntReq
					&& msgType != SimpleArrayInterfaceMessageType.RPC_FuncInt32Req
					&& msgType != SimpleArrayInterfaceMessageType.RPC_FuncInt64Req
					&& msgType != SimpleArrayInterfaceMessageType.RPC_FuncFloatReq
					&& msgType != SimpleArrayInterfaceMessageType.RPC_FuncFloat32Req
					&& msgType != SimpleArrayInterfaceMessageType.RPC_FuncFloat64Req
					&& msgType != SimpleArrayInterfaceMessageType.RPC_FuncStringReq)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: SimpleArrayInterfaceMessageType" + msgType );
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
						
			        boolean[] propBool = data.getBooleanArray("propBool");
						backend.setPropBool(propBool);
						break;
					}
					case PROP_PropInt:
					{
						Bundle data = msg.getData();
						
			        int[] propInt = data.getIntArray("propInt");
						backend.setPropInt(propInt);
						break;
					}
					case PROP_PropInt32:
					{
						Bundle data = msg.getData();
						
			        int[] propInt32 = data.getIntArray("propInt32");
						backend.setPropInt32(propInt32);
						break;
					}
					case PROP_PropInt64:
					{
						Bundle data = msg.getData();
						
			        long[] propInt64 = data.getLongArray("propInt64");
						backend.setPropInt64(propInt64);
						break;
					}
					case PROP_PropFloat:
					{
						Bundle data = msg.getData();
						
			        float[] propFloat = data.getFloatArray("propFloat");
						backend.setPropFloat(propFloat);
						break;
					}
					case PROP_PropFloat32:
					{
						Bundle data = msg.getData();
						
			        float[] propFloat32 = data.getFloatArray("propFloat32");
						backend.setPropFloat32(propFloat32);
						break;
					}
					case PROP_PropFloat64:
					{
						Bundle data = msg.getData();
						
			        double[] propFloat64 = data.getDoubleArray("propFloat64");
						backend.setPropFloat64(propFloat64);
						break;
					}
					case PROP_PropString:
					{
						Bundle data = msg.getData();
						
			        String[] propString = data.getStringArray("propString");
						backend.setPropString(propString);
						break;
					}
					case PROP_PropReadOnlyString:
					{
						Bundle data = msg.getData();
						
			        String propReadOnlyString = data.getString("propReadOnlyString", new String());
						backend.setPropReadOnlyString(propReadOnlyString);
						break;
					}
				case RPC_FuncBoolReq: {
					Bundle data = msg.getData();
					
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        boolean[] paramBool = data.getBooleanArray("paramBool");

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncBoolResp.getValue();
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
						respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncBoolResp.getValue();
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
							boolean[] result = (boolean[]) rawResult;
							
		        resp_data.putBooleanArray("result", result);
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
					
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        int[] paramInt = data.getIntArray("paramInt");

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncIntResp.getValue();
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
						respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncIntResp.getValue();
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
							int[] result = (int[]) rawResult;
							
		        resp_data.putIntArray("result", result);
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
				case RPC_FuncInt32Req: {
					Bundle data = msg.getData();
					
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        int[] paramInt32 = data.getIntArray("paramInt32");

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncInt32Resp.getValue();
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
								Log.e(TAG, "failed to send funcInt32 not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.funcInt32Async(paramInt32).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncInt32Resp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "funcInt32 failed: " + errorMessage);
							Log.d(TAG, "funcInt32 exception details", cause);
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
							int[] result = (int[]) rawResult;
							
		        resp_data.putIntArray("result", result);
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send funcInt32 response: " + e);
							}
						} else {
							Log.w(TAG, "funcInt32: replyTo is null, cannot send response");
						}
					});
					break;
				}
				case RPC_FuncInt64Req: {
					Bundle data = msg.getData();
					
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        long[] paramInt64 = data.getLongArray("paramInt64");

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncInt64Resp.getValue();
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
								Log.e(TAG, "failed to send funcInt64 not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.funcInt64Async(paramInt64).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncInt64Resp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "funcInt64 failed: " + errorMessage);
							Log.d(TAG, "funcInt64 exception details", cause);
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
							long[] result = (long[]) rawResult;
							
		        resp_data.putLongArray("result", result);
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send funcInt64 response: " + e);
							}
						} else {
							Log.w(TAG, "funcInt64: replyTo is null, cannot send response");
						}
					});
					break;
				}
				case RPC_FuncFloatReq: {
					Bundle data = msg.getData();
					
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        float[] paramFloat = data.getFloatArray("paramFloat");

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncFloatResp.getValue();
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
						respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncFloatResp.getValue();
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
							float[] result = (float[]) rawResult;
							
		        resp_data.putFloatArray("result", result);
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
				case RPC_FuncFloat32Req: {
					Bundle data = msg.getData();
					
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        float[] paramFloat32 = data.getFloatArray("paramFloat32");

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncFloat32Resp.getValue();
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
								Log.e(TAG, "failed to send funcFloat32 not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.funcFloat32Async(paramFloat32).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncFloat32Resp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "funcFloat32 failed: " + errorMessage);
							Log.d(TAG, "funcFloat32 exception details", cause);
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
							float[] result = (float[]) rawResult;
							
		        resp_data.putFloatArray("result", result);
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send funcFloat32 response: " + e);
							}
						} else {
							Log.w(TAG, "funcFloat32: replyTo is null, cannot send response");
						}
					});
					break;
				}
				case RPC_FuncFloat64Req: {
					Bundle data = msg.getData();
					
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        double[] paramFloat = data.getDoubleArray("paramFloat");

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncFloat64Resp.getValue();
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
								Log.e(TAG, "failed to send funcFloat64 not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.funcFloat64Async(paramFloat).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncFloat64Resp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "funcFloat64 failed: " + errorMessage);
							Log.d(TAG, "funcFloat64 exception details", cause);
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
							double[] result = (double[]) rawResult;
							
		        resp_data.putDoubleArray("result", result);
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send funcFloat64 response: " + e);
							}
						} else {
							Log.w(TAG, "funcFloat64: replyTo is null, cannot send response");
						}
					});
					break;
				}
				case RPC_FuncStringReq: {
					Bundle data = msg.getData();
					
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        String[] paramString = data.getStringArray("paramString");

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncStringResp.getValue();
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
						respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncStringResp.getValue();
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
							String[] result = (String[]) rawResult;
							
		        resp_data.putStringArray("result", result);
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
			msg.what = SimpleArrayInterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			ISimpleArrayInterface backend;
			synchronized (SimpleArrayInterfaceServiceAdapter.sBackendMutex)
			{
				backend = SimpleArrayInterfaceServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				
				boolean[] propBool = backend.getPropBool();
				
		        data.putBooleanArray("propBool", propBool);
				int[] propInt = backend.getPropInt();
				
		        data.putIntArray("propInt", propInt);
				int[] propInt32 = backend.getPropInt32();
				
		        data.putIntArray("propInt32", propInt32);
				long[] propInt64 = backend.getPropInt64();
				
		        data.putLongArray("propInt64", propInt64);
				float[] propFloat = backend.getPropFloat();
				
		        data.putFloatArray("propFloat", propFloat);
				float[] propFloat32 = backend.getPropFloat32();
				
		        data.putFloatArray("propFloat32", propFloat32);
				double[] propFloat64 = backend.getPropFloat64();
				
		        data.putDoubleArray("propFloat64", propFloat64);
				String[] propString = backend.getPropString();
				
		        data.putStringArray("propString", propString);
				String propReadOnlyString = backend.getPropReadOnlyString();
				
		        data.putString("propReadOnlyString", propReadOnlyString);
				msg.setData(data);
				sendMessageToClients(msg);
			}
		}
		@Override
		public void onPropBoolChanged(boolean[] propBool){
			Log.i(TAG, "New value for PropBool from backend" + propBool);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropBool.getValue();
			Bundle data = new Bundle();
			
		        data.putBooleanArray("propBool", propBool);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropIntChanged(int[] propInt){
			Log.i(TAG, "New value for PropInt from backend" + propInt);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropInt.getValue();
			Bundle data = new Bundle();
			
		        data.putIntArray("propInt", propInt);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropInt32Changed(int[] propInt32){
			Log.i(TAG, "New value for PropInt32 from backend" + propInt32);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropInt32.getValue();
			Bundle data = new Bundle();
			
		        data.putIntArray("propInt32", propInt32);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropInt64Changed(long[] propInt64){
			Log.i(TAG, "New value for PropInt64 from backend" + propInt64);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropInt64.getValue();
			Bundle data = new Bundle();
			
		        data.putLongArray("propInt64", propInt64);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropFloatChanged(float[] propFloat){
			Log.i(TAG, "New value for PropFloat from backend" + propFloat);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropFloat.getValue();
			Bundle data = new Bundle();
			
		        data.putFloatArray("propFloat", propFloat);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropFloat32Changed(float[] propFloat32){
			Log.i(TAG, "New value for PropFloat32 from backend" + propFloat32);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropFloat32.getValue();
			Bundle data = new Bundle();
			
		        data.putFloatArray("propFloat32", propFloat32);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropFloat64Changed(double[] propFloat64){
			Log.i(TAG, "New value for PropFloat64 from backend" + propFloat64);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropFloat64.getValue();
			Bundle data = new Bundle();
			
		        data.putDoubleArray("propFloat64", propFloat64);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropStringChanged(String[] propString){
			Log.i(TAG, "New value for PropString from backend" + propString);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropString.getValue();
			Bundle data = new Bundle();
			
		        data.putStringArray("propString", propString);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropReadOnlyStringChanged(String propReadOnlyString){
			Log.i(TAG, "New value for PropReadOnlyString from backend" + propReadOnlyString);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropReadOnlyString.getValue();
			Bundle data = new Bundle();
			
		        data.putString("propReadOnlyString", propReadOnlyString);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigBool(boolean[] paramBool){
			Log.i(TAG, "New singal for SigBool = "+ " " + paramBool);
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SIG_SigBool.getValue();
			Bundle data = new Bundle();
			
		        data.putBooleanArray("paramBool", paramBool);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigInt(int[] paramInt){
			Log.i(TAG, "New singal for SigInt = "+ " " + paramInt);
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SIG_SigInt.getValue();
			Bundle data = new Bundle();
			
		        data.putIntArray("paramInt", paramInt);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigInt32(int[] paramInt32){
			Log.i(TAG, "New singal for SigInt32 = "+ " " + paramInt32);
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SIG_SigInt32.getValue();
			Bundle data = new Bundle();
			
		        data.putIntArray("paramInt32", paramInt32);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigInt64(long[] paramInt64){
			Log.i(TAG, "New singal for SigInt64 = "+ " " + paramInt64);
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SIG_SigInt64.getValue();
			Bundle data = new Bundle();
			
		        data.putLongArray("paramInt64", paramInt64);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigFloat(float[] paramFloat){
			Log.i(TAG, "New singal for SigFloat = "+ " " + paramFloat);
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SIG_SigFloat.getValue();
			Bundle data = new Bundle();
			
		        data.putFloatArray("paramFloat", paramFloat);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigFloat32(float[] paramFloa32){
			Log.i(TAG, "New singal for SigFloat32 = "+ " " + paramFloa32);
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SIG_SigFloat32.getValue();
			Bundle data = new Bundle();
			
		        data.putFloatArray("paramFloa32", paramFloa32);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigFloat64(double[] paramFloat64){
			Log.i(TAG, "New singal for SigFloat64 = "+ " " + paramFloat64);
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SIG_SigFloat64.getValue();
			Bundle data = new Bundle();
			
		        data.putDoubleArray("paramFloat64", paramFloat64);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigString(String[] paramString){
			Log.i(TAG, "New singal for SigString = "+ " " + paramString);
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SIG_SigString.getValue();
			Bundle data = new Bundle();
			
		        data.putStringArray("paramString", paramString);
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
