//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package counter.counter_android_service;

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

import counter.counter_api.ICounterEventListener;
import counter.counter_android_service.ICounterServiceProvider;
import counter.counter_api.ICounter;
import counter.counter_api.AbstractCounter;
import counter.counter_api.RemoteOperationException;
import counter.counter_android_messenger.CounterMessageType;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
public class CounterServiceAdapter extends Service
{
	private static final String TAG = "CounterServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	// Lifetime of mBackendService and its accessibility through mServiceProvider is controlled by the backend provide with setService function.
	// The ServiceAdapter is just a user of the backend. 
	// Use provided ServiceStarter classes and the start and stop functions for that.
	private static ICounter mBackendService;
	private static ICounterServiceProvider mServiceProvider;
	private static final Object sBackendMutex = new Object();

	public CounterServiceAdapter()
	{
	}

	public static ICounter setService(ICounterServiceProvider serviceProvider)
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
					Log.i(TAG, "LIFECYCLE: setService(Counter) called. For handler " + mHandler);
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
		Log.i(TAG, "LIFECYCLE: onCreate(CounterService) called. context = " + this);
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
		Log.i(TAG, "LIFECYCLE: CounterService::onStartCommand called. context = " + this +
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
		Log.i(TAG, "LIFECYCLE: onDestroy(CounterService) - proc = " + ", mMessenger = " + mMessenger);
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
	class IncomingHandler extends Handler implements ICounterEventListener
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
			ICounter backend;
			synchronized (CounterServiceAdapter.sBackendMutex)
			{
				backend = CounterServiceAdapter.mBackendService;
			}
			CounterMessageType msgType =
				CounterMessageType.fromInteger(msg.what);
			if (backend == null || !backend._isReady())
			{
				if (msgType != CounterMessageType.REGISTER_CLIENT
					&& msgType != CounterMessageType.UNREGISTER_CLIENT
					&& msgType != CounterMessageType.RPC_IncrementReq
					&& msgType != CounterMessageType.RPC_IncrementArrayReq
					&& msgType != CounterMessageType.RPC_DecrementReq
					&& msgType != CounterMessageType.RPC_DecrementArrayReq)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: CounterMessageType" + msgType );
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
					case PROP_Vector:
					{
						Bundle data = msg.getData();
						data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
						
			        customTypes.customTypes_api.Vector3D vector = data.getParcelable("vector", customTypes.customTypes_android_messenger.Vector3DParcelable.class).getVector3D();
						backend.setVector(vector);
						break;
					}
					case PROP_ExternVector:
					{
						Bundle data = msg.getData();
						data.setClassLoader(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader());
						
			        org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector = data.getParcelable("extern_vector", externTypes.externTypes_android_messenger.MyVector3DParcelable.class).getMyVector3D();
						backend.setExternVector(extern_vector);
						break;
					}
					case PROP_VectorArray:
					{
						Bundle data = msg.getData();
						data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
						
                    customTypes.customTypes_api.Vector3D[] vectorArray =  customTypes.customTypes_android_messenger.Vector3DParcelable.unwrapArray((customTypes.customTypes_android_messenger.Vector3DParcelable[])data.getParcelableArray("vectorArray", customTypes.customTypes_android_messenger.Vector3DParcelable.class));
						backend.setVectorArray(vectorArray);
						break;
					}
					case PROP_ExternVectorArray:
					{
						Bundle data = msg.getData();
						data.setClassLoader(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader());
						
                    org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray =  externTypes.externTypes_android_messenger.MyVector3DParcelable.unwrapArray((externTypes.externTypes_android_messenger.MyVector3DParcelable[])data.getParcelableArray("extern_vectorArray", externTypes.externTypes_android_messenger.MyVector3DParcelable.class));
						backend.setExternVectorArray(extern_vectorArray);
						break;
					}
				case RPC_IncrementReq: {
					Bundle data = msg.getData();
					
        data.setClassLoader(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader());
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec = data.getParcelable("vec", externTypes.externTypes_android_messenger.MyVector3DParcelable.class).getMyVector3D();

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = CounterMessageType.RPC_IncrementResp.getValue();
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
								Log.e(TAG, "failed to send increment not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.incrementAsync(vec).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = CounterMessageType.RPC_IncrementResp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "increment failed: " + errorMessage);
							Log.d(TAG, "increment exception details", cause);
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
							org.apache.commons.math3.geometry.euclidean.threed.Vector3D result = (org.apache.commons.math3.geometry.euclidean.threed.Vector3D) rawResult;
							
		        resp_data.putParcelable("result", new externTypes.externTypes_android_messenger.MyVector3DParcelable(result));
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send increment response: " + e);
							}
						} else {
							Log.w(TAG, "increment: replyTo is null, cannot send response");
						}
					});
					break;
				}
				case RPC_IncrementArrayReq: {
					Bundle data = msg.getData();
					
        data.setClassLoader(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader());
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
                    org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec =  externTypes.externTypes_android_messenger.MyVector3DParcelable.unwrapArray((externTypes.externTypes_android_messenger.MyVector3DParcelable[])data.getParcelableArray("vec", externTypes.externTypes_android_messenger.MyVector3DParcelable.class));

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = CounterMessageType.RPC_IncrementArrayResp.getValue();
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
								Log.e(TAG, "failed to send incrementArray not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.incrementArrayAsync(vec).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = CounterMessageType.RPC_IncrementArrayResp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "incrementArray failed: " + errorMessage);
							Log.d(TAG, "incrementArray exception details", cause);
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
							org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] result = (org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]) rawResult;
							
		        resp_data.putParcelableArray("result",externTypes.externTypes_android_messenger.MyVector3DParcelable.wrapArray(result));
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send incrementArray response: " + e);
							}
						} else {
							Log.w(TAG, "incrementArray: replyTo is null, cannot send response");
						}
					});
					break;
				}
				case RPC_DecrementReq: {
					Bundle data = msg.getData();
					
        data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
			        customTypes.customTypes_api.Vector3D vec = data.getParcelable("vec", customTypes.customTypes_android_messenger.Vector3DParcelable.class).getVector3D();

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = CounterMessageType.RPC_DecrementResp.getValue();
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
								Log.e(TAG, "failed to send decrement not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.decrementAsync(vec).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = CounterMessageType.RPC_DecrementResp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "decrement failed: " + errorMessage);
							Log.d(TAG, "decrement exception details", cause);
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
							customTypes.customTypes_api.Vector3D result = (customTypes.customTypes_api.Vector3D) rawResult;
							
		        resp_data.putParcelable("result", new customTypes.customTypes_android_messenger.Vector3DParcelable(result));
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send decrement response: " + e);
							}
						} else {
							Log.w(TAG, "decrement: replyTo is null, cannot send response");
						}
					});
					break;
				}
				case RPC_DecrementArrayReq: {
					Bundle data = msg.getData();
					
        data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
					final int callId = data.getInt("callId");
					final Messenger replyTo = msg.replyTo;
					
                    customTypes.customTypes_api.Vector3D[] vec =  customTypes.customTypes_android_messenger.Vector3DParcelable.unwrapArray((customTypes.customTypes_android_messenger.Vector3DParcelable[])data.getParcelableArray("vec", customTypes.customTypes_android_messenger.Vector3DParcelable.class));

					// Pre-flight: backend not ready — respond synchronously with error
					if (backend == null || !backend._isReady()) {
						Message respMsg = new Message();
						respMsg.what = CounterMessageType.RPC_DecrementArrayResp.getValue();
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
								Log.e(TAG, "failed to send decrementArray not-ready response: " + e);
							}
						}
						break;
					}

					// Async dispatch — returns immediately, looper is free
					backend.decrementArrayAsync(vec).whenComplete((Object rawResult, Throwable error) -> {
						Message respMsg = new Message();
						respMsg.what = CounterMessageType.RPC_DecrementArrayResp.getValue();
						Bundle resp_data = new Bundle();
						resp_data.putInt("callId", callId);

						if (error != null) {
							Throwable cause = error;
							if (error instanceof java.util.concurrent.CompletionException && error.getCause() != null) {
								cause = error.getCause();
							}
							String errorMessage = cause.getMessage() != null ? cause.getMessage() : cause.getClass().getName();
							Log.w(TAG, "decrementArray failed: " + errorMessage);
							Log.d(TAG, "decrementArray exception details", cause);
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
							customTypes.customTypes_api.Vector3D[] result = (customTypes.customTypes_api.Vector3D[]) rawResult;
							
		        resp_data.putParcelableArray("result",customTypes.customTypes_android_messenger.Vector3DParcelable.wrapArray(result));
						}

						respMsg.setData(resp_data);

						if (replyTo != null) {
							try {
								replyTo.send(respMsg);
							} catch (RemoteException e) {
								Log.e(TAG, "failed to send decrementArray response: " + e);
							}
						} else {
							Log.w(TAG, "decrementArray: replyTo is null, cannot send response");
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
			msg.what = CounterMessageType.INIT.getValue();
			Bundle data = new Bundle();
			ICounter backend;
			synchronized (CounterServiceAdapter.sBackendMutex)
			{
				backend = CounterServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				
				customTypes.customTypes_api.Vector3D vector = backend.getVector();
				
		        data.putParcelable("vector", new customTypes.customTypes_android_messenger.Vector3DParcelable(vector));
				org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector = backend.getExternVector();
				
		        data.putParcelable("extern_vector", new externTypes.externTypes_android_messenger.MyVector3DParcelable(extern_vector));
				customTypes.customTypes_api.Vector3D[] vectorArray = backend.getVectorArray();
				
		        data.putParcelableArray("vectorArray", customTypes.customTypes_android_messenger.Vector3DParcelable.wrapArray(vectorArray));
				org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray = backend.getExternVectorArray();
				
		        data.putParcelableArray("extern_vectorArray", externTypes.externTypes_android_messenger.MyVector3DParcelable.wrapArray(extern_vectorArray));
				msg.setData(data);
				sendMessageToClients(msg);
			}
		}
		@Override
		public void onVectorChanged(customTypes.customTypes_api.Vector3D vector){
			Log.i(TAG, "New value for Vector from backend" + vector);

			Message msg = new Message();
			msg.what = CounterMessageType.SET_Vector.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("vector", new customTypes.customTypes_android_messenger.Vector3DParcelable(vector));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onExternVectorChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector){
			Log.i(TAG, "New value for ExternVector from backend" + extern_vector);

			Message msg = new Message();
			msg.what = CounterMessageType.SET_ExternVector.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("extern_vector", new externTypes.externTypes_android_messenger.MyVector3DParcelable(extern_vector));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onVectorArrayChanged(customTypes.customTypes_api.Vector3D[] vectorArray){
			Log.i(TAG, "New value for VectorArray from backend" + vectorArray);

			Message msg = new Message();
			msg.what = CounterMessageType.SET_VectorArray.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("vectorArray", customTypes.customTypes_android_messenger.Vector3DParcelable.wrapArray(vectorArray));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onExternVectorArrayChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray){
			Log.i(TAG, "New value for ExternVectorArray from backend" + extern_vectorArray);

			Message msg = new Message();
			msg.what = CounterMessageType.SET_ExternVectorArray.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("extern_vectorArray", externTypes.externTypes_android_messenger.MyVector3DParcelable.wrapArray(extern_vectorArray));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onValueChanged(customTypes.customTypes_api.Vector3D vector, org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector, customTypes.customTypes_api.Vector3D[] vectorArray, org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray){
			Log.i(TAG, "New singal for ValueChanged = "+ " " + vector+ " " + extern_vector+ " " + vectorArray+ " " + extern_vectorArray);
			Message msg = new Message();
			msg.what = CounterMessageType.SIG_ValueChanged.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("vector", new customTypes.customTypes_android_messenger.Vector3DParcelable(vector));
			
		        data.putParcelable("extern_vector", new externTypes.externTypes_android_messenger.MyVector3DParcelable(extern_vector));
			
		        data.putParcelableArray("vectorArray", customTypes.customTypes_android_messenger.Vector3DParcelable.wrapArray(vectorArray));
			
		        data.putParcelableArray("extern_vectorArray", externTypes.externTypes_android_messenger.MyVector3DParcelable.wrapArray(extern_vectorArray));
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
