package testbed1.testbed1_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed1.testbed1_api.IStructArrayInterfaceEventListener;
import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_android_service.StructArrayInterfaceServiceAdapter;
import testbed1.testbed1_android_service.StructArrayInterfaceServiceFactory;
import testbed1.testbed1_android_service.StructArrayInterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for StructArrayInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package testbed1.testbed1_impl;.
// Please see StructArrayInterfaceBaseServiceLifecycleController for the details.
public class StructArrayInterfaceServiceStarter
{
    private static final String TAG = "StructArrayInterfaceStarter";

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

    private static final StructArrayInterfaceBaseServiceLifecycleController IMPL =
    new StructArrayInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IStructArrayInterfaceServiceFactory getFactoryInstance()
        {
            return StructArrayInterfaceServiceFactory.get();
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

    public static IStructArrayInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
