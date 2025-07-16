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

//import message type and parcelabe types

{{- range .Module.Structs }}
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.{{Camel .Name}};
import {{dot .Module.Name}}.{{dot .Module.Name}}_android_messenger.{{Camel .Name}}Parcelable;
{{- end }}
{{- range .Module.Enums }}
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.{{Camel .Name}};
import {{dot .Module.Name}}.{{dot .Module.Name}}_android_messenger.{{Camel .Name}}Parcelable;
{{- end }}

import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.I{{Camel .Interface.Name}}ServiceFactory;
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.Abstract{{Camel .Interface.Name}};
import {{dot .Module.Name}}.{{dot .Module.Name}}_android_messenger.{{Camel .Interface.Name}}MessageType;

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
	private static I{{Camel .Interface.Name}} mBackendService;
	private static I{{Camel .Interface.Name}}ServiceFactory mServiceFactory;

	//private final List<Message> mMessagesQueue = new ArrayList<>();

	public {{Camel .Interface.Name }}ServiceAdapter()
	{
	}

	public static I{{Camel .Interface.Name}} setService(I{{Camel .Interface.Name}}ServiceFactory factory)
	{
		Log.i(TAG, "Setting factory: " + factory);
		if (mServiceFactory  != factory)
		{
			mServiceFactory = factory;
		}
		mBackendService = mServiceFactory.getServiceInstance();
		if (mHandler != null)
		{
			mBackendService.addEventListener(mHandler);
		}
		return mBackendService;
	}


	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.i(TAG, "LIFECYCLE: onCreate({{Camel .Interface.Name }}Service) called. context = " + this);

		mHandler = new IncomingHandler(this);
		mMessenger = new Messenger(mHandler);
		if (mBackendService != null)
		{
			mBackendService.addEventListener(mHandler);
		}
	}

	// execution of service will start on calling this method
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.i(TAG, "LIFECYCLE: {{Camel .Interface.Name }}Service::onStartCommand called. context = " + this +
				", startID=" + startId);

		return START_STICKY;
	}

	// execution of the service will stop on calling this method
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		Log.i(TAG, "LIFECYCLE: onDestroy({{Camel .Interface.Name }}Service) - proc = " + ", mMessenger = " + mMessenger
		);

		if (mBackendService != null)
		{
			Log.i(TAG, "LIFECYCLE: onDestroy({{Camel .Interface.Name }}Service) - proc = " + ", remove engine event callback!");

			mBackendService.removeEventListener(mHandler);
			mBackendService = null;
		}
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
		private final Service mApplicationContext;
		private final ConcurrentHashMap<String, Messenger> mActivityClients = new ConcurrentHashMap<>();

		IncomingHandler(Service context)
		{
			super(Looper.getMainLooper());
			mApplicationContext = context;
		}

		private void sendMessageToActivityClients(Message msg)
		{
			for (Map.Entry<String, Messenger> client : mActivityClients.entrySet())
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
			if (mBackendService == null || !mBackendService._isReady())
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
					break;
				case UNREGISTER_CLIENT:
					removeClientActivity(msg.getData().getString("connectionID"));
					break;
			//TODO ENUMS AND ARRAYS (for array just change the func to getXArray)
			{{- range .Interface.Properties }}
					case PROP_{{Camel .Name}}:
					{
						Bundle data = msg.getData();
					{{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
						{{javaReturn "" .}} newValue = data.get{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", -1);
					{{- else if (eq .KindType "bool")}}
						{{javaReturn "" .}} newValue =  = data.getInt}("{{.Name}}", -1);
					{{- else }}
						data.setClassLoader({{Camel .Type}}Parcelable.class.getClassLoader());
						{{javaReturn "" .}} newValue = data.getParcelable("{{.Name}}", {{Camel .Type}}Parcelable.class).get{{Camel (javaReturn "" .)}}();
					{{- end }}

						mBackendService.set{{Camel .Name}}(newValue);
						break;
					}
			{{- end }}
			{{- range .Interface.Operations }}
			// TODO ENUMS AND ARRAYS (for array just change the func to getXArray)
			// TODO params may be different structs from different modules, there should be a custom class loader 
			// with a list of class loaders required for this message
			// IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
				case RPC_{{Camel .Name}}Req: {

					Bundle data = msg.getData();
					{{- range .Params }}
					{{- if not .IsPrimitive }}
					data.setClassLoader({{Camel .Type}}Parcelable.class.getClassLoader());
					{{- end }}
					{{- end }}
					int callId = data.getInt("callId");

					{{- range .Params }}
					{{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
					{{javaReturn "" .}} {{javaVar .}} = data.get{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", -1);
					{{- else if (eq .KindType "bool")}}
					{{javaReturn "" .}} {{javaVar .}} =  = data.getInt("{{.Name}}", -1);
					{{- else }}
					{{javaReturn "" .}}Parcelable {{javaVar .}}Parcelable = data.getParcelable("{{.Name}}", {{Camel .Type}}Parcelable.class);
					{{javaReturn "" .}} {{javaVar .}} = {{javaVar .}}Parcelable.get{{Camel (javaReturn "" .)}}();
					{{- end }}
					{{- end }}

					{{- if .Return.IsPrimitive }}
					{{javaReturn "" .Return}} result = mBackendService.{{camel .Name}}({{javaVars .Params}});
					{{- else }}
					{{javaReturn "" .Return}} dataResult = mBackendService.{{camel .Name}}({{javaVars .Params}});
					{{javaReturn "" .Return}}Parcelable result = new {{javaReturn "" .Return}}Parcelable(dataResult);
					{{- end }}

					Message respMsg = new Message();
					respMsg.what = {{$InterfaceName}}MessageType.RPC_{{Camel .Name}}Resp.getValue();
					Bundle resp_data = new Bundle();
					resp_data.putInt("callId", callId);
					{{- if and (.Return.IsPrimitive) (not (eq .Return.KindType "bool")) }}
					resp_data.put{{ ( Camel  (javaType "" .Return) ) }}("result", result);
					{{- else if (eq .Return.KindType "bool")}}
					resp_data.putInt("result", result);
					{{- else }}
					resp_data.putParcelable("result", result);
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
				mActivityClients.put(connectionID, serviceReply);
				Log.i(TAG, "Register event listener with connectionID = " + connectionID);
			}
		}

		private void removeClientActivity(String connectionID)
		{
			mActivityClients.remove(connectionID);
			Log.i(TAG, "UnRegister event listener with connectionID = " + connectionID);
		}

		{{- range .Interface.Properties }}
		@Override
		public void on{{Camel .Name}}Changed({{javaType "" .}} newValue){
			Log.i(TAG, "New value for {{Camel .Name}} from backend" + newValue);

			Message msg = new Message();
			msg.what = {{$InterfaceName}}MessageType.SET_{{Camel .Name}}.getValue();
			Bundle data = new Bundle();
			{{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
			data.put{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", newValue);
			{{- else if (eq .KindType "bool")}}
			data.putInt("{{.Name}}", newValue);
			{{- else }}
			data.putParcelable("{{.Name}}", new {{Camel .Type}}Parcelable(newValue));
			{{- end }}
			msg.setData(data);
			sendMessageToActivityClients(msg);
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
			{{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
			data.put{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", {{ javaVar .}});
			{{- else if (eq .KindType "bool")}}
			data.putInt("{{.Name}}", {{ javaVar .}});
			{{- else }}
			data.putParcelable("{{.Name}}", new {{Camel .Type}}Parcelable({{javaVar .}}));
			{{- end }}
		{{- end }}
			msg.setData(data);
			sendMessageToActivityClients(msg);
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
