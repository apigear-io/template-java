package {{camel .Module.Name}}.{{camel .Module.Name}}jniservice;

import android.util.Log;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.Abstract{{Camel .Interface.Name}};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger.Conversions;
{{- template "importApiWithParcelable" .}}

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;


public class {{Camel .Interface.Name}}JniService extends Abstract{{Camel .Interface.Name}} {


    private final static String TAG = "{{Camel .Interface.Name}}JniService";
    private static volatile boolean isServiceReady = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public {{Camel .Interface.Name}}JniService()
    {
        fire_readyStatusChanged(true);
    }

{{- range .Interface.Properties }}
    @Override
    public void set{{Camel .Name}}({{javaListParam "" .}})
    {
        Log.i(TAG, "request set{{Camel .Name}} called, will call native ");
        {{- if .IsArray }}
        nativeSet{{Camel .Name}}(Conversions.toArray({{javaVar .}}, new {{javaElementType "" .}}[0]));
        {{- else }}
        nativeSet{{Camel .Name}}({{javaVar . }});
        {{- end }}
    }

    @Override
    public {{javaListReturn "" . }} get{{Camel .Name}}()
    {
        Log.i(TAG, "request get{{Camel .Name}} called, will call native ");
        {{- if .IsArray }}
        return Conversions.toList(nativeGet{{Camel .Name}}());
        {{- else }}
        return nativeGet{{Camel .Name}}();
        {{- end }}
    }

  {{ end }}
    // methods
  {{- range .Interface.Operations }}

    @Override
    public {{javaListReturn "" .Return}} {{camel .Name}}({{javaListParams "" .Params}}) {
        Log.i(TAG, "request method {{camel .Name}} called, will call native");
        {{- if .Return.IsVoid }}
        native{{Camel .Name}}({{- range $idx, $p := .Params }}{{- if $idx}}, {{ end -}}{{- if .IsArray }}Conversions.toArray({{javaVar .}}, new {{javaElementType "" .}}[0]){{- else }}{{javaVar .}}{{- end }}{{- end }});
        {{- else if .Return.IsArray }}
        return Conversions.toList(native{{Camel .Name}}({{- range $idx, $p := .Params }}{{- if $idx}}, {{ end -}}{{- if .IsArray }}Conversions.toArray({{javaVar .}}, new {{javaElementType "" .}}[0]){{- else }}{{javaVar .}}{{- end }}{{- end }}));
        {{- else }}
        return native{{Camel .Name}}({{- range $idx, $p := .Params }}{{- if $idx}}, {{ end -}}{{- if .IsArray }}Conversions.toArray({{javaVar .}}, new {{javaElementType "" .}}[0]){{- else }}{{javaVar .}}{{- end }}{{- end }});
        {{- end }}
    }

    @Override
    public  {{javaListAsyncReturn "" .Return}} {{camel .Name}}Async({{javaListParams "" .Params}}) {
        try {
            return CompletableFuture.{{- if .Return.IsVoid }}runAsync{{else}}supplyAsync{{end}}(
                    () -> {     {{- if not .Return.IsVoid }}return{{end}} {{camel .Name}}({{javaVars .Params }}); },
                    executor);
        } catch (RejectedExecutionException e) {
            {{javaListAsyncReturn "" .Return}} f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

  {{- end }}

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    @Override
    public void _shutdown() {
        isServiceReady = false;
        fire_readyStatusChanged(false);
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // Native methods — use array types for JNI compatibility
  {{- range .Interface.Properties }}
    private native void nativeSet{{Camel .Name}}({{javaParam "" .}});
    private native {{javaReturn "" . }} nativeGet{{Camel .Name}}();
  {{ end }}
  {{- range .Interface.Operations }}
    private native {{javaReturn "" .Return}} native{{Camel .Name}}({{javaParams "" .Params}});
  {{- end }}

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    // Callbacks from native — receive arrays, convert to List and fire events
    {{- range .Interface.Properties }}
    public void on{{Camel .Name}}Changed({{javaType "" .}} newValue)
    {
         Log.i(TAG, "on{{Camel .Name}}Changed, will pass notification to all listeners");
         {{- if .IsArray }}
         fire{{Camel .Name}}Changed(Conversions.toList(newValue));
         {{- else }}
         fire{{Camel .Name}}Changed(newValue);
         {{- end }}
    }
    {{- end}}
    {{- range .Interface.Signals }}
    public void on{{Camel .Name}}({{javaParams "" .Params}})
    {
        Log.i(TAG, "on{{Camel .Name}}, will pass notification to all listeners");
        fire{{Camel .Name}}({{- range $idx, $p := .Params }}{{- if $idx}}, {{ end -}}{{- if .IsArray }}Conversions.toList({{javaVar .}}){{- else }}{{javaVar .}}{{- end }}{{- end }});
    }
    {{- end }}

}
