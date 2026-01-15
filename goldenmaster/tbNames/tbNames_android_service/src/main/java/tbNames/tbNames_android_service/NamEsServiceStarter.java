package tbNames.tbNames_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbNames.tbNames_api.INamEsEventListener;
import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_android_service.NamEsServiceAdapter;
import tbNames.tbNames_android_service.NamEsServiceFactory;
import tbNames.tbNames_android_service.NamEsBaseServiceLifecycleController;


// This class provides concrete implementation, for NamEsBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package tbNames.tbNames_impl;.
// Please see NamEsBaseServiceLifecycleController for the details.
public class NamEsServiceStarter
{
    private static final String TAG = "NamEsStarter";

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

    private static final NamEsBaseServiceLifecycleController IMPL =
    new NamEsBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected INamEsServiceFactory getFactoryInstance()
        {
            return NamEsServiceFactory.get();
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

    public static INamEs start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
