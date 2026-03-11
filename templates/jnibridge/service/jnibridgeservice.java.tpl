package {{camel .Module.Name}}.{{camel .Module.Name}}jniservice;

import android.util.Log;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.Abstract{{Camel .Interface.Name}};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
{{- template "importApiWithParcelable" .}}

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class {{Camel .Interface.Name}}JniService extends Abstract{{Camel .Interface.Name}} {


    private final static String TAG = "{{Camel .Interface.Name}}JniService";
    private static volatile boolean isServiceReady = false;
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingFutures
        = new ConcurrentHashMap<>();

    public {{Camel .Interface.Name}}JniService()
    {
        fire_readyStatusChanged(true);
    }

{{- range .Interface.Properties }}
    @Override
    public void set{{Camel .Name}}({{javaParam "" .}})
    {
        Log.i(TAG, "request set{{Camel .Name}} called, will call native ");
        nativeSet{{Camel .Name}}({{javaVar . }});
    }

    @Override
    public {{javaReturn "" . }} get{{Camel .Name}}()
    {
        Log.i(TAG, "request get{{Camel .Name}} called, will call native ");
        return nativeGet{{Camel .Name}}();
    }

  {{ end }}
    // methods
  {{- range .Interface.Operations }}

    @Override
    public {{javaReturn "" .Return}} {{camel .Name}}({{javaParams "" .Params}}) {
        Log.i(TAG, "request method {{camel .Name}} called");
      {{- if .Return.IsVoid }}
        try {
            {{camel .Name}}Async({{javaVars .Params}}).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "{{camel .Name}} sync call timed out");
        } catch (Exception e) {
            Log.w(TAG, "{{camel .Name}} sync call failed: " + e.getMessage());
        }
      {{- else }}
        try {
            return {{camel .Name}}Async({{javaVars .Params}}).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "{{camel .Name}} sync call timed out");
            return {{javaDefault "" .Return}};
        } catch (Exception e) {
            Log.w(TAG, "{{camel .Name}} sync call failed: " + e.getMessage());
            return {{javaDefault "" .Return}};
        }
      {{- end }}
    }

    @Override
    public {{javaAsyncReturn "" .Return}} {{camel .Name}}Async({{javaParams "" .Params}}) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<{{- if .Return.IsVoid }}Void{{else}}Object{{end}}> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = native{{Camel .Name}}Async(callId{{if len .Params}}, {{javaVars .Params}}{{end}});
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for {{camel .Name}}"));
        }
        return future;
    }

  {{- end }}

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
  {{- range .Interface.Properties }}
    private native void nativeSet{{Camel .Name}}({{javaParam "" .}});
    private native {{javaReturn "" . }} nativeGet{{Camel .Name}}();
  {{ end }}
    // methods (async, returns false if native service unavailable)
  {{- range .Interface.Operations }}
    private native boolean native{{Camel .Name}}Async(String callId{{if len .Params}}, {{javaParams "" .Params}}{{end}});
  {{- end }}

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
        if (!value) {
            cancelAllPending();
        }
    }

    public void cancelAllPending() {
        for (String callId : pendingFutures.keySet()) {
            CompletableFuture<?> future = pendingFutures.remove(callId);
            if (future != null) {
                future.completeExceptionally(
                    new IllegalStateException("Service disconnected"));
            }
        }
    }

    // Operation result callbacks (called by native C++ continuations)
  {{- range .Interface.Operations }}
  {{- if .Return.IsVoid }}
    public void on{{Camel .Name}}Result(String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Void> typedFuture = (CompletableFuture<Void>) future;
            typedFuture.complete(null);
        }
    }
  {{- else }}
    public void on{{Camel .Name}}Result({{javaReturn "" .Return}} result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
  {{- end }}
  {{- end }}

    //In theory event listener interface

    {{- range .Interface.Properties }}
    public void on{{Camel .Name}}Changed({{javaType "" .}} newValue)
    {
         Log.i(TAG, "on{{Camel .Name}}Changed, will pass notification to all listeners");
         fire{{Camel .Name}}Changed(newValue);
    }
    {{- end}}
    {{- range .Interface.Signals }}
    public void on{{Camel .Name}}({{javaParams "" .Params}})
    {
        Log.i(TAG, "on{{Camel .Name}}, will pass notification to all listeners");
        fire{{Camel .Name}}({{javaVars .Params}});
    }
    {{- end }}

}
