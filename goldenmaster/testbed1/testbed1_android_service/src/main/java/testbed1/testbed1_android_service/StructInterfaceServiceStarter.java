package testbed1.testbed1_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed1.testbed1_api.IStructInterfaceEventListener;
import testbed1.testbed1_api.IStructInterface;
import testbed1.testbed1_android_service.StructInterfaceServiceAdapter;
import testbed1.testbed1_android_service.StructInterfaceServiceFactory;
import testbed1.testbed1_android_service.StructInterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for StructInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package testbed1.testbed1_impl;.
// Please see StructInterfaceBaseServiceLifecycleController for the details.
public class StructInterfaceServiceStarter
{
    private static final String TAG = "StructInterfaceStarter";

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

    private static final StructInterfaceBaseServiceLifecycleController IMPL =
    new StructInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IStructInterfaceServiceFactory getFactoryInstance()
        {
            return StructInterfaceServiceFactory.get();
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

    public static IStructInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
