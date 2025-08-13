package {{camel .Module.Name}}.{{camel .Module.Name}}jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.{{Camel .Interface.Name }}ServiceAdapter;
import {{camel .Module.Name}}.{{camel .Module.Name}}jniservice.{{Camel .Interface.Name}}JniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class {{Camel .Interface.Name }}JniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "{{Camel .Interface.Name }}JniStarter";



    public static I{{Camel .Interface.Name }} start(Context context) {
        stop(context);
        androidService = new Intent(context, {{Camel .Interface.Name }}ServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        {{Camel .Interface.Name}}JniServiceFactory factory = {{Camel .Interface.Name}}JniServiceFactory.get();
        Log.w(TAG, "starter: factory set for {{Camel .Interface.Name}}JniServiceFactory");
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