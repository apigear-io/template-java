package testbed2.testbed2_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed2.testbed2_api.INestedStruct2InterfaceEventListener;
import testbed2.testbed2_api.INestedStruct2Interface;
import testbed2.testbed2_android_service.NestedStruct2InterfaceServiceAdapter;
import testbed2.testbed2_android_service.NestedStruct2InterfaceServiceFactory;
import testbed2.testbed2_android_service.NestedStruct2InterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for NestedStruct2InterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package testbed2.testbed2_impl;.
// Please see NestedStruct2InterfaceBaseServiceLifecycleController for the details.
public class NestedStruct2InterfaceServiceStarter
{
    private static final String TAG = "NestedStruct2InterfaceStarter";

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

    private static final NestedStruct2InterfaceBaseServiceLifecycleController IMPL =
    new NestedStruct2InterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected INestedStruct2InterfaceServiceFactory getFactoryInstance()
        {
            return NestedStruct2InterfaceServiceFactory.get();
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

    public static INestedStruct2Interface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
