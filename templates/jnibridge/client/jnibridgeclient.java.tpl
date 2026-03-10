package {{camel .Module.Name}}.{{camel .Module.Name}}jniclient;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.Abstract{{Camel .Interface.Name}};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
{{- if .Interface.Operations }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.RemoteOperationException;
{{- end }}

import {{camel .Module.Name}}.{{camel .Module.Name}}_android_client.{{Camel .Interface.Name }}Client;

{{- template "importApiWithParcelable" .}}
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
        return mMessengerClient != null ? mMessengerClient._isReady() : false;
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
     @Override
     public {{javaReturn "" .Return}} {{camel .Name}}({{javaParams "" .Params}})
     {
        Log.v(TAG, "Blocking call{{camel .Name}} - should not be used ");
        {{if not .Return.IsVoid}}return{{ end }} mMessengerClient.{{camel .Name}}({{javaVars .Params}});
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOn{{Camel .Name}}Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void {{camel .Name}}Async(String callId{{if len .Params}}, {{javaParams "" .Params}}{{end}}){
        Log.v(TAG, "non blocking call {{camel .Name}} ");
        mMessengerClient.{{camel .Name}}Async({{javaVars .Params }}).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "{{.Name}} async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOn{{Camel .Name}}Result({{if not .Return.IsVoid}}result, {{end}}callId);
            }
        });
    }

    @Override
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
        if (mMessengerClient != null)
        {
            mMessengerClient.unbindFromService();
        }
    }

    private boolean initServiceConnection(Context ctx, String servicePackage, String connectionID)
    {
        if (mMessengerClient == null)
        {
            mMessengerClient = new {{Camel .Interface.Name }}Client(ctx, connectionID);
            Log.i(TAG, "client created ");
            mMessengerClient.addEventListener(this);
        }
        if (!lastServicePackage.equals(servicePackage) &&  mMessengerClient.isBoundToService()) {
            unbind();
        }
        lastServicePackage = servicePackage;
        boolean res = mMessengerClient.bindToService(lastServicePackage);
        Log.v(TAG, "Bind " + res+": to "+lastServicePackage);
        return res;
    }

    @Override
    public void on_readyStatusChanged(boolean isReady) {
        Log.i(TAG, "Will call native Connection state changed "+isReady);
        nativeIsReady(isReady);
    }

    //Event listener
    {{- range .Interface.Properties }}
    @Override
    public void on{{Camel .Name}}Changed({{javaType "" .}} newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOn{{Camel .Name}}Changed(newValue);
    }
    {{- end }}

    {{- range .Interface.Signals }}
    @Override
    public void on{{Camel .Name}}({{javaParams "" .Params}})
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal {{.Name}} "{{- range .Params -}} + " " + {{javaVar .}}{{ end}});
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
    {{- if .Interface.Operations }}
    private native void nativeAsyncOperationFailed(String callId, String errorMessage, int errorCode);
    {{- end }}
    private native void nativeIsReady(boolean isReady);
}
