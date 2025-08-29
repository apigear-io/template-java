package {{camel .Module.Name}}.{{camel .Module.Name}}jniclient;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.Abstract{{Camel .Interface.Name}};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;

import {{camel .Module.Name}}.{{camel .Module.Name}}_android_client.{{Camel .Interface.Name }}Client;

{{- range .Module.Structs }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Name}};
{{- end }}
{{- range .Module.Enums }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Name}};
{{- end }}
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class {{Camel .Interface.Name}}JniClient extends Abstract{{Camel .Interface.Name }} implements I{{Camel .Interface.Name }}EventListener
{

    private static final String TAG = "{{Camel .Interface.Name}}JniClient";

    private {{Camel .Interface.Name }}Client mMessengerClient = null;


    private static String ModuleName = "{{camel .Module.Name}}.{{camel .Module.Name}}jniservice.{{Camel .Interface.Name}}JniService";
    private String lastServicePackage ="";

    @Override
    public boolean _isReady()
    {
        return mMessengerClient._isReady();
    }

    {{- range .Interface.Properties }}
    @Override
    public void set{{Camel .Name}}({{javaParam "" .}})
    {
        Log.i(TAG, "got request from ue, set{{Camel .Name}}" + ({{javaVar .}}));
        mMessengerClient.set{{Camel .Name}}({{javaVar .}});
    }
    @Override
    public {{javaReturn "" . }} get{{Camel .Name}}()
    {
        Log.i(TAG, "got request from ue, get{{Camel .Name}}");
        return mMessengerClient.get{{Camel .Name}}();
    }
    {{ end }}

    {{- range .Interface.Operations }}
     public {{javaReturn "" .Return}} {{camel .Name}}({{javaParams "" .Params}})
     {
        Log.v(TAG, "Blocking call{{camel .Name}} - should not be used ");
        {{if not .Return.IsVoid}}return{{ end }} mMessengerClient.{{camel .Name}}({{javaVars .Params}});
    }

    public void {{camel .Name}}Async(String callId{{if len .Params}}, {{javaParams "" .Params}}{{end}}){
        Log.v(TAG, "non blocking call {{camel .Name}} ");
        mMessengerClient.{{camel .Name}}Async({{javaVars .Params }}).thenAccept(i -> {
            nativeOn{{Camel .Name}}Result({{if not .Return.IsVoid}}i, {{end}}callId);});
    }

    //Should not be called directly, use {{camel .Name}}Async(String callId, {{javaParams "" .Params}})
    public {{javaAsyncReturn "" .Return}} {{camel .Name}}Async({{javaParams "" .Params}})
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.{{camel .Name}}Async({{javaVars .Params }});
    }
    {{- end }}

    public boolean bind(Context ctx, String packageName, String connectionID){
        Log.v(TAG, "natice client: bind " + packageName);
        return initServiceConnection(ctx, packageName, connectionID);
    }

    public void unbind(){
        Log.v(TAG, "native client: unbind " + lastServicePackage);
        mMessengerClient.unbindFromService();
    }

    private boolean initServiceConnection(Context ctx, String servicePackage, String connectionID)
    {
        if (mMessengerClient == null)
        {
            mMessengerClient = new {{Camel .Interface.Name }}Client(ctx, connectionID);
            Log.w(TAG, "client created ");
            mMessengerClient.addEventListener(this);
        }
        if (lastServicePackage != servicePackage &&  mMessengerClient.isBoundToService()) {
            unbind();
        }
        lastServicePackage = servicePackage;
        boolean res = mMessengerClient.bindToService(lastServicePackage);
        Log.v(TAG, "Bind " + res+": to "+lastServicePackage);
        return res;
    }

    @Override
    public void on_readyStatusChanged(boolean isReady) {
        Log.w(TAG, "Connection state changed "+isReady);
        nativeIsReady(isReady);
    }

    //Event listener
    {{- range .Interface.Properties }}
    @Override
    public void on{{Camel .Name}}Changed({{javaType "" .}} newValue)
    {
        Log.w(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOn{{Camel .Name}}Changed(newValue);
    }
    {{- end }}

    {{- range .Interface.Signals }}
    @Override
    public void on{{Camel .Name}}({{javaParams "" .Params}})
    {
        Log.w(TAG, "NOTIFICATION from messenger client Signal {{.Name}} "{{- range .Params -}} + " " + {{javaVar .}}{{ end}});
        nativeOn{{Camel .Name}}({{javaVars .Params }});
    }
    {{- end }}


    {{- range .Interface.Properties }}
     private native void nativeOn{{Camel .Name}}Changed({{javaParam "" . }});
    {{- end }}

    {{- range .Interface.Signals }}
    private native void nativeOn{{Camel .Name}}({{javaParams "" .Params }});
    {{- end }}
    {{- range .Interface.Operations }}
    private native void nativeOn{{Camel .Name}}Result({{if not .Return.IsVoid}}{{javaReturn "" .Return}} result, {{end}}String callId);
    {{- end }}
    private native void nativeIsReady(boolean isReady);
}
