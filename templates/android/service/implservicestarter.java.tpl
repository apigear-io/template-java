package {{camel .Module.Name}}.{{camel .Module.Name}}_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }};
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.{{Camel .Interface.Name }}ServiceAdapter;
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.{{Camel .Interface.Name}}ServiceFactory;
import {{camel .Module.Name}}.{{camel .Module.Name}}_android_service.{{Camel .Interface.Name }}BaseServiceLifecycleController;


// This class provides concrete implementation, for {{Camel .Interface.Name }}BaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package {{camel .Module.Name}}.{{camel .Module.Name}}_impl;.
// Please see {{Camel .Interface.Name }}BaseServiceLifecycleController for the details.
public class {{Camel .Interface.Name }}ServiceStarter
{
    private static final String TAG = "{{Camel .Interface.Name }}Starter";

    public interface ServiceLifecycleListener
    {
        // Called when service connects successfully.
        void onServiceConnected();

        // Called when service is killed by Android or crashed, not when stopped.
        void onServiceDied();
    }

    private static ServiceLifecycleListener sListener = null;

    public static void setServiceLifecycleListener(ServiceLifecycleListener listener)
    {
        sListener = listener;
    }

    private static final {{Camel .Interface.Name }}BaseServiceLifecycleController IMPL =
    new {{Camel .Interface.Name }}BaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected I{{Camel .Interface.Name}}ServiceFactory getFactoryInstance()
        {
            return {{Camel .Interface.Name}}ServiceFactory.get();
        }

        @Override
        protected void onAndroidServiceConnectionStatusChanged(boolean status)
        {
            if (sListener != null)
            {
                if (status)
                {
                    sListener.onServiceConnected();
                }
                else
                {
                    sListener.onServiceDied();
                }
            }
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
}
