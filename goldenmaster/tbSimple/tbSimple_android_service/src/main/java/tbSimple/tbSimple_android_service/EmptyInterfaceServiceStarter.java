package tbSimple.tbSimple_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.IEmptyInterfaceEventListener;
import tbSimple.tbSimple_api.IEmptyInterface;
import tbSimple.tbSimple_android_service.EmptyInterfaceServiceAdapter;
import tbSimple.tbSimple_android_service.EmptyInterfaceServiceProvider;
import tbSimple.tbSimple_android_service.EmptyInterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for EmptyInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package tbSimple.tbSimple_impl;.
// Please see EmptyInterfaceBaseServiceLifecycleController for the details.
public class EmptyInterfaceServiceStarter
{
    private static final String TAG = "EmptyInterfaceStarter";

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

    private static final EmptyInterfaceBaseServiceLifecycleController IMPL =
    new EmptyInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IEmptyInterfaceServiceProvider getProviderInstance()
        {
            return EmptyInterfaceServiceProvider.get();
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

    public static IEmptyInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
