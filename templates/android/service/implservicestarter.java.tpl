package {{camel .Module.Name}}.{{camel .Module.Name}}_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.{{Camel .Interface.Name }}ServiceAdapter;
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.{{Camel .Interface.Name}}ServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package {{camel .Module.Name}}.{{camel .Module.Name}}_impl; .
public class {{Camel .Interface.Name }}ServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "{{Camel .Interface.Name }}Starter";



    public static I{{Camel .Interface.Name }} start(Context context) {
        stop(context);
        androidService = new Intent(context, {{Camel .Interface.Name }}ServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        {{Camel .Interface.Name}}ServiceFactory factory = {{Camel .Interface.Name}}ServiceFactory.get();
        Log.w(TAG, "starter: factory set for {{Camel .Interface.Name}}ServiceFactory");
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