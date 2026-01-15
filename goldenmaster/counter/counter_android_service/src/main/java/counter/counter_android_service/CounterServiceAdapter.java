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
import counter.counter_android_messenger.CounterMessageType;

import java.util.Map;
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
			if (backend == null || !backend._isReady())
			{
				if (CounterMessageType.fromInteger(msg.what) != CounterMessageType.REGISTER_CLIENT
					&& CounterMessageType.fromInteger(msg.what) != CounterMessageType.UNREGISTER_CLIENT)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: CounterMessageType" + CounterMessageType.fromInteger(msg.what) );
					return;
				}
			}
			switch (CounterMessageType.fromInteger(msg.what))
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
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_IncrementReq: {

					Bundle data = msg.getData();
					
    // all structs (even from other modules) are known at compile time (see gradle files) and share same PathClassLoader, any class loader provides access to it.
        data.setClassLoader(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec = data.getParcelable("vec", externTypes.externTypes_android_messenger.MyVector3DParcelable.class).getMyVector3D();
					org.apache.commons.math3.geometry.euclidean.threed.Vector3D result =  backend.increment(vec);

					Message respMsg = new Message();
					respMsg.what = CounterMessageType.RPC_IncrementResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelable("result", new externTypes.externTypes_android_messenger.MyVector3DParcelable(result));
					respMsg.setData(resp_data);

					try {
						msg.replyTo.send(respMsg);
					} catch (RemoteException e) {
						throw new RuntimeException(e);
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_IncrementArrayReq: {

					Bundle data = msg.getData();
					
    // all structs (even from other modules) are known at compile time (see gradle files) and share same PathClassLoader, any class loader provides access to it.
        data.setClassLoader(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
                    org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec =  externTypes.externTypes_android_messenger.MyVector3DParcelable.unwrapArray((externTypes.externTypes_android_messenger.MyVector3DParcelable[])data.getParcelableArray("vec", externTypes.externTypes_android_messenger.MyVector3DParcelable.class));
					org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] result =  backend.incrementArray(vec);

					Message respMsg = new Message();
					respMsg.what = CounterMessageType.RPC_IncrementArrayResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelableArray("result",externTypes.externTypes_android_messenger.MyVector3DParcelable.wrapArray(result));
					respMsg.setData(resp_data);

					try {
						msg.replyTo.send(respMsg);
					} catch (RemoteException e) {
						throw new RuntimeException(e);
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_DecrementReq: {

					Bundle data = msg.getData();
					
    // all structs (even from other modules) are known at compile time (see gradle files) and share same PathClassLoader, any class loader provides access to it.
        data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        customTypes.customTypes_api.Vector3D vec = data.getParcelable("vec", customTypes.customTypes_android_messenger.Vector3DParcelable.class).getVector3D();
					customTypes.customTypes_api.Vector3D result =  backend.decrement(vec);

					Message respMsg = new Message();
					respMsg.what = CounterMessageType.RPC_DecrementResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelable("result", new customTypes.customTypes_android_messenger.Vector3DParcelable(result));
					respMsg.setData(resp_data);

					try {
						msg.replyTo.send(respMsg);
					} catch (RemoteException e) {
						throw new RuntimeException(e);
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_DecrementArrayReq: {

					Bundle data = msg.getData();
					
    // all structs (even from other modules) are known at compile time (see gradle files) and share same PathClassLoader, any class loader provides access to it.
        data.setClassLoader(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
                    customTypes.customTypes_api.Vector3D[] vec =  customTypes.customTypes_android_messenger.Vector3DParcelable.unwrapArray((customTypes.customTypes_android_messenger.Vector3DParcelable[])data.getParcelableArray("vec", customTypes.customTypes_android_messenger.Vector3DParcelable.class));
					customTypes.customTypes_api.Vector3D[] result =  backend.decrementArray(vec);

					Message respMsg = new Message();
					respMsg.what = CounterMessageType.RPC_DecrementArrayResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelableArray("result",customTypes.customTypes_android_messenger.Vector3DParcelable.wrapArray(result));
					respMsg.setData(resp_data);

					try {
						msg.replyTo.send(respMsg);
					} catch (RemoteException e) {
						throw new RuntimeException(e);
					}
					break;

				}
				default:
					Log.e(TAG, "Receive Unsupported message: " + msg.what);
					super.handleMessage(msg);
					break;
				}
		}

		@Override
		protected void finalize() throws Throwable
		{
			super.finalize();
			Log.i(TAG, "LIFECYCLE: IncomingHandler(finalize)");
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
