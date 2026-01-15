package tbSimple.tbSimple_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.INoPropertiesInterfaceEventListener;
import tbSimple.tbSimple_api.INoPropertiesInterface;
import tbSimple.tbSimple_android_service.NoPropertiesInterfaceServiceAdapter;
import tbSimple.tbSimple_android_service.NoPropertiesInterfaceServiceFactory;
import tbSimple.tbSimple_android_service.NoPropertiesInterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for NoPropertiesInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package tbSimple.tbSimple_impl;.
// Please see NoPropertiesInterfaceBaseServiceLifecycleController for the details.
public class NoPropertiesInterfaceServiceStarter
{
    private static final String TAG = "NoPropertiesInterfaceStarter";

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

    private static final NoPropertiesInterfaceBaseServiceLifecycleController IMPL =
    new NoPropertiesInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected INoPropertiesInterfaceServiceFactory getFactoryInstance()
        {
            return NoPropertiesInterfaceServiceFactory.get();
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

    public static INoPropertiesInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
