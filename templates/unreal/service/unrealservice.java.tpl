package {{camel .Module.Name}}.unreal.{{camel .Module.Name}}service

import android.os.Messenger;
import android.util.Log;

import {{dot .Module.Name}}.api.I{{Camel .Interface.Name }};
import {{dot .Module.Name}}.api.Abstract{{Camel .Interface.Name}};
import {{dot .Module.Name}}.api.I{{Camel .Interface.Name }}EventListener;
// TODO import all the structs and enums


import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class Unreal{{Camel .Interface.Name}}Service extends Abstract{{Camel .Interface.Name}} {


    private final static String TAG = "Unreal{{Camel .Interface.Name}}Service";
    private static boolean isUnrealServiceReady = false;
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

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


    {{javaReturn "" .Return}} {{camel .Name}}({{javaParams "" .Params}});
    @Override
    public {{javaReturn "" .Return}} {{camel .Name}}({{javaParams "" .Params}}) {
        Log.w(TAG, "request method {{camel .Name}} called, will call native");
        return native{{Camel .Name}}({{javaVars .Params }});
    }

    @Override
    public  {{javaAsyncReturn "" .Return}} {{camel .Name}}Async({{javaParams "" .Params}}) {
        return CompletableFuture.supplyAsync(
                () -> { return {{camel .Name}}({{javaVars .Params }}); },
                executor);
    }

  {{- end }}    

    @Override
    public boolean _isReady() {
        return isUnrealServiceReady;
    }

    // Called on Unreal Service
  {{- range .Interface.Properties }}
    private static native void nativeSet{{Camel .Name}}({{javaParam "" .}});
    private static native {{javaReturn "" . }} nativeSet{{Camel .Name}}();
  {{ end }}
    // methods
  {{- range .Interface.Operations }}
    private static native {{javaReturn "" .Return}} native{{Camel .Name}}({{javaParams "" .Params}});
  {{- end }}

    // Called by Unreal Service
    public static void unrealServiceReady(boolean value) {
        isUnrealServiceReady = value;
    }

    //In theory event listner interface

    // TODO should those be static funcs? I don't think there is sense in having more than one those objects. 
    {{- range .Interface.Properties }}
    public static void on{{Camel .Name}}Changed({{javaType "" .}} newValue)
    {
         Log.i(TAG, "on{{Camel .Name}}Changed, will pass notification to all listners");
         fire{{Camel .Name}}Changed(newValue);
    }
    {{- end}}
    {{- range .Interface.Signals }}
    public static void on{{Camel .Name}}({{javaParams "" .Params}})
    {
        Log.i(TAG, "on{{Camel .Name}}, will pass notification to all listners");
        fire{{Camel .Name}}({{javaVars .Params}});
    }
    {{- end }}

}