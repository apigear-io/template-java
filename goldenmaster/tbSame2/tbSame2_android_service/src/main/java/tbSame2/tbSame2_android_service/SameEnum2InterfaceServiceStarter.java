package tbSame2.tbSame2_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSame2.tbSame2_api.ISameEnum2InterfaceEventListener;
import tbSame2.tbSame2_api.ISameEnum2Interface;
import tbSame2.tbSame2_android_service.SameEnum2InterfaceServiceAdapter;
import tbSame2.tbSame2_android_service.SameEnum2InterfaceServiceProvider;
import tbSame2.tbSame2_android_service.SameEnum2InterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for SameEnum2InterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package tbSame2.tbSame2_impl;.
// Please see SameEnum2InterfaceBaseServiceLifecycleController for the details.
public class SameEnum2InterfaceServiceStarter
{
    private static final String TAG = "SameEnum2InterfaceStarter";

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

    private static final SameEnum2InterfaceBaseServiceLifecycleController IMPL =
    new SameEnum2InterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected ISameEnum2InterfaceServiceProvider getProviderInstance()
        {
            return SameEnum2InterfaceServiceProvider.get();
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

    public static ISameEnum2Interface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
