package tbSimple.tbSimple_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.IVoidInterfaceEventListener;
import tbSimple.tbSimple_api.IVoidInterface;
import tbSimple.tbSimple_android_service.VoidInterfaceServiceAdapter;
import tbSimple.tbSimple_android_service.VoidInterfaceServiceFactory;
import tbSimple.tbSimple_android_service.VoidInterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for VoidInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package tbSimple.tbSimple_impl;.
// Please see VoidInterfaceBaseServiceLifecycleController for the details.
public class VoidInterfaceServiceStarter
{
    private static final String TAG = "VoidInterfaceStarter";

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

    private static final VoidInterfaceBaseServiceLifecycleController IMPL =
    new VoidInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IVoidInterfaceServiceFactory getFactoryInstance()
        {
            return VoidInterfaceServiceFactory.get();
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

    public static IVoidInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
