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

import tbSimple.tbSimple_api.ISimpleInterfaceEventListener;
import tbSimple.tbSimple_android_service.ISimpleInterfaceServiceProvider;
import tbSimple.tbSimple_api.ISimpleInterface;
import tbSimple.tbSimple_api.AbstractSimpleInterface;
import tbSimple.tbSimple_api.RemoteOperationException;
import tbSimple.tbSimple_android_messenger.SimpleInterfaceMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class SimpleInterfaceServiceAdapter extends Service
{
	private static final String TAG = "SimpleInterfaceServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	// Lifetime of mBackendService and its accessibility through mServiceProvider is controlled by the backend provide with setService function.
	// The ServiceAdapter is just a user of the backend. 
	// Use provided ServiceStarter classes and the start and stop functions for that.
	private static ISimpleInterface mBackendService;
	private static ISimpleInterfaceServiceProvider mServiceProvider;
	private static final Object sBackendMutex = new Object();

	public SimpleInterfaceServiceAdapter()
	{
	}

	public static ISimpleInterface setService(ISimpleInterfaceServiceProvider serviceProvider)
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
					Log.i(TAG, "LIFECYCLE: setService(SimpleInterface) called. For handler " + mHandler);
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
		Log.i(TAG, "LIFECYCLE: onCreate(SimpleInterfaceService) called. context = " + this);
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
		Log.i(TAG, "LIFECYCLE: SimpleInterfaceService::onStartCommand called. context = " + this +
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
		Log.i(TAG, "LIFECYCLE: onDestroy(SimpleInterfaceService) - proc = " + ", mMessenger = " + mMessenger);
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
	class IncomingHandler extends Handler implements ISimpleInterfaceEventListener
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
			ISimpleInterface backend;
			synchronized (SimpleInterfaceServiceAdapter.sBackendMutex)
			{
				backend = SimpleInterfaceServiceAdapter.mBackendService;
			}
			if (backend == null || !backend._isReady())
			{
				if (SimpleInterfaceMessageType.fromInteger(msg.what) != SimpleInterfaceMessageType.REGISTER_CLIENT
					&& SimpleInterfaceMessageType.fromInteger(msg.what) != SimpleInterfaceMessageType.UNREGISTER_CLIENT)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: SimpleInterfaceMessageType" + SimpleInterfaceMessageType.fromInteger(msg.what) );
					return;
				}
			}
			switch (SimpleInterfaceMessageType.fromInteger(msg.what))
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
					case PROP_PropInt32:
					{
						Bundle data = msg.getData();
						
			        int propInt32 = data.getInt("propInt32", 0);
						backend.setPropInt32(propInt32);
						break;
					}
					case PROP_PropInt64:
					{
						Bundle data = msg.getData();
						
			        long propInt64 = data.getLong("propInt64", 0L);
						backend.setPropInt64(propInt64);
						break;
					}
					case PROP_PropFloat:
					{
						Bundle data = msg.getData();
						
			        float propFloat = data.getFloat("propFloat", 0.0f);
						backend.setPropFloat(propFloat);
						break;
					}
					case PROP_PropFloat32:
					{
						Bundle data = msg.getData();
						
			        float propFloat32 = data.getFloat("propFloat32", 0.0f);
						backend.setPropFloat32(propFloat32);
						break;
					}
					case PROP_PropFloat64:
					{
						Bundle data = msg.getData();
						
			        double propFloat64 = data.getDouble("propFloat64", 0.0);
						backend.setPropFloat64(propFloat64);
						break;
					}
					case PROP_PropString:
					{
						Bundle data = msg.getData();
						
			        String propString = data.getString("propString", new String());
						backend.setPropString(propString);
						break;
					}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_FuncNoReturnValueReq: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        boolean paramBool = data.getBoolean("paramBool", false);
					Message respMsg = new Message();
					respMsg.what = SimpleInterfaceMessageType.RPC_FuncNoReturnValueResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						 backend.funcNoReturnValue(paramBool);
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
					respMsg.what = SimpleInterfaceMessageType.RPC_FuncNoParamsResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						boolean result =  backend.funcNoParams();
						
		        resp_data.putBoolean("result", result);
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
				case RPC_FuncBoolReq: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        boolean paramBool = data.getBoolean("paramBool", false);
					Message respMsg = new Message();
					respMsg.what = SimpleInterfaceMessageType.RPC_FuncBoolResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
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
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_FuncIntReq: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        int paramInt = data.getInt("paramInt", 0);
					Message respMsg = new Message();
					respMsg.what = SimpleInterfaceMessageType.RPC_FuncIntResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						int result =  backend.funcInt(paramInt);
						
		        resp_data.putInt("result", result);
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
				case RPC_FuncInt32Req: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        int paramInt32 = data.getInt("paramInt32", 0);
					Message respMsg = new Message();
					respMsg.what = SimpleInterfaceMessageType.RPC_FuncInt32Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						int result =  backend.funcInt32(paramInt32);
						
		        resp_data.putInt("result", result);
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "funcInt32 failed: " + errorMessage);
						Log.d(TAG, "funcInt32 exception details", e);
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
							Log.e(TAG, "failed to send funcInt32 response: " + e);
						}
					} else {
						Log.w(TAG, "funcInt32: replyTo is null, cannot send response");
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_FuncInt64Req: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        long paramInt64 = data.getLong("paramInt64", 0L);
					Message respMsg = new Message();
					respMsg.what = SimpleInterfaceMessageType.RPC_FuncInt64Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						long result =  backend.funcInt64(paramInt64);
						
		        resp_data.putLong("result", result);
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "funcInt64 failed: " + errorMessage);
						Log.d(TAG, "funcInt64 exception details", e);
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
							Log.e(TAG, "failed to send funcInt64 response: " + e);
						}
					} else {
						Log.w(TAG, "funcInt64: replyTo is null, cannot send response");
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_FuncFloatReq: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        float paramFloat = data.getFloat("paramFloat", 0.0f);
					Message respMsg = new Message();
					respMsg.what = SimpleInterfaceMessageType.RPC_FuncFloatResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						float result =  backend.funcFloat(paramFloat);
						
		        resp_data.putFloat("result", result);
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
				case RPC_FuncFloat32Req: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        float paramFloat32 = data.getFloat("paramFloat32", 0.0f);
					Message respMsg = new Message();
					respMsg.what = SimpleInterfaceMessageType.RPC_FuncFloat32Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						float result =  backend.funcFloat32(paramFloat32);
						
		        resp_data.putFloat("result", result);
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "funcFloat32 failed: " + errorMessage);
						Log.d(TAG, "funcFloat32 exception details", e);
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
							Log.e(TAG, "failed to send funcFloat32 response: " + e);
						}
					} else {
						Log.w(TAG, "funcFloat32: replyTo is null, cannot send response");
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_FuncFloat64Req: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        double paramFloat = data.getDouble("paramFloat", 0.0);
					Message respMsg = new Message();
					respMsg.what = SimpleInterfaceMessageType.RPC_FuncFloat64Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						double result =  backend.funcFloat64(paramFloat);
						
		        resp_data.putDouble("result", result);
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "funcFloat64 failed: " + errorMessage);
						Log.d(TAG, "funcFloat64 exception details", e);
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
							Log.e(TAG, "failed to send funcFloat64 response: " + e);
						}
					} else {
						Log.w(TAG, "funcFloat64: replyTo is null, cannot send response");
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_FuncStringReq: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        String paramString = data.getString("paramString", new String());
					Message respMsg = new Message();
					respMsg.what = SimpleInterfaceMessageType.RPC_FuncStringResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						String result =  backend.funcString(paramString);
						
		        resp_data.putString("result", result);
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
			msg.what = SimpleInterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			ISimpleInterface backend;
			synchronized (SimpleInterfaceServiceAdapter.sBackendMutex)
			{
				backend = SimpleInterfaceServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				
				boolean propBool = backend.getPropBool();
				
		        data.putBoolean("propBool", propBool);
				int propInt = backend.getPropInt();
				
		        data.putInt("propInt", propInt);
				int propInt32 = backend.getPropInt32();
				
		        data.putInt("propInt32", propInt32);
				long propInt64 = backend.getPropInt64();
				
		        data.putLong("propInt64", propInt64);
				float propFloat = backend.getPropFloat();
				
		        data.putFloat("propFloat", propFloat);
				float propFloat32 = backend.getPropFloat32();
				
		        data.putFloat("propFloat32", propFloat32);
				double propFloat64 = backend.getPropFloat64();
				
		        data.putDouble("propFloat64", propFloat64);
				String propString = backend.getPropString();
				
		        data.putString("propString", propString);
				msg.setData(data);
				sendMessageToClients(msg);
			}
		}
		@Override
		public void onPropBoolChanged(boolean propBool){
			Log.i(TAG, "New value for PropBool from backend" + propBool);

			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.SET_PropBool.getValue();
			Bundle data = new Bundle();
			
		        data.putBoolean("propBool", propBool);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropIntChanged(int propInt){
			Log.i(TAG, "New value for PropInt from backend" + propInt);

			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.SET_PropInt.getValue();
			Bundle data = new Bundle();
			
		        data.putInt("propInt", propInt);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropInt32Changed(int propInt32){
			Log.i(TAG, "New value for PropInt32 from backend" + propInt32);

			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.SET_PropInt32.getValue();
			Bundle data = new Bundle();
			
		        data.putInt("propInt32", propInt32);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropInt64Changed(long propInt64){
			Log.i(TAG, "New value for PropInt64 from backend" + propInt64);

			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.SET_PropInt64.getValue();
			Bundle data = new Bundle();
			
		        data.putLong("propInt64", propInt64);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropFloatChanged(float propFloat){
			Log.i(TAG, "New value for PropFloat from backend" + propFloat);

			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.SET_PropFloat.getValue();
			Bundle data = new Bundle();
			
		        data.putFloat("propFloat", propFloat);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropFloat32Changed(float propFloat32){
			Log.i(TAG, "New value for PropFloat32 from backend" + propFloat32);

			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.SET_PropFloat32.getValue();
			Bundle data = new Bundle();
			
		        data.putFloat("propFloat32", propFloat32);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropFloat64Changed(double propFloat64){
			Log.i(TAG, "New value for PropFloat64 from backend" + propFloat64);

			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.SET_PropFloat64.getValue();
			Bundle data = new Bundle();
			
		        data.putDouble("propFloat64", propFloat64);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropStringChanged(String propString){
			Log.i(TAG, "New value for PropString from backend" + propString);

			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.SET_PropString.getValue();
			Bundle data = new Bundle();
			
		        data.putString("propString", propString);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigBool(boolean paramBool){
			Log.i(TAG, "New singal for SigBool = "+ " " + paramBool);
			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.SIG_SigBool.getValue();
			Bundle data = new Bundle();
			
		        data.putBoolean("paramBool", paramBool);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigInt(int paramInt){
			Log.i(TAG, "New singal for SigInt = "+ " " + paramInt);
			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.SIG_SigInt.getValue();
			Bundle data = new Bundle();
			
		        data.putInt("paramInt", paramInt);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigInt32(int paramInt32){
			Log.i(TAG, "New singal for SigInt32 = "+ " " + paramInt32);
			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.SIG_SigInt32.getValue();
			Bundle data = new Bundle();
			
		        data.putInt("paramInt32", paramInt32);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigInt64(long paramInt64){
			Log.i(TAG, "New singal for SigInt64 = "+ " " + paramInt64);
			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.SIG_SigInt64.getValue();
			Bundle data = new Bundle();
			
		        data.putLong("paramInt64", paramInt64);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigFloat(float paramFloat){
			Log.i(TAG, "New singal for SigFloat = "+ " " + paramFloat);
			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.SIG_SigFloat.getValue();
			Bundle data = new Bundle();
			
		        data.putFloat("paramFloat", paramFloat);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigFloat32(float paramFloat32){
			Log.i(TAG, "New singal for SigFloat32 = "+ " " + paramFloat32);
			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.SIG_SigFloat32.getValue();
			Bundle data = new Bundle();
			
		        data.putFloat("paramFloat32", paramFloat32);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigFloat64(double paramFloat64){
			Log.i(TAG, "New singal for SigFloat64 = "+ " " + paramFloat64);
			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.SIG_SigFloat64.getValue();
			Bundle data = new Bundle();
			
		        data.putDouble("paramFloat64", paramFloat64);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigString(String paramString){
			Log.i(TAG, "New singal for SigString = "+ " " + paramString);
			Message msg = new Message();
			msg.what = SimpleInterfaceMessageType.SIG_SigString.getValue();
			Bundle data = new Bundle();
			
		        data.putString("paramString", paramString);
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
