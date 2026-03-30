package {{camel .Module.Name}}.{{camel .Module.Name}}jniclient;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.Abstract{{Camel .Interface.Name}};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
{{- if .Interface.Operations }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.RemoteOperationException;
{{- end }}

import {{camel .Module.Name}}.{{camel .Module.Name}}_android_client.{{Camel .Interface.Name }}Client;
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger.Conversions;

{{- template "importApiWithParcelable" .}}
import android.content.Context;

import android.os.Bundle;
import java.util.List;
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
    // Interface method — List types
    @Override
    public void set{{Camel .Name}}({{javaListParam "" .}})
    {
        Log.i(TAG, "got request set{{Camel .Name}}" + ({{javaVar .}}));
        mMessengerClient.set{{Camel .Name}}({{javaVar .}});
    }
    {{- if .IsArray }}
    // JNI entry point — array types for C++ compatibility
    public void set{{Camel .Name}}({{javaParam "" .}})
    {
        Log.i(TAG, "got JNI request set{{Camel .Name}}");
        set{{Camel .Name}}(Conversions.toList({{javaVar .}}));
    }
    {{- end }}
    @Override
    public {{javaListReturn "" . }} get{{Camel .Name}}()
    {
        Log.i(TAG, "got request get{{Camel .Name}}");
        return mMessengerClient.get{{Camel .Name}}();
    }


    {{ end }}

    {{- range .Interface.Operations }}
    // Interface method — List types
    @Override
    public {{javaListReturn "" .Return}} {{camel .Name}}({{javaListParams "" .Params}})
    {
        Log.v(TAG, "Blocking call{{camel .Name}} - should not be used ");
        {{if not .Return.IsVoid}}return{{ end }} mMessengerClient.{{camel .Name}}({{javaVars .Params}});
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOn{{Camel .Name}}Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void {{camel .Name}}Async(String callId{{if len .Params}}, {{javaParams "" .Params}}{{end}}){
        Log.v(TAG, "non blocking call {{camel .Name}} ");
        mMessengerClient.{{camel .Name}}Async({{- range $idx, $p := .Params }}{{- if $idx}}, {{ end -}}{{- if .IsArray }}Conversions.toList({{javaVar .}}){{- else }}{{javaVar .}}{{- end }}{{- end }}).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "{{.Name}} async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                {{- if not .Return.IsVoid }}
                {{- if .Return.IsArray }}
                nativeOn{{Camel .Name}}Result(Conversions.toArray(result, new {{javaElementType "" .Return}}[0]), callId);
                {{- else }}
                nativeOn{{Camel .Name}}Result(result, callId);
                {{- end }}
                {{- else }}
                nativeOn{{Camel .Name}}Result(callId);
                {{- end }}
            }
        });
    }

    @Override
    public {{javaListAsyncReturn "" .Return}} {{camel .Name}}Async({{javaListParams "" .Params}})
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

    // Event listener — receives List from messenger client, converts to array for native
    {{- range .Interface.Properties }}
    @Override
    public void on{{Camel .Name}}Changed({{javaListType "" .}} newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        {{- if .IsArray }}
        nativeOn{{Camel .Name}}Changed(Conversions.toArray(newValue, new {{javaElementType "" .}}[0]));
        {{- else }}
        nativeOn{{Camel .Name}}Changed(newValue);
        {{- end }}
    }
    {{- end }}

    {{- range .Interface.Signals }}
    @Override
    public void on{{Camel .Name}}({{javaListParams "" .Params}})
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal {{.Name}} "{{- range .Params -}} + " " + {{javaVar .}}{{ end}});
        nativeOn{{Camel .Name}}({{- range $idx, $p := .Params }}{{- if $idx}}, {{ end -}}{{- if .IsArray }}Conversions.toArray({{javaVar .}}, new {{javaElementType "" .}}[0]){{- else }}{{javaVar .}}{{- end }}{{- end }});
    }
    {{- end }}


    // Native declarations — array types for JNI compatibility
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
