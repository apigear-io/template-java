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
import tbSimple.tbSimple_android_messenger.SimpleArrayInterfaceMessageType;

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
			if (backend == null || !backend._isReady())
			{
				if (SimpleArrayInterfaceMessageType.fromInteger(msg.what) != SimpleArrayInterfaceMessageType.REGISTER_CLIENT
					&& SimpleArrayInterfaceMessageType.fromInteger(msg.what) != SimpleArrayInterfaceMessageType.UNREGISTER_CLIENT)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: SimpleArrayInterfaceMessageType" + SimpleArrayInterfaceMessageType.fromInteger(msg.what) );
					return;
				}
			}
			switch (SimpleArrayInterfaceMessageType.fromInteger(msg.what))
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
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_FuncBoolReq: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        boolean[] paramBool = data.getBooleanArray("paramBool");
					boolean[] result =  backend.funcBool(paramBool);

					Message respMsg = new Message();
					respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncBoolResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putBooleanArray("result", result);
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
				case RPC_FuncIntReq: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        int[] paramInt = data.getIntArray("paramInt");
					int[] result =  backend.funcInt(paramInt);

					Message respMsg = new Message();
					respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncIntResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putIntArray("result", result);
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
				case RPC_FuncInt32Req: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        int[] paramInt32 = data.getIntArray("paramInt32");
					int[] result =  backend.funcInt32(paramInt32);

					Message respMsg = new Message();
					respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncInt32Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putIntArray("result", result);
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
				case RPC_FuncInt64Req: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        long[] paramInt64 = data.getLongArray("paramInt64");
					long[] result =  backend.funcInt64(paramInt64);

					Message respMsg = new Message();
					respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncInt64Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putLongArray("result", result);
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
				case RPC_FuncFloatReq: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        float[] paramFloat = data.getFloatArray("paramFloat");
					float[] result =  backend.funcFloat(paramFloat);

					Message respMsg = new Message();
					respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncFloatResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putFloatArray("result", result);
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
				case RPC_FuncFloat32Req: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        float[] paramFloat32 = data.getFloatArray("paramFloat32");
					float[] result =  backend.funcFloat32(paramFloat32);

					Message respMsg = new Message();
					respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncFloat32Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putFloatArray("result", result);
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
				case RPC_FuncFloat64Req: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        double[] paramFloat = data.getDoubleArray("paramFloat");
					double[] result =  backend.funcFloat64(paramFloat);

					Message respMsg = new Message();
					respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncFloat64Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putDoubleArray("result", result);
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
				case RPC_FuncStringReq: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        String[] paramString = data.getStringArray("paramString");
					String[] result =  backend.funcString(paramString);

					Message respMsg = new Message();
					respMsg.what = SimpleArrayInterfaceMessageType.RPC_FuncStringResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putStringArray("result", result);
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
