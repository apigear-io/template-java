package {{camel .Module.Name}}.{{camel .Module.Name}}_impl;

import android.os.Messenger;
import android.util.Log;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.Abstract{{Camel .Interface.Name}};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
{{- template "importApiWithService" .}}


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class {{Camel .Interface.Name}}Service extends Abstract{{Camel .Interface.Name}} {

    private final static String TAG = "{{Camel .Interface.Name}}Service";
    private volatile boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    {{- range .Interface.Properties }}
    private {{javaListReturn "" .}} m_{{javaVar  .}} = {{ javaListDefault "" . }};
    {{- end}}

    public {{Camel .Interface.Name}}Service()
    {
        fire_readyStatusChanged(true);
    }

{{- range .Interface.Properties }}
    @Override
    public void set{{Camel .Name}}({{javaListParam "" .}})
    {
        Log.i(TAG, "request set{{Camel .Name}} called ");
        {{- if .IsArray }}
        if (!m_{{javaVar  .}}.equals({{javaVar  .}}))
        {{- else if or (or .IsPrimitive  (eq .KindType "enum")) (eq .KindType "interface") }}
        if (m_{{javaVar  .}} != {{javaVar  .}})
        {{- else }}
        if ( (m_{{javaVar  .}} != null && ! m_{{javaVar  .}}.equals({{javaVar  .}}))
        || (m_{{javaVar  .}} == null && {{javaVar  .}} != null ))
        {{- end}}
        {
            m_{{javaVar  .}} = {{- if .IsArray }} new ArrayList<>({{javaVar  .}}){{- else }} {{javaVar  .}}{{- end}};
            on{{Camel .Name}}Changed({{- if .IsArray }}new ArrayList<>(m_{{javaVar  .}}){{- else }}m_{{javaVar  .}}{{- end}});
        }

    }

    @Override
    public {{javaListReturn "" . }} get{{Camel .Name}}()
    {
        Log.i(TAG, "request get{{Camel .Name}} called,");
        return {{- if .IsArray }} new ArrayList<>(m_{{javaVar  .}}){{- else }} m_{{javaVar  .}}{{- end}};
    }

  {{ end }}
    // methods
  {{- range .Interface.Operations }}

    @Override
    public {{javaListReturn "" .Return}} {{camel .Name}}({{javaListParams "" .Params}}) {
        Log.i(TAG, "request method {{camel .Name}} called, returnig default");
        return {{ if not .Return.IsVoid }}{{ javaListDefault "" .Return }}{{end }};
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

    //In theory event listener interface

    {{- range .Interface.Properties }}
    private void on{{Camel .Name}}Changed({{javaListType "" .}} newValue)
    {
         Log.i(TAG, "on{{Camel .Name}}Changed, will pass notification to all listeners");
         fire{{Camel .Name}}Changed(newValue);
    }
    {{- end}}
    {{- range .Interface.Signals }}
    public void on{{Camel .Name}}({{javaListParams "" .Params}})
    {
        Log.i(TAG, "on{{Camel .Name}}, will pass notification to all listeners");
        fire{{Camel .Name}}({{javaVars .Params}});
    }
    {{- end }}

}