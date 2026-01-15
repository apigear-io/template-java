package tbSame2.tbSame2_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSame2.tbSame2_api.ISameStruct2InterfaceEventListener;
import tbSame2.tbSame2_api.ISameStruct2Interface;
import tbSame2.tbSame2_android_service.SameStruct2InterfaceServiceAdapter;
import tbSame2.tbSame2_android_service.SameStruct2InterfaceServiceFactory;
import tbSame2.tbSame2_android_service.SameStruct2InterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for SameStruct2InterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package tbSame2.tbSame2_impl;.
// Please see SameStruct2InterfaceBaseServiceLifecycleController for the details.
public class SameStruct2InterfaceServiceStarter
{
    private static final String TAG = "SameStruct2InterfaceStarter";

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

    private static final SameStruct2InterfaceBaseServiceLifecycleController IMPL =
    new SameStruct2InterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected ISameStruct2InterfaceServiceFactory getFactoryInstance()
        {
            return SameStruct2InterfaceServiceFactory.get();
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

    public static ISameStruct2Interface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
