package {{camel .Module.Name}}.unreal_{{camel .Module.Name}}service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.{{Camel .Interface.Name }}ServiceAdapter;
import {{camel .Module.Name}}.unreal_{{camel .Module.Name}}service.Unreal{{Camel .Interface.Name}}ServiceFactory;


//Use this class to manage lifetime of android server with unreal backend service.
public class Unreal{{Camel .Interface.Name }}ServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "Unreal{{Camel .Interface.Name }}Starter";



    public static I{{Camel .Interface.Name }} start(Context context) {
        stop(context);
        androidService = new Intent(context, {{Camel .Interface.Name }}ServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        Unreal{{Camel .Interface.Name}}ServiceFactory factory = Unreal{{Camel .Interface.Name}}ServiceFactory.get();
        Log.w(TAG, "starter: factory set for Unreal{{Camel .Interface.Name}}ServiceFactory");
        return {{Camel .Interface.Name }}ServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        if (androidService != null)
        {
            Log.w(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}