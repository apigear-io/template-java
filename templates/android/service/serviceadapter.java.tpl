//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package {{camel .Module.Name}}.{{camel .Module.Name}}_android_service;

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

{{- template "importApiWithParcelable" .}}

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.I{{Camel .Interface.Name}}ServiceProvider;
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.Abstract{{Camel .Interface.Name}};
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger.{{Camel .Interface.Name}}MessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class {{Camel .Interface.Name }}ServiceAdapter extends Service
{
	private static final String TAG = "{{Camel .Interface.Name }}ServiceAdapter";
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	private Messenger mMessenger;
	private static IncomingHandler mHandler = null;
	// Lifetime of mBackendService and its accessibility through mServiceProvider is controlled by the backend provide with setService function.
	// The ServiceAdapter is just a user of the backend. 
	// Use provided ServiceStarter classes and the start and stop functions for that.
	private static I{{Camel .Interface.Name}} mBackendService;
	private static I{{Camel .Interface.Name}}ServiceProvider mServiceProvider;
	private static final Object sBackendMutex = new Object();

	public {{Camel .Interface.Name }}ServiceAdapter()
	{
	}

	public static I{{Camel .Interface.Name}} setService(I{{Camel .Interface.Name}}ServiceProvider serviceProvider)
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
					Log.i(TAG, "LIFECYCLE: setService({{Camel .Interface.Name}}) called. For handler " + mHandler);
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
		Log.i(TAG, "LIFECYCLE: onCreate({{Camel .Interface.Name }}Service) called. context = " + this);
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
		Log.i(TAG, "LIFECYCLE: {{Camel .Interface.Name }}Service::onStartCommand called. context = " + this +
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
		Log.i(TAG, "LIFECYCLE: onDestroy({{Camel .Interface.Name }}Service) - proc = " + ", mMessenger = " + mMessenger);
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
	class IncomingHandler extends Handler implements I{{Camel .Interface.Name }}EventListener
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
		{{- $InterfaceName := Camel .Interface.Name}}
		public void handleMessage(Message msg)
		{
			Log.i(TAG, "Handle msg " + msg);
			I{{Camel .Interface.Name}} backend;
			synchronized ({{Camel .Interface.Name }}ServiceAdapter.sBackendMutex)
			{
				backend = {{Camel .Interface.Name }}ServiceAdapter.mBackendService;
			}
			if (backend == null || !backend._isReady())
			{
				if ({{Camel .Interface.Name}}MessageType.fromInteger(msg.what) != {{Camel .Interface.Name}}MessageType.REGISTER_CLIENT
					&& {{Camel .Interface.Name}}MessageType.fromInteger(msg.what) != {{Camel .Interface.Name}}MessageType.UNREGISTER_CLIENT)
				{
					Log.w(TAG, "Check if server is ready, messsage will be dropped. MsgType: {{Camel .Interface.Name}}MessageType" + {{Camel .Interface.Name}}MessageType.fromInteger(msg.what) );
					return;
				}
			}
			switch ({{Camel .Interface.Name}}MessageType.fromInteger(msg.what))
			{
				case REGISTER_CLIENT:
					addClientActivity(msg.replyTo, msg.getData().getString("connectionID", ""));
					sendInit();
					break;
				case UNREGISTER_CLIENT:
					removeClientActivity(msg.getData().getString("connectionID"));
					break;
			{{- range .Interface.Properties }}
					case PROP_{{Camel .Name}}:
					{
						Bundle data = msg.getData();
						{{- if not (.IsPrimitive)}}
						data.setClassLoader({{template "getParcelable" . }}.class.getClassLoader());
						{{- end}}
						{{template "getDataFromBundle" . }}
						backend.set{{Camel .Name}}({{javaVar .}});
						break;
					}
			{{- end }}
			{{- range .Interface.Operations }}
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_{{Camel .Name}}Req: {

					Bundle data = msg.getData();
					{{template "setClassLoaderIfNeeded" .Params}}
					int callId = data.getInt("callId");

					{{- range .Params }}
					{{template "getDataFromBundle" . }}
					{{- end }}
					{{ if not .Return.IsVoid }}{{javaReturn "" .Return}} result = {{ end}} backend.{{camel .Name}}({{javaVars .Params}});

					Message respMsg = new Message();
					respMsg.what = {{$InterfaceName}}MessageType.RPC_{{Camel .Name}}Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					{{- if not .Return.IsVoid }}
					{{ template "putResultIntoBundle" . }}
					{{- end }}
					respMsg.setData(resp_data);

					try {
						msg.replyTo.send(respMsg);
					} catch (RemoteException e) {
						throw new RuntimeException(e);
					}
					break;

				}
			{{- end }}
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
			msg.what = {{$InterfaceName}}MessageType.INIT.getValue();
			Bundle data = new Bundle();
			I{{Camel .Interface.Name}} backend;
			synchronized ({{Camel .Interface.Name }}ServiceAdapter.sBackendMutex)
			{
				backend = {{Camel .Interface.Name }}ServiceAdapter.mBackendService;
			}
			if (backend != null && backend._isReady())
			{
				{{range .Interface.Properties}}
				{{javaReturn "" .}} {{javaVar .}} = backend.get{{Camel .Name}}();
				{{template "putDataIntoBundle" .}}
				{{- end}}
				msg.setData(data);
				sendMessageToClients(msg);
			}
		}

		{{- range .Interface.Properties }}
		@Override
		public void on{{Camel .Name}}Changed({{javaType "" .}} {{javaVar .}}){
			Log.i(TAG, "New value for {{Camel .Name}} from backend" + {{javaVar .}});

			Message msg = new Message();
			msg.what = {{$InterfaceName}}MessageType.SET_{{Camel .Name}}.getValue();
			Bundle data = new Bundle();
			{{template "putDataIntoBundle" . }}
			msg.setData(data);
			sendMessageToClients(msg);
		}
		{{- end }}
		{{- range .Interface.Signals }}
		@Override
		public void on{{Camel .Name}}({{javaParams "" .Params}}){
			Log.i(TAG, "New singal for {{Camel .Name}} = "
			{{- range .Params -}} + " " + {{javaVar .}}{{ end}});
			Message msg = new Message();
			msg.what = {{$InterfaceName}}MessageType.SIG_{{Camel .Name}}.getValue();
			Bundle data = new Bundle();
		{{- range .Params }}
			{{template "putDataIntoBundle" . }}
		{{- end }}
			msg.setData(data);
			sendMessageToClients(msg);
		}
		{{- end }}
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
