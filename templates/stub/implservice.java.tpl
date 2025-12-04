package {{camel .Module.Name}}.{{camel .Module.Name}}_impl;

import android.os.Messenger;
import android.util.Log;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.Abstract{{Camel .Interface.Name}};
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
{{- $typesToImport := getEmptyStringList}}
{{- $interfacesToImport := getEmptyStringList}}
{{- $module := camel .Module.Name}}
{{- range .Interface.Properties }}
    {{- if and (not .Schema.Import)  (not .IsPrimitive)  }}
{{- $type :=  Camel .Type }}
        {{- if eq .KindType "interface" }}
{{- $interfacesToImport = (appendList $typesToImport $type) }}
        {{- else }}
{{- $typesToImport = (appendList $typesToImport $type) }}
        {{- end }}
    {{- end }}
{{- end }}
{{- range .Interface.Operations }}
    {{- range .Params }}
    {{- if and (not .Schema.Import)  (not .IsPrimitive)  }}
{{- $type :=  Camel .Type }}
        {{- if eq .KindType "interface" }}
{{- $interfacesToImport = (appendList $typesToImport $type) }}
        {{- else }}
{{- $typesToImport = (appendList $typesToImport $type) }}
        {{- end }}
    {{- end }}
    {{- end }}
    {{- if and (and (not .Return.Schema.Import)  (not .Return.IsPrimitive))  (not .Return.IsVoid) }}
{{- $type :=  Camel .Return.Type }}
        {{- if eq .Return.KindType "interface" }}
{{- $interfacesToImport = (appendList $typesToImport $type) }}
        {{- else }}
{{- $typesToImport = (appendList $typesToImport $type) }}
        {{- end }}
    {{- end }}
{{- end }}
{{- range .Interface.Signals }}
    {{- range .Params }}
    {{- if and (not .Schema.Import)  (not .IsPrimitive)  }}
{{- $type :=  Camel .Type }}
        {{- if eq .KindType "interface" }}
{{- $interfacesToImport = (appendList $typesToImport $type) }}
        {{- else }}
{{- $typesToImport = (appendList $typesToImport $type) }}
        {{- end }}
    {{- end }}
    {{- end }}
{{- end }}
{{- $typesToImport = unique $typesToImport }}
{{- $interfacesToImport = unique $interfacesToImport }}
{{- range $typesToImport}}
import {{$module}}.{{$module}}_api.{{.}};
{{- end}}
{{- range $interfacesToImport}}
import {{$module}}.{{$module}}_api.I{{.}};
import {{$module}}.{{$module}}_impl.{{.}}Service;
{{- end}}


import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.Arrays;


public class {{Camel .Interface.Name}}Service extends Abstract{{Camel .Interface.Name}} {

    private final static String TAG = "{{Camel .Interface.Name}}Service";
    private static boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    {{- range .Interface.Properties }}
    private {{javaReturn "" .}} m_{{javaVar  .}} = {{ javaDefault "" . }};
    {{- end}}

    public {{Camel .Interface.Name}}Service()
    {
        fire_readyStatusChanged(true);
    }

{{- range .Interface.Properties }}
    @Override
    public void set{{Camel .Name}}({{javaParam "" .}})
    {
        Log.i(TAG, "request set{{Camel .Name}} called ");
        {{- if .IsArray }}
        if (! Arrays.equals(m_{{javaVar  .}}, {{javaVar  .}}))
        {{- else if or (or .IsPrimitive  (eq .KindType "enum")) (eq .KindType "interface") }}
        if (m_{{javaVar  .}} != {{javaVar  .}})
        {{- else }}
        if ( (m_{{javaVar  .}} != null && ! m_{{javaVar  .}}.equals({{javaVar  .}}))
        || (m_{{javaVar  .}} == null && {{javaVar  .}} != null ))
        {{- end}}
        {
            m_{{javaVar  .}} = {{javaVar  .}};
            on{{Camel .Name}}Changed(m_{{javaVar  .}});
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
        Log.i(TAG, "request method {{camel .Name}} called, returnig default");
        return {{ if not .Return.IsVoid }}{{ javaDefault "" .Return }}{{end }};
    }

    @Override
    public  {{javaAsyncReturn "" .Return}} {{camel .Name}}Async({{javaParams "" .Params}}) {
        return CompletableFuture.{{- if .Return.IsVoid }}runAsync{{else}}supplyAsync{{end}}(
                () -> {     {{- if not .Return.IsVoid }}return{{end}} {{camel .Name}}({{javaVars .Params }}); },
                executor);
    }

  {{- end }}    

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    //In theory event listener interface

    {{- range .Interface.Properties }}
    private void on{{Camel .Name}}Changed({{javaType "" .}} newValue)
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