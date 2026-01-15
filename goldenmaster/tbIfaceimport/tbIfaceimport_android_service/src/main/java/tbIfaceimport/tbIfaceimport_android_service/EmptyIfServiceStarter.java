package tbIfaceimport.tbIfaceimport_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbIfaceimport.tbIfaceimport_api.IEmptyIfEventListener;
import tbIfaceimport.tbIfaceimport_api.IEmptyIf;
import tbIfaceimport.tbIfaceimport_android_service.EmptyIfServiceAdapter;
import tbIfaceimport.tbIfaceimport_android_service.EmptyIfServiceFactory;
import tbIfaceimport.tbIfaceimport_android_service.EmptyIfBaseServiceLifecycleController;


// This class provides concrete implementation, for EmptyIfBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package tbIfaceimport.tbIfaceimport_impl;.
// Please see EmptyIfBaseServiceLifecycleController for the details.
public class EmptyIfServiceStarter
{
    private static final String TAG = "EmptyIfStarter";

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

    private static final EmptyIfBaseServiceLifecycleController IMPL =
    new EmptyIfBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IEmptyIfServiceFactory getFactoryInstance()
        {
            return EmptyIfServiceFactory.get();
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

    public static IEmptyIf start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
