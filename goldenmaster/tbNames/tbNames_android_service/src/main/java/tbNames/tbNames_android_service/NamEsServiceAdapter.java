//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package tbNames.tbNames_android_service;

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
import tbNames.tbNames_api.EnumWithUnderScores;
import tbNames.tbNames_android_messenger.EnumWithUnderScoresParcelable;

import tbNames.tbNames_api.INamEsEventListener;
import tbNames.tbNames_android_service.INamEsServiceProvider;
import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_api.AbstractNamEs;
import tbNames.tbNames_api.RemoteOperationException;
import tbNames.tbNames_android_messenger.NamEsMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class NamEsServiceAdapter extends Service
{
	private static final String TAG = "NamEsServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	// Lifetime of mBackendService and its accessibility through mServiceProvider is controlled by the backend provide with setService function.
	// The ServiceAdapter is just a user of the backend. 
	// Use provided ServiceStarter classes and the start and stop functions for that.
	private static INamEs mBackendService;
	private static INamEsServiceProvider mServiceProvider;
	private static final Object sBackendMutex = new Object();

	public NamEsServiceAdapter()
	{
	}

	public static INamEs setService(INamEsServiceProvider serviceProvider)
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
					Log.i(TAG, "LIFECYCLE: setService(NamEs) called. For handler " + mHandler);
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
		Log.i(TAG, "LIFECYCLE: onCreate(NamEsService) called. context = " + this);
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
		Log.i(TAG, "LIFECYCLE: NamEsService::onStartCommand called. context = " + this +
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
		Log.i(TAG, "LIFECYCLE: onDestroy(NamEsService) - proc = " + ", mMessenger = " + mMessenger);
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
	class IncomingHandler extends Handler implements INamEsEventListener
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
			INamEs backend;
			synchronized (NamEsServiceAdapter.sBackendMutex)
			{
				backend = NamEsServiceAdapter.mBackendService;
			}
			NamEsMessageType msgType =
				NamEsMessageType.fromInteger(msg.what);
			if (backend == null || !backend._isReady())
			{
				if (msgType != NamEsMessageType.REGISTER_CLIENT
					&& msgType != NamEsMessageType.UNREGISTER_CLIENT
					&& msgType != NamEsMessageType.RPC_SomeFunctionReq
					&& msgType != NamEsMessageType.RPC_SomeFunction2Req)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: NamEsMessageType" + msgType );
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
					case PROP_Switch:
					{
						Bundle data = msg.getData();
						
			        boolean Switch = data.getBoolean("Switch", false);
						backend.setSwitch(Switch);
						break;
					}
					case PROP_SomeProperty:
					{
						Bundle data = msg.getData();
						
			        int SOME_PROPERTY = data.getInt("SOME_PROPERTY", 0);
						backend.setSomeProperty(SOME_PROPERTY);
						break;
					}
					case PROP_SomePoperty2:
					{
						Bundle data = msg.getData();
						
			        int Some_Poperty2 = data.getInt("Some_Poperty2", 0);
						backend.setSomePoperty2(Some_Poperty2);
						break;
					}
					case PROP_EnumProperty:
					{
						Bundle data = msg.getData();
						data.setClassLoader(EnumWithUnderScoresParcelable.class.getClassLoader());
						
			        EnumWithUnderScores enum_property = data.getParcelable("enum_property", EnumWithUnderScoresParcelable.class).getEnumWithUnderScores();
						backend.setEnumProperty(enum_property);
						break;
					}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_SomeFunctionReq: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        boolean SOME_PARAM = data.getBoolean("SOME_PARAM", false);
					Message respMsg = new Message();
					respMsg.what = NamEsMessageType.RPC_SomeFunctionResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						 backend.someFunction(SOME_PARAM);
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "SOME_FUNCTION failed: " + errorMessage);
						Log.d(TAG, "SOME_FUNCTION exception details", e);
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
							Log.e(TAG, "failed to send SOME_FUNCTION response: " + e);
						}
					} else {
						Log.w(TAG, "SOME_FUNCTION: replyTo is null, cannot send response");
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_SomeFunction2Req: {

					Bundle data = msg.getData();
					
					int callId = data.getInt("callId");
					
			        boolean Some_Param = data.getBoolean("Some_Param", false);
					Message respMsg = new Message();
					respMsg.what = NamEsMessageType.RPC_SomeFunction2Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						 backend.someFunction2(Some_Param);
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "Some_Function2 failed: " + errorMessage);
						Log.d(TAG, "Some_Function2 exception details", e);
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
							Log.e(TAG, "failed to send Some_Function2 response: " + e);
						}
					} else {
						Log.w(TAG, "Some_Function2: replyTo is null, cannot send response");
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
			msg.what = NamEsMessageType.INIT.getValue();
			Bundle data = new Bundle();
			INamEs backend;
			synchronized (NamEsServiceAdapter.sBackendMutex)
			{
				backend = NamEsServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				
				boolean Switch = backend.getSwitch();
				
		        data.putBoolean("Switch", Switch);
				int SOME_PROPERTY = backend.getSomeProperty();
				
		        data.putInt("SOME_PROPERTY", SOME_PROPERTY);
				int Some_Poperty2 = backend.getSomePoperty2();
				
		        data.putInt("Some_Poperty2", Some_Poperty2);
				EnumWithUnderScores enum_property = backend.getEnumProperty();
				
		        data.putParcelable("enum_property", new EnumWithUnderScoresParcelable(enum_property));
				msg.setData(data);
				sendMessageToClients(msg);
			}
		}
		@Override
		public void onSwitchChanged(boolean Switch){
			Log.i(TAG, "New value for Switch from backend" + Switch);

			Message msg = new Message();
			msg.what = NamEsMessageType.SET_Switch.getValue();
			Bundle data = new Bundle();
			
		        data.putBoolean("Switch", Switch);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSomePropertyChanged(int SOME_PROPERTY){
			Log.i(TAG, "New value for SomeProperty from backend" + SOME_PROPERTY);

			Message msg = new Message();
			msg.what = NamEsMessageType.SET_SomeProperty.getValue();
			Bundle data = new Bundle();
			
		        data.putInt("SOME_PROPERTY", SOME_PROPERTY);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSomePoperty2Changed(int Some_Poperty2){
			Log.i(TAG, "New value for SomePoperty2 from backend" + Some_Poperty2);

			Message msg = new Message();
			msg.what = NamEsMessageType.SET_SomePoperty2.getValue();
			Bundle data = new Bundle();
			
		        data.putInt("Some_Poperty2", Some_Poperty2);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onEnumPropertyChanged(EnumWithUnderScores enum_property){
			Log.i(TAG, "New value for EnumProperty from backend" + enum_property);

			Message msg = new Message();
			msg.what = NamEsMessageType.SET_EnumProperty.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("enum_property", new EnumWithUnderScoresParcelable(enum_property));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSomeSignal(boolean SOME_PARAM){
			Log.i(TAG, "New singal for SomeSignal = "+ " " + SOME_PARAM);
			Message msg = new Message();
			msg.what = NamEsMessageType.SIG_SomeSignal.getValue();
			Bundle data = new Bundle();
			
		        data.putBoolean("SOME_PARAM", SOME_PARAM);
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onSomeSignal2(boolean Some_Param){
			Log.i(TAG, "New singal for SomeSignal2 = "+ " " + Some_Param);
			Message msg = new Message();
			msg.what = NamEsMessageType.SIG_SomeSignal2.getValue();
			Bundle data = new Bundle();
			
		        data.putBoolean("Some_Param", Some_Param);
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
