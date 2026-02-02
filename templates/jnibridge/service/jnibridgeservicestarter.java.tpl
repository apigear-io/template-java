package {{camel .Module.Name}}.{{camel .Module.Name}}jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.{{Camel .Interface.Name }}ServiceAdapter;
import {{camel .Module.Name}}.{{camel .Module.Name}}jniservice.{{Camel .Interface.Name}}JniServiceProvider;
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.{{Camel .Interface.Name }}BaseServiceLifecycleController;
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.I{{Camel .Interface.Name }}ServiceProvider;


// This class provides concrete implementation, for {{Camel .Interface.Name }}BaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see {{Camel .Interface.Name }}BaseServiceLifecycleController for the details.
public class {{Camel .Interface.Name }}JniServiceStarter
{
    private static final String TAG = "{{Camel .Interface.Name }}JniStarter";

    private static final {{Camel .Interface.Name }}BaseServiceLifecycleController IMPL =
    new {{Camel .Interface.Name }}BaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected I{{Camel .Interface.Name}}ServiceProvider getProviderInstance()
        {
            return {{Camel .Interface.Name}}JniServiceProvider.get();
        }

        //Important note, onAndroidServiceConnectionStatusChanged(true) is always called when service starts,
        // but the onAndroidServiceConnectionStatusChanged(false) is called only when the service died,
        // not when it is stopped explicitly.
        @Override
        protected void onAndroidServiceConnectionStatusChanged(boolean status)
        {
            nativeOnAndroidServiceConnectionStatusChanged(status);
        }
    };

    public static I{{Camel .Interface.Name }} start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
