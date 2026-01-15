package tbEnum.tbEnum_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbEnum.tbEnum_api.IEnumInterfaceEventListener;
import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_android_service.EnumInterfaceServiceAdapter;
import tbEnum.tbEnum_android_service.EnumInterfaceServiceProvider;
import tbEnum.tbEnum_android_service.EnumInterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for EnumInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package tbEnum.tbEnum_impl;.
// Please see EnumInterfaceBaseServiceLifecycleController for the details.
public class EnumInterfaceServiceStarter
{
    private static final String TAG = "EnumInterfaceStarter";

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

    private static final EnumInterfaceBaseServiceLifecycleController IMPL =
    new EnumInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IEnumInterfaceServiceProvider getProviderInstance()
        {
            return EnumInterfaceServiceProvider.get();
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

    public static IEnumInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
