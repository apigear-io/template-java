//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package tbRefIfaces.tbRefIfaces_android_service;

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
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_android_messenger.SimpleLocalIfParcelable;

import tbRefIfaces.tbRefIfaces_api.IParentIfEventListener;
import tbRefIfaces.tbRefIfaces_android_service.IParentIfServiceProvider;
import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_api.AbstractParentIf;
import tbRefIfaces.tbRefIfaces_api.RemoteOperationException;
import tbRefIfaces.tbRefIfaces_android_messenger.ParentIfMessageType;
import tbRefIfaces.tbRefIfaces_android_messenger.Conversions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class ParentIfServiceAdapter extends Service
{
	private static final String TAG = "ParentIfServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	// Lifetime of mBackendService and its accessibility through mServiceProvider is controlled by the backend provide with setService function.
	// The ServiceAdapter is just a user of the backend. 
	// Use provided ServiceStarter classes and the start and stop functions for that.
	private static IParentIf mBackendService;
	private static IParentIfServiceProvider mServiceProvider;
	private static final Object sBackendMutex = new Object();

	public ParentIfServiceAdapter()
	{
	}

	public static IParentIf setService(IParentIfServiceProvider serviceProvider)
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
					Log.i(TAG, "LIFECYCLE: setService(ParentIf) called. For handler " + mHandler);
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
		Log.i(TAG, "LIFECYCLE: onCreate(ParentIfService) called. context = " + this);
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
		Log.i(TAG, "LIFECYCLE: ParentIfService::onStartCommand called. context = " + this +
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
		Log.i(TAG, "LIFECYCLE: onDestroy(ParentIfService) - proc = " + ", mMessenger = " + mMessenger);
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
	class IncomingHandler extends Handler implements IParentIfEventListener
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
			IParentIf backend;
			synchronized (ParentIfServiceAdapter.sBackendMutex)
			{
				backend = ParentIfServiceAdapter.mBackendService;
			}
			ParentIfMessageType msgType =
				ParentIfMessageType.fromInteger(msg.what);
			if (backend == null || !backend._isReady())
			{
				if (msgType != ParentIfMessageType.REGISTER_CLIENT
					&& msgType != ParentIfMessageType.UNREGISTER_CLIENT
					&& msgType != ParentIfMessageType.RPC_LocalIfMethodReq
					&& msgType != ParentIfMessageType.RPC_LocalIfMethodListReq
					&& msgType != ParentIfMessageType.RPC_ImportedIfMethodReq
					&& msgType != ParentIfMessageType.RPC_ImportedIfMethodListReq)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: ParentIfMessageType" + msgType );
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
					case PROP_LocalIf:
					{
						Bundle data = msg.getData();
						data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
						
			        ISimpleLocalIf localIf = data.getParcelable("localIf", SimpleLocalIfParcelable.class).getSimpleLocalIf();
						backend.setLocalIf(localIf);
						break;
					}
					case PROP_LocalIfList:
					{
						Bundle data = msg.getData();
						data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
						
                    List<ISimpleLocalIf> localIfList = Conversions.toList(SimpleLocalIfParcelable.unwrapArray((SimpleLocalIfParcelable[])data.getParcelableArray("localIfList", SimpleLocalIfParcelable.class)));
						backend.setLocalIfList(localIfList);
						break;
					}
					case PROP_ImportedIf:
					{
						Bundle data = msg.getData();
						data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
						
			        tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf = data.getParcelable("importedIf", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class).getEmptyIf();
						backend.setImportedIf(importedIf);
						break;
					}
					case PROP_ImportedIfList:
					{
						Bundle data = msg.getData();
						data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
						
                    List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> importedIfList = Conversions.toList(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.unwrapArray((tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable[])data.getParcelableArray("importedIfList", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class)));
						backend.setImportedIfList(importedIfList);
						break;
					}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_LocalIfMethodReq: {

					Bundle data = msg.getData();
					
        data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        ISimpleLocalIf param = data.getParcelable("param", SimpleLocalIfParcelable.class).getSimpleLocalIf();
					Message respMsg = new Message();
					respMsg.what = ParentIfMessageType.RPC_LocalIfMethodResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						ISimpleLocalIf result =  backend.localIfMethod(param);
						
		        resp_data.putParcelable("result", new SimpleLocalIfParcelable(result));
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "localIfMethod failed: " + errorMessage);
						Log.d(TAG, "localIfMethod exception details", e);
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
							Log.e(TAG, "failed to send localIfMethod response: " + e);
						}
					} else {
						Log.w(TAG, "localIfMethod: replyTo is null, cannot send response");
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_LocalIfMethodListReq: {

					Bundle data = msg.getData();
					
        data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
                    List<ISimpleLocalIf> param = Conversions.toList(SimpleLocalIfParcelable.unwrapArray((SimpleLocalIfParcelable[])data.getParcelableArray("param", SimpleLocalIfParcelable.class)));
					Message respMsg = new Message();
					respMsg.what = ParentIfMessageType.RPC_LocalIfMethodListResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						List<ISimpleLocalIf> result =  backend.localIfMethodList(param);
						
		        resp_data.putParcelableArray("result",SimpleLocalIfParcelable.wrapArray(Conversions.toArray(result, new ISimpleLocalIf[0])));
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "localIfMethodList failed: " + errorMessage);
						Log.d(TAG, "localIfMethodList exception details", e);
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
							Log.e(TAG, "failed to send localIfMethodList response: " + e);
						}
					} else {
						Log.w(TAG, "localIfMethodList: replyTo is null, cannot send response");
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_ImportedIfMethodReq: {

					Bundle data = msg.getData();
					
        data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        tbIfaceimport.tbIfaceimport_api.IEmptyIf param = data.getParcelable("param", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class).getEmptyIf();
					Message respMsg = new Message();
					respMsg.what = ParentIfMessageType.RPC_ImportedIfMethodResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						tbIfaceimport.tbIfaceimport_api.IEmptyIf result =  backend.importedIfMethod(param);
						
		        resp_data.putParcelable("result", new tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable(result));
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "importedIfMethod failed: " + errorMessage);
						Log.d(TAG, "importedIfMethod exception details", e);
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
							Log.e(TAG, "failed to send importedIfMethod response: " + e);
						}
					} else {
						Log.w(TAG, "importedIfMethod: replyTo is null, cannot send response");
					}
					break;

				}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_ImportedIfMethodListReq: {

					Bundle data = msg.getData();
					
        data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
                    List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> param = Conversions.toList(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.unwrapArray((tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable[])data.getParcelableArray("param", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class)));
					Message respMsg = new Message();
					respMsg.what = ParentIfMessageType.RPC_ImportedIfMethodListResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);

					try {
						if (backend == null || !backend._isReady()) {
							throw new RemoteOperationException("service not ready",
								RemoteOperationException.ERROR_SERVICE_NOT_READY);
						}
						List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> result =  backend.importedIfMethodList(param);
						
		        resp_data.putParcelableArray("result",tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(Conversions.toArray(result, new tbIfaceimport.tbIfaceimport_api.IEmptyIf[0])));
					} catch (Exception e) {
						String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
						Log.w(TAG, "importedIfMethodList failed: " + errorMessage);
						Log.d(TAG, "importedIfMethodList exception details", e);
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
							Log.e(TAG, "failed to send importedIfMethodList response: " + e);
						}
					} else {
						Log.w(TAG, "importedIfMethodList: replyTo is null, cannot send response");
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
			msg.what = ParentIfMessageType.INIT.getValue();
			Bundle data = new Bundle();
			IParentIf backend;
			synchronized (ParentIfServiceAdapter.sBackendMutex)
			{
				backend = ParentIfServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				
				ISimpleLocalIf localIf = backend.getLocalIf();
				
		        data.putParcelable("localIf", new SimpleLocalIfParcelable(localIf));
				List<ISimpleLocalIf> localIfList = backend.getLocalIfList();
				
		        data.putParcelableArray("localIfList", SimpleLocalIfParcelable.wrapArray(Conversions.toArray(localIfList, new ISimpleLocalIf[0])));
				tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf = backend.getImportedIf();
				
		        data.putParcelable("importedIf", new tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable(importedIf));
				List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> importedIfList = backend.getImportedIfList();
				
		        data.putParcelableArray("importedIfList", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(Conversions.toArray(importedIfList, new tbIfaceimport.tbIfaceimport_api.IEmptyIf[0])));
				msg.setData(data);
				sendMessageToClients(msg);
			}
		}
		@Override
		public void onLocalIfChanged(ISimpleLocalIf localIf){
			Log.i(TAG, "New value for LocalIf from backend" + localIf);

			Message msg = new Message();
			msg.what = ParentIfMessageType.SET_LocalIf.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("localIf", new SimpleLocalIfParcelable(localIf));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onLocalIfListChanged(List<ISimpleLocalIf> localIfList){
			Log.i(TAG, "New value for LocalIfList from backend" + localIfList);

			Message msg = new Message();
			msg.what = ParentIfMessageType.SET_LocalIfList.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("localIfList", SimpleLocalIfParcelable.wrapArray(Conversions.toArray(localIfList, new ISimpleLocalIf[0])));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onImportedIfChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf){
			Log.i(TAG, "New value for ImportedIf from backend" + importedIf);

			Message msg = new Message();
			msg.what = ParentIfMessageType.SET_ImportedIf.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("importedIf", new tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable(importedIf));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onImportedIfListChanged(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> importedIfList){
			Log.i(TAG, "New value for ImportedIfList from backend" + importedIfList);

			Message msg = new Message();
			msg.what = ParentIfMessageType.SET_ImportedIfList.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("importedIfList", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(Conversions.toArray(importedIfList, new tbIfaceimport.tbIfaceimport_api.IEmptyIf[0])));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onLocalIfSignal(ISimpleLocalIf param){
			Log.i(TAG, "New singal for LocalIfSignal = "+ " " + param);
			Message msg = new Message();
			msg.what = ParentIfMessageType.SIG_LocalIfSignal.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("param", new SimpleLocalIfParcelable(param));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onLocalIfSignalList(List<ISimpleLocalIf> param){
			Log.i(TAG, "New singal for LocalIfSignalList = "+ " " + param);
			Message msg = new Message();
			msg.what = ParentIfMessageType.SIG_LocalIfSignalList.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("param", SimpleLocalIfParcelable.wrapArray(Conversions.toArray(param, new ISimpleLocalIf[0])));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onImportedIfSignal(tbIfaceimport.tbIfaceimport_api.IEmptyIf param){
			Log.i(TAG, "New singal for ImportedIfSignal = "+ " " + param);
			Message msg = new Message();
			msg.what = ParentIfMessageType.SIG_ImportedIfSignal.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelable("param", new tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable(param));
			msg.setData(data);
			sendMessageToClients(msg);
		}
		@Override
		public void onImportedIfSignalList(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> param){
			Log.i(TAG, "New singal for ImportedIfSignalList = "+ " " + param);
			Message msg = new Message();
			msg.what = ParentIfMessageType.SIG_ImportedIfSignalList.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("param", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(Conversions.toArray(param, new tbIfaceimport.tbIfaceimport_api.IEmptyIf[0])));
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
