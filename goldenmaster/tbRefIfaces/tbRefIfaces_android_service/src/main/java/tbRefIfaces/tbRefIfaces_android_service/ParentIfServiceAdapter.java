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
import tbRefIfaces.tbRefIfaces_android_service.IParentIfServiceFactory;
import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_api.AbstractParentIf;
import tbRefIfaces.tbRefIfaces_android_messenger.ParentIfMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class ParentIfServiceAdapter extends Service
{
	private static final String TAG = "ParentIfServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private static Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	private static IParentIf mBackendService;
	private static IParentIfServiceFactory mServiceFactory;
	private static final Object mutex = new Object();

	//private final List<Message> mMessagesQueue = new ArrayList<>();

	public ParentIfServiceAdapter()
	{
	}

	public static IParentIf setService(IParentIfServiceFactory factory)
	{
		Log.i(TAG, "Setting factory: " + factory);
		if (mServiceFactory  != factory)
		{
			mServiceFactory = factory;
		}
		synchronized (mutex)
		{
			if (mHandler != null && mBackendService != null)
			{
				// remove old event listener (backend is about to change)
				mBackendService.removeEventListener(mHandler);
			}
			mBackendService = mServiceFactory.getServiceInstance();
			if (mHandler != null)
			{
				Log.i(TAG, "LIFECYCLE: setService(ParentIf) called. For handler " + mHandler);
				mBackendService.addEventListener(mHandler);
			}
		}
		return mBackendService;
	}


	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.i(TAG, "LIFECYCLE: onCreate(ParentIfService) called. context = " + this);
		synchronized (mutex) {
			if (mHandler != null && mBackendService != null)
			{
				// The handler (event listener) is about to change.
				mBackendService.removeEventListener(mHandler);
			}
			if (mHandler != null)
			{
				mHandler.removeCallbacksAndMessages(null);
			}
			mHandler = new IncomingHandler(this);
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

		return START_STICKY;
	}

	// execution of the service will stop on calling this method
	@Override
	public void onDestroy()
	{
		Log.i(TAG, "LIFECYCLE: onDestroy(ParentIfService) - proc = " + ", mMessenger = " + mMessenger);

		if (mHandler != null)
		{
			if (mBackendService != null)
			{
				mBackendService.removeEventListener(mHandler);
			}
			mHandler.removeCallbacksAndMessages(null);
			mHandler = null;
		}
		mBackendService = null;
		mMessenger = null;

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
		private final Service mApplicationContext;
		private final ConcurrentHashMap<String, Messenger> mClients = new ConcurrentHashMap<>();

		IncomingHandler(Service context)
		{
			super(Looper.getMainLooper());
			mApplicationContext = context;
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
			if (mBackendService == null || !mBackendService._isReady())
			{
				if (ParentIfMessageType.fromInteger(msg.what) != ParentIfMessageType.REGISTER_CLIENT
					&& ParentIfMessageType.fromInteger(msg.what) != ParentIfMessageType.UNREGISTER_CLIENT)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: ParentIfMessageType" + ParentIfMessageType.fromInteger(msg.what) );
					return;
				}
			}
			switch (ParentIfMessageType.fromInteger(msg.what))
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
						mBackendService.setLocalIf(localIf);
						break;
					}
					case PROP_LocalIfList:
					{
						Bundle data = msg.getData();
						data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
						
                    ISimpleLocalIf[] localIfList =  SimpleLocalIfParcelable.unwrapArray((SimpleLocalIfParcelable[])data.getParcelableArray("localIfList", SimpleLocalIfParcelable.class));
						mBackendService.setLocalIfList(localIfList);
						break;
					}
					case PROP_ImportedIf:
					{
						Bundle data = msg.getData();
						data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
						
			        tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf = data.getParcelable("importedIf", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class).getEmptyIf();
						mBackendService.setImportedIf(importedIf);
						break;
					}
					case PROP_ImportedIfList:
					{
						Bundle data = msg.getData();
						data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
						
                    tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfList =  tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.unwrapArray((tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable[])data.getParcelableArray("importedIfList", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class));
						mBackendService.setImportedIfList(importedIfList);
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

					ISimpleLocalIf result =  mBackendService.localIfMethod(param);

					Message respMsg = new Message();
					respMsg.what = ParentIfMessageType.RPC_LocalIfMethodResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelable("result", new SimpleLocalIfParcelable(result));
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
				case RPC_LocalIfMethodListReq: {

					Bundle data = msg.getData();
					
        data.setClassLoader(SimpleLocalIfParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
                    ISimpleLocalIf[] param =  SimpleLocalIfParcelable.unwrapArray((SimpleLocalIfParcelable[])data.getParcelableArray("param", SimpleLocalIfParcelable.class));

					ISimpleLocalIf[] result =  mBackendService.localIfMethodList(param);

					Message respMsg = new Message();
					respMsg.what = ParentIfMessageType.RPC_LocalIfMethodListResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelableArray("result",SimpleLocalIfParcelable.wrapArray(result));
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
				case RPC_ImportedIfMethodReq: {

					Bundle data = msg.getData();
					
    // all structs (even from other modules) are known at compile time (see gradle files) and share same PathClassLoader, any class loader provides access to it.
        data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
			        tbIfaceimport.tbIfaceimport_api.IEmptyIf param = data.getParcelable("param", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class).getEmptyIf();

					tbIfaceimport.tbIfaceimport_api.IEmptyIf result =  mBackendService.importedIfMethod(param);

					Message respMsg = new Message();
					respMsg.what = ParentIfMessageType.RPC_ImportedIfMethodResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelable("result", new tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable(result));
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
				case RPC_ImportedIfMethodListReq: {

					Bundle data = msg.getData();
					
    // all structs (even from other modules) are known at compile time (see gradle files) and share same PathClassLoader, any class loader provides access to it.
        data.setClassLoader(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader());
					int callId = data.getInt("callId");
					
                    tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param =  tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.unwrapArray((tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable[])data.getParcelableArray("param", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class));

					tbIfaceimport.tbIfaceimport_api.IEmptyIf[] result =  mBackendService.importedIfMethodList(param);

					Message respMsg = new Message();
					respMsg.what = ParentIfMessageType.RPC_ImportedIfMethodListResp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					
		        resp_data.putParcelableArray("result",tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(result));
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
			msg.what = ParentIfMessageType.INIT.getValue();
			Bundle data = new Bundle();
			
			ISimpleLocalIf localIf = mBackendService.getLocalIf();
			
		        data.putParcelable("localIf", new SimpleLocalIfParcelable(localIf));
			ISimpleLocalIf[] localIfList = mBackendService.getLocalIfList();
			
		        data.putParcelableArray("localIfList", SimpleLocalIfParcelable.wrapArray(localIfList));
			tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf = mBackendService.getImportedIf();
			
		        data.putParcelable("importedIf", new tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable(importedIf));
			tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfList = mBackendService.getImportedIfList();
			
		        data.putParcelableArray("importedIfList", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(importedIfList));
			msg.setData(data);
			sendMessageToClients(msg);
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
		public void onLocalIfListChanged(ISimpleLocalIf[] localIfList){
			Log.i(TAG, "New value for LocalIfList from backend" + localIfList);

			Message msg = new Message();
			msg.what = ParentIfMessageType.SET_LocalIfList.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("localIfList", SimpleLocalIfParcelable.wrapArray(localIfList));
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
		public void onImportedIfListChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfList){
			Log.i(TAG, "New value for ImportedIfList from backend" + importedIfList);

			Message msg = new Message();
			msg.what = ParentIfMessageType.SET_ImportedIfList.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("importedIfList", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(importedIfList));
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
		public void onLocalIfSignalList(ISimpleLocalIf[] param){
			Log.i(TAG, "New singal for LocalIfSignalList = "+ " " + param);
			Message msg = new Message();
			msg.what = ParentIfMessageType.SIG_LocalIfSignalList.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("param", SimpleLocalIfParcelable.wrapArray(param));
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
		public void onImportedIfSignalList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param){
			Log.i(TAG, "New singal for ImportedIfSignalList = "+ " " + param);
			Message msg = new Message();
			msg.what = ParentIfMessageType.SIG_ImportedIfSignalList.getValue();
			Bundle data = new Bundle();
			
		        data.putParcelableArray("param", tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(param));
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
