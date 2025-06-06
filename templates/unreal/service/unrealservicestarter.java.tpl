package {{camel .Module.Name}}.unreal.{{camel .Module.Name}}service

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import {{dot .Module.Name}}.api.I{{Camel .Interface.Name }}EventListener;
import {{dot .Module.Name}}.api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.android.service.{{Camel .Interface.Name }}ServiceAdapter;
import {{camel .Module.Name}}.unreal.{{camel .Module.Name}}service.Unreal{{Camel .Interface.Name}}ServiceFactory;

public class Unreal{{Camel .Interface.Name }}ServiceStarter {

    static Intent unreal_service = null;
    private static final String TAG = "Unreal{{Camel .Interface.Name }}Starter";

    public static void start(Context context) {
        stop(context);
        unreal_service = new Intent(context, {{Camel .Interface.Name }}ServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(unreal_service);
        Log.w(TAG, "starter: started intent (service) ");
        {{Camel .Interface.Name }}ServiceAdapter.setServiceFactory(Unreal{{Camel .Interface.Name}}ServiceFactory.get());
        Log.w(TAG, "starter: factory set for Unreal{{Camel .Interface.Name}}ServiceFactory");
    }

    public static void stop(Context context)
    {
        if (unreal_service != null)
        {
            Log.w(TAG, "starter: stop the service");
            context.stopService(unreal_service);
        }
        unreal_service = null;
    }

}