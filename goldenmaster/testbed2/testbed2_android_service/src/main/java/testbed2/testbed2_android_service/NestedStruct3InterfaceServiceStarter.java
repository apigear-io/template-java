package testbed2.testbed2_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed2.testbed2_api.INestedStruct3InterfaceEventListener;
import testbed2.testbed2_api.INestedStruct3Interface;
import testbed2.testbed2_android_service.NestedStruct3InterfaceServiceAdapter;
import testbed2.testbed2_android_service.NestedStruct3InterfaceServiceProvider;
import testbed2.testbed2_android_service.NestedStruct3InterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for NestedStruct3InterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package testbed2.testbed2_impl;.
// Please see NestedStruct3InterfaceBaseServiceLifecycleController for the details.
public class NestedStruct3InterfaceServiceStarter
{
    private static final String TAG = "NestedStruct3InterfaceStarter";

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

    private static final NestedStruct3InterfaceBaseServiceLifecycleController IMPL =
    new NestedStruct3InterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected INestedStruct3InterfaceServiceProvider getProviderInstance()
        {
            return NestedStruct3InterfaceServiceProvider.get();
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

    public static INestedStruct3Interface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
