package testbed2.testbed2_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed2.testbed2_api.INestedStruct1InterfaceEventListener;
import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_android_service.NestedStruct1InterfaceServiceAdapter;
import testbed2.testbed2_android_service.NestedStruct1InterfaceServiceProvider;
import testbed2.testbed2_android_service.NestedStruct1InterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for NestedStruct1InterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package testbed2.testbed2_impl;.
// Please see NestedStruct1InterfaceBaseServiceLifecycleController for the details.
public class NestedStruct1InterfaceServiceStarter
{
    private static final String TAG = "NestedStruct1InterfaceStarter";

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

    private static final NestedStruct1InterfaceBaseServiceLifecycleController IMPL =
    new NestedStruct1InterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected INestedStruct1InterfaceServiceProvider getProviderInstance()
        {
            return NestedStruct1InterfaceServiceProvider.get();
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

    public static INestedStruct1Interface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
