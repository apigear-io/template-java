package testbed1.testbed1_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed1.testbed1_api.IStructArray2InterfaceEventListener;
import testbed1.testbed1_api.IStructArray2Interface;
import testbed1.testbed1_android_service.StructArray2InterfaceServiceAdapter;
import testbed1.testbed1_android_service.StructArray2InterfaceServiceProvider;
import testbed1.testbed1_android_service.StructArray2InterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for StructArray2InterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package testbed1.testbed1_impl;.
// Please see StructArray2InterfaceBaseServiceLifecycleController for the details.
public class StructArray2InterfaceServiceStarter
{
    private static final String TAG = "StructArray2InterfaceStarter";

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

    private static final StructArray2InterfaceBaseServiceLifecycleController IMPL =
    new StructArray2InterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IStructArray2InterfaceServiceProvider getProviderInstance()
        {
            return StructArray2InterfaceServiceProvider.get();
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

    public static IStructArray2Interface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
