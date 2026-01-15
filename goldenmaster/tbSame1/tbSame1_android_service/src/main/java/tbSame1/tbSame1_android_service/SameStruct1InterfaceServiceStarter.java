package tbSame1.tbSame1_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSame1.tbSame1_api.ISameStruct1InterfaceEventListener;
import tbSame1.tbSame1_api.ISameStruct1Interface;
import tbSame1.tbSame1_android_service.SameStruct1InterfaceServiceAdapter;
import tbSame1.tbSame1_android_service.SameStruct1InterfaceServiceFactory;
import tbSame1.tbSame1_android_service.SameStruct1InterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for SameStruct1InterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package tbSame1.tbSame1_impl;.
// Please see SameStruct1InterfaceBaseServiceLifecycleController for the details.
public class SameStruct1InterfaceServiceStarter
{
    private static final String TAG = "SameStruct1InterfaceStarter";

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

    private static final SameStruct1InterfaceBaseServiceLifecycleController IMPL =
    new SameStruct1InterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected ISameStruct1InterfaceServiceFactory getFactoryInstance()
        {
            return SameStruct1InterfaceServiceFactory.get();
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

    public static ISameStruct1Interface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
