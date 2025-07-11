//TODO later// Copyright Epic Games, Inc. All Rights Reserved.

package {{camel .Module.Name}}.{{camel .Module.Name}}_android_client;

import android.content.ServiceConnection;
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
import android.content.ComponentName;
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
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.Abstract{{Camel .Interface.Name}};
import {{dot .Module.Name}}.{{dot .Module.Name}}_android_messenger.{{Camel .Interface.Name}}MessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicInteger;

public class {{Camel .Interface.Name }}Client extends Abstract{{Camel .Interface.Name}} implements ServiceConnection
{
	private static final String TAG = "{{Camel .Interface.Name }}Client";

	private final Context mApplicationContext;

    private boolean mIsBoundToService = false;
    private Messenger mServiceMessenger = null;
    private Messenger mClientMessenger = null;
    private String mConnectionId;
    private ClientHandler mClientHandler = new ClientHandler();

    private final Map<Integer, Consumer<Bundle>> mpendingCalls = new ConcurrentHashMap<>();
    AtomicInteger callIdsGetter = new AtomicInteger(0);

	{{- range .Interface.Properties }}
    private {{javaReturn "" .}} m_{{javaVar  .}} = {{ javaDefault "" . }};
    {{- end}}


	public {{Camel .Interface.Name }}Client(Context applicationContext, String connectionId)
	{
        assert (applicationContext != null);
        mApplicationContext = applicationContext;

        if (connectionId.isEmpty())
        {
            mConnectionId = UUID.randomUUID().toString();
        }
        else
        {
            mConnectionId = connectionId;
        }
        mClientMessenger = new Messenger(mClientHandler);
	}

	public boolean isBoundToService()
    {
        return mIsBoundToService;
    }


	/**
     * Binds to a running service of type {{Camel .Interface.Name }}ServiceAdapter.
     *
     * @return true if binding was successful, false otherwise.
     */
    public boolean bindToService(String packageName)
    {
        Intent intent = new Intent();
        intent.setClassName(packageName, "com.example.helloservice.HelloServiceActivityAdapter");
        intent.putExtra("connectionID", mConnectionId);
        Log.d(TAG, "Using context: " + mApplicationContext.getClass().getName());
        Log.d(TAG, "bindToService intent=" + intent + ", mServiceConnection=" + this);

        return mApplicationContext.bindService(intent, this,0 );
    }

    /**
     * Unbinds from the Unreal instance service.
     */
    public void unbindFromService()
    {
        if (mIsBoundToService)
        {
            Log.v(TAG, "unbindFromService");
            Message msg = Message.obtain(null, {{Camel .Interface.Name}}MessageType.UNREGISTER_CLIENT.ordinal());
            msg.getData().putString("connectionID", mConnectionId);
            mClientHandler.sendToService(msg);
            mApplicationContext.unbindService(this);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder serviceBinder)
    {
        Log.v(TAG, "onServiceConnected name=" + name + ", serviceBinder=" + serviceBinder);
        // Retrieve and use the Messenger
        mServiceMessenger = new Messenger(serviceBinder);
        mIsBoundToService = true;

        requestRegisterClient();
        fire_readyStatusChanged(true);
    }

    @Override
    public void onServiceDisconnected(ComponentName name)
    {
        Log.w(TAG, "onServiceDisconnected name=" + name);
        doCleanupForUnbinding("onServiceDisconnected name=" + name);
    }

    @Override
    public void onBindingDied(ComponentName name)
    {
        Log.w(TAG, "onBindingDied name=" + name);
        doCleanupForUnbinding("onBindingDied name=" + name);
        ServiceConnection.super.onBindingDied(name);
    }

    private void requestRegisterClient()
    {
        if (mClientMessenger != null)
        {
            Message msg = Message.obtain(null, {{Camel .Interface.Name}}MessageType.REGISTER_CLIENT.ordinal());
            msg.replyTo = mClientMessenger;
            msg.getData().putString("connectionID", mConnectionId);
            mClientHandler.sendToService(msg);
        }
    }

    private void doCleanupForUnbinding(String caller)
    {
        mServiceMessenger = null;
        mClientMessenger = null;
        mIsBoundToService = false;
        fire_readyStatusChanged(false);
    }


    class ClientHandler extends Handler {

        ClientHandler(){
            super(Looper.getMainLooper());
        }

	    public void sendToService(Message msg)
	    {
		    if (mServiceMessenger != null)
		    {
			    try
			    {
				    mServiceMessenger.send(msg);
			    } catch (RemoteException e)
			    {
				    Log.e(TAG, "Can't send message to service, looks like it is not correctly connected, make sure client has bind to service " + e);
			    }
		    }
	    }

	    {{- $InterfaceName := Camel .Interface.Name}}
	    @Override
	    public void handleMessage(Message msg)
	    {
		    Log.i(TAG, "Handle msg " + msg);

		    switch ({{Camel .Interface.Name}}MessageType.fromInteger(msg.what))
		    {
		    //TODO ENUMS AND ARRAYS (for array just change the func to getXArray)
		    {{- range .Interface.Properties }}
			    case SET_{{Camel .Name}}:
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

				    on{{Camel .Name}}(newValue);
				    break;
			    }
		    {{- end }}
			    // TODO ENUMS AND ARRAYS (for array just change the func to getXArray)
			    // TODO params may be different structs from different modules, there should be a custom class loader 
			    // with a list of class loaders required for this message
			    // IF there are at least 2 different structs from different modules - in theory if it is from same module setting loader for one should work for all structs from this module.
		    {{- range .Interface.Signals }}
			    case SIG_{{Camel .Name}}: {

				    Bundle data = msg.getData();
				    int callId = data.getInt("callId");
			    {{- range .Params }}
			    {{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
				    {{javaReturn "" .}} {{javaVar .}} = data.get{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", -1);
			    {{- else if (eq .KindType "bool")}}
				    {{javaReturn "" .}} {{javaVar .}} =  = data.getInt("{{.Name}}", -1);
			    {{- else }}
				    data.setClassLoader({{Camel .Type}}Parcelable.class.getClassLoader());
				    {{javaReturn "" .}}Parcelable {{javaVar .}}Parcelable = data.getParcelable("{{.Name}}", {{Camel .Type}}Parcelable.class);
				    {{javaReturn "" .}} {{javaVar .}} = {{javaVar .}}Parcelable.get{{Camel (javaReturn "" .)}}();
			    {{- end }}
			    {{- end }}
				    on{{Camel .Name}}({{javaVars .Params}});
				    break;
			    }
			    {{- end }}
			    {{- range .Interface.Operations }}
			    case RPC_{{Camel .Name}}Resp: {

				    Bundle data = msg.getData();
				    int callId = data.getInt("callId");

				    Consumer<Bundle> foundCall = mpendingCalls.remove(callId);
                    if (foundCall != null)
                    {
                        foundCall.accept(data);
                    }
                    else
                    {
                        Log.v(TAG, "received {{$InterfaceName}}MessageType.RPC_{{Camel .Name}}Resp , could not find pending call for " + msg.obj);
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
    };

	// TODO handle arrays, enums, complex structs, imports, externs
{{- range .Interface.Properties }}
    @Override
    public void set{{Camel .Name}}({{javaParam "" .}})
    {
        Log.i(TAG, "request set{{Camel .Name}} called "+ {{javaVar . }});
        if (m_{{javaVar  .}} != {{javaVar  .}})
        {
			Message msg = new Message();
			msg.what = {{$InterfaceName}}MessageType.PROP_{{Camel .Name}}.getValue();
			Bundle data = new Bundle();
			{{- if and (.IsPrimitive) (not (eq .KindType "bool")) }}
			data.put{{ ( Camel  (javaType "" .) ) }}("{{.Name}}", {{javaVar . }});
			{{- else if (eq .KindType "bool")}}
			data.putInt("{{.Name}}", {{javaVar . }});
			{{- else }}
			data.putParcelable("{{.Name}}", new {{Camel .Type}}Parcelable({{javaVar . }}));
			{{- end }}
			msg.setData(data);
			mClientHandler.sendToService(msg);
        }

    }

	public void on{{Camel .Name}}({{javaParam "" .}})
    {
        Log.i(TAG, "value received from service for {{Camel .Name}} ");
        if (m_{{javaVar  .}} != {{javaVar  .}})
        {
            m_{{javaVar  .}} = {{javaVar  .}};
            fire{{Camel .Name}}Changed({{javaVar .}});
        }

    }

    @Override
    public {{javaReturn "" . }} get{{Camel .Name}}()
    {
        Log.i(TAG, "request get{{Camel .Name}} called,");
        return m_{{javaVar  .}};
    }

  {{ end }}
    // methods
  {{- range .Interface.Operations }}

   
    @Override
    public {{javaReturn "" .Return}} {{camel .Name}}({{javaParams "" .Params}}) {
        {{javaAsyncReturn "" .Return}} resFuture = {{camel .Name}}Async({{javaVars .Params }});
        try {
            return resFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public  {{javaAsyncReturn "" .Return}} {{camel .Name}}Async({{javaParams "" .Params}}) {

    	Log.i(TAG, "Call on service {{camel .Name}}  "
	{{- range .Params -}} + " " + {{javaVar .}}{{ end}});
		Message msg = new Message();
		msg.what = {{$InterfaceName}}MessageType.RPC_{{Camel .Name}}Req.getValue();
		Bundle data = new Bundle();
        int msgId =  callIdsGetter.getAndIncrement();
        data.putInt("callId",msgId);
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
		mClientHandler.sendToService(msg);

        {{javaAsyncReturn "" .Return}}  future = new CompletableFuture<>();
        Consumer<Bundle> resolver = bundle -> {

        {{- if and (.Return.IsPrimitive) (not (eq .Return.KindType "bool")) }}
		    {{javaReturn "" .Return }} result = bundle.get{{ ( Camel  (javaType "" .Return) ) }}("result", -1);
	    {{- else if (eq .Return.KindType "bool")}}
		    {{javaReturn "" .Return }} result =  = bundle.getInt("result", -1);
	    {{- else }}
		    bundle.setClassLoader({{Camel .Return.Type}}Parcelable.class.getClassLoader());
		    {{javaReturn "" .Return }} result = bundle.getParcelable("result", {{Camel .Return.Type}}Parcelable.class).get{{Camel (javaReturn "" .Return)}}();
	    {{- end }}

            Log.v(TAG, "resolve {{.Name }}" + result);
            future.complete(result);
        };

        // Store the lambda function in the map
        mpendingCalls.put(msgId, resolver);

        return future;
    }

  {{- end }}    

    @Override
    public boolean _isReady() {
        return mIsBoundToService  && mServiceMessenger != null;
    }

	// Should be called when message arrives
    {{- range .Interface.Signals }}
    public void on{{Camel .Name}}({{javaParams "" .Params}})
    {
        Log.i(TAG, "on{{Camel .Name}}  received from service");
        fire{{Camel .Name}}({{javaVars .Params}});
    }
    {{- end }}
}
