package testbed2.testbed2_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed2.testbed2_api.IManyParamInterfaceEventListener;
import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_android_service.ManyParamInterfaceServiceAdapter;
import testbed2.testbed2_android_service.ManyParamInterfaceServiceFactory;
import testbed2.testbed2_android_service.ManyParamInterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for ManyParamInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package testbed2.testbed2_impl;.
// Please see ManyParamInterfaceBaseServiceLifecycleController for the details.
public class ManyParamInterfaceServiceStarter
{
    private static final String TAG = "ManyParamInterfaceStarter";

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

    private static final ManyParamInterfaceBaseServiceLifecycleController IMPL =
    new ManyParamInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IManyParamInterfaceServiceFactory getFactoryInstance()
        {
            return ManyParamInterfaceServiceFactory.get();
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

    public static IManyParamInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
