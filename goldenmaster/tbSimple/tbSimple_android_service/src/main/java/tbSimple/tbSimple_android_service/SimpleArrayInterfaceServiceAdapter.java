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
import tbSimple.tbSimple_android_messenger.Conversions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
						
			        List<Boolean> propBool = Conversions.toList(data.getBooleanArray("propBool"));
						backend.setPropBool(propBool);
						break;
					}
					case PROP_PropInt:
					{
						Bundle data = msg.getData();
						
			        List<Integer> propInt = Conversions.toList(data.getIntArray("propInt"));
						backend.setPropInt(propInt);
						break;
					}
					case PROP_PropInt32:
					{
						Bundle data = msg.getData();
						
			        List<Integer> propInt32 = Conversions.toList(data.getIntArray("propInt32"));
						backend.setPropInt32(propInt32);
						break;
					}
					case PROP_PropInt64:
					{
						Bundle data = msg.getData();
						
			        List<Long> propInt64 = Conversions.toList(data.getLongArray("propInt64"));
						backend.setPropInt64(propInt64);
						break;
					}
					case PROP_PropFloat:
					{
						Bundle data = msg.getData();
						
			        List<Float> propFloat = Conversions.toList(data.getFloatArray("propFloat"));
						backend.setPropFloat(propFloat);
						break;
					}
					case PROP_PropFloat32:
					{
						Bundle data = msg.getData();
						
			        List<Float> propFloat32 = Conversions.toList(data.getFloatArray("propFloat32"));
						backend.setPropFloat32(propFloat32);
						break;
					}
					case PROP_PropFloat64:
					{
						Bundle data = msg.getData();
						
			        List<Double> propFloat64 = Conversions.toList(data.getDoubleArray("propFloat64"));
						backend.setPropFloat64(propFloat64);
						break;
					}
					case PROP_PropString:
					{
						Bundle data = msg.getData();
						
			        List<String> propString = Conversions.toList(data.getStringArray("propString"));
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
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_FuncBoolReq: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        List<Boolean> paramBool = Conversions.toList(data.getBooleanArray("paramBool"));
					Message respMsg = new Message();
					respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncBoolResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						List<Boolean> result =  backend.funcBool(paramBool);
						
		        resp_data.putBooleanArray("result", Conversions.toArray(result, new boolean[0]));
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
					
			        List<Integer> paramInt = Conversions.toList(data.getIntArray("paramInt"));
					Message respMsg = new Message();
					respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncIntResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						List<Integer> result =  backend.funcInt(paramInt);
						
		        resp_data.putIntArray("result", Conversions.toArray(result, new int[0]));
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
					
			        List<Integer> paramInt32 = Conversions.toList(data.getIntArray("paramInt32"));
					Message respMsg = new Message();
					respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncInt32Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						List<Integer> result =  backend.funcInt32(paramInt32);
						
		        resp_data.putIntArray("result", Conversions.toArray(result, new int[0]));
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
					
			        List<Long> paramInt64 = Conversions.toList(data.getLongArray("paramInt64"));
					Message respMsg = new Message();
					respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncInt64Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						List<Long> result =  backend.funcInt64(paramInt64);
						
		        resp_data.putLongArray("result", Conversions.toArray(result, new long[0]));
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
					
			        List<Float> paramFloat = Conversions.toList(data.getFloatArray("paramFloat"));
					Message respMsg = new Message();
					respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncFloatResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						List<Float> result =  backend.funcFloat(paramFloat);
						
		        resp_data.putFloatArray("result", Conversions.toArray(result, new float[0]));
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
					
			        List<Float> paramFloat32 = Conversions.toList(data.getFloatArray("paramFloat32"));
					Message respMsg = new Message();
					respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncFloat32Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						List<Float> result =  backend.funcFloat32(paramFloat32);
						
		        resp_data.putFloatArray("result", Conversions.toArray(result, new float[0]));
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
					
			        List<Double> paramFloat = Conversions.toList(data.getDoubleArray("paramFloat"));
					Message respMsg = new Message();
					respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncFloat64Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						List<Double> result =  backend.funcFloat64(paramFloat);
						
		        resp_data.putDoubleArray("result", Conversions.toArray(result, new double[0]));
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
					
			        List<String> paramString = Conversions.toList(data.getStringArray("paramString"));
					Message respMsg = new Message();
					respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncStringResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						List<String> result =  backend.funcString(paramString);
						
		        resp_data.putStringArray("result", Conversions.toArray(result, new String[0]));
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
			msg.what = SimpleArrayInterfaceMessageType.INIT.getValue();
			Bundle data = new Bundle();
			ISimpleArrayInterface backend;
			synchronized (SimpleArrayInterfaceServiceAdapter.sBackendMutex)
			{
				backend = SimpleArrayInterfaceServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				
				List<Boolean> propBool = backend.getPropBool();
				
		        data.putBooleanArray("propBool", Conversions.toArray(propBool, new boolean[0]));
				List<Integer> propInt = backend.getPropInt();
				
		        data.putIntArray("propInt", Conversions.toArray(propInt, new int[0]));
				List<Integer> propInt32 = backend.getPropInt32();
				
		        data.putIntArray("propInt32", Conversions.toArray(propInt32, new int[0]));
				List<Long> propInt64 = backend.getPropInt64();
				
		        data.putLongArray("propInt64", Conversions.toArray(propInt64, new long[0]));
				List<Float> propFloat = backend.getPropFloat();
				
		        data.putFloatArray("propFloat", Conversions.toArray(propFloat, new float[0]));
				List<Float> propFloat32 = backend.getPropFloat32();
				
		        data.putFloatArray("propFloat32", Conversions.toArray(propFloat32, new float[0]));
				List<Double> propFloat64 = backend.getPropFloat64();
				
		        data.putDoubleArray("propFloat64", Conversions.toArray(propFloat64, new double[0]));
				List<String> propString = backend.getPropString();
				
		        data.putStringArray("propString", Conversions.toArray(propString, new String[0]));
				String propReadOnlyString = backend.getPropReadOnlyString();
				
		        data.putString("propReadOnlyString", propReadOnlyString);
				msg.setData(data);
				sendMessageToClients(msg);
			}
		}
		@Override
		public void onPropBoolChanged(List<Boolean> propBool){
			Log.i(TAG, "New value for PropBool from backend" + propBool);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropBool.getValue();
			Bundle data = new Bundle();
			
		        data.putBooleanArray("propBool", Conversions.toArray(propBool, new boolean[0]));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropIntChanged(List<Integer> propInt){
			Log.i(TAG, "New value for PropInt from backend" + propInt);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropInt.getValue();
			Bundle data = new Bundle();
			
		        data.putIntArray("propInt", Conversions.toArray(propInt, new int[0]));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropInt32Changed(List<Integer> propInt32){
			Log.i(TAG, "New value for PropInt32 from backend" + propInt32);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropInt32.getValue();
			Bundle data = new Bundle();
			
		        data.putIntArray("propInt32", Conversions.toArray(propInt32, new int[0]));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropInt64Changed(List<Long> propInt64){
			Log.i(TAG, "New value for PropInt64 from backend" + propInt64);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropInt64.getValue();
			Bundle data = new Bundle();
			
		        data.putLongArray("propInt64", Conversions.toArray(propInt64, new long[0]));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropFloatChanged(List<Float> propFloat){
			Log.i(TAG, "New value for PropFloat from backend" + propFloat);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropFloat.getValue();
			Bundle data = new Bundle();
			
		        data.putFloatArray("propFloat", Conversions.toArray(propFloat, new float[0]));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropFloat32Changed(List<Float> propFloat32){
			Log.i(TAG, "New value for PropFloat32 from backend" + propFloat32);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropFloat32.getValue();
			Bundle data = new Bundle();
			
		        data.putFloatArray("propFloat32", Conversions.toArray(propFloat32, new float[0]));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropFloat64Changed(List<Double> propFloat64){
			Log.i(TAG, "New value for PropFloat64 from backend" + propFloat64);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropFloat64.getValue();
			Bundle data = new Bundle();
			
		        data.putDoubleArray("propFloat64", Conversions.toArray(propFloat64, new double[0]));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onPropStringChanged(List<String> propString){
			Log.i(TAG, "New value for PropString from backend" + propString);

			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SET_PropString.getValue();
			Bundle data = new Bundle();
			
		        data.putStringArray("propString", Conversions.toArray(propString, new String[0]));
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
		public void onSigBool(List<Boolean> paramBool){
			Log.i(TAG, "New singal for SigBool = "+ " " + paramBool);
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SIG_SigBool.getValue();
			Bundle data = new Bundle();
			
		        data.putBooleanArray("paramBool", Conversions.toArray(paramBool, new boolean[0]));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigInt(List<Integer> paramInt){
			Log.i(TAG, "New singal for SigInt = "+ " " + paramInt);
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SIG_SigInt.getValue();
			Bundle data = new Bundle();
			
		        data.putIntArray("paramInt", Conversions.toArray(paramInt, new int[0]));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigInt32(List<Integer> paramInt32){
			Log.i(TAG, "New singal for SigInt32 = "+ " " + paramInt32);
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SIG_SigInt32.getValue();
			Bundle data = new Bundle();
			
		        data.putIntArray("paramInt32", Conversions.toArray(paramInt32, new int[0]));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigInt64(List<Long> paramInt64){
			Log.i(TAG, "New singal for SigInt64 = "+ " " + paramInt64);
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SIG_SigInt64.getValue();
			Bundle data = new Bundle();
			
		        data.putLongArray("paramInt64", Conversions.toArray(paramInt64, new long[0]));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigFloat(List<Float> paramFloat){
			Log.i(TAG, "New singal for SigFloat = "+ " " + paramFloat);
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SIG_SigFloat.getValue();
			Bundle data = new Bundle();
			
		        data.putFloatArray("paramFloat", Conversions.toArray(paramFloat, new float[0]));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigFloat32(List<Float> paramFloa32){
			Log.i(TAG, "New singal for SigFloat32 = "+ " " + paramFloa32);
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SIG_SigFloat32.getValue();
			Bundle data = new Bundle();
			
		        data.putFloatArray("paramFloa32", Conversions.toArray(paramFloa32, new float[0]));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigFloat64(List<Double> paramFloat64){
			Log.i(TAG, "New singal for SigFloat64 = "+ " " + paramFloat64);
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SIG_SigFloat64.getValue();
			Bundle data = new Bundle();
			
		        data.putDoubleArray("paramFloat64", Conversions.toArray(paramFloat64, new double[0]));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSigString(List<String> paramString){
			Log.i(TAG, "New singal for SigString = "+ " " + paramString);
			Message msg = new Message();
			msg.what = SimpleArrayInterfaceMessageType.SIG_SigString.getValue();
			Bundle data = new Bundle();
			
		        data.putStringArray("paramString", Conversions.toArray(paramString, new String[0]));
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
