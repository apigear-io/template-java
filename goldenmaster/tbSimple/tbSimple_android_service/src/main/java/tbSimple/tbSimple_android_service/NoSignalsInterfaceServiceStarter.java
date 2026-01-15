package tbSimple.tbSimple_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.INoSignalsInterfaceEventListener;
import tbSimple.tbSimple_api.INoSignalsInterface;
import tbSimple.tbSimple_android_service.NoSignalsInterfaceServiceAdapter;
import tbSimple.tbSimple_android_service.NoSignalsInterfaceServiceProvider;
import tbSimple.tbSimple_android_service.NoSignalsInterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for NoSignalsInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package tbSimple.tbSimple_impl;.
// Please see NoSignalsInterfaceBaseServiceLifecycleController for the details.
public class NoSignalsInterfaceServiceStarter
{
    private static final String TAG = "NoSignalsInterfaceStarter";

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

    private static final NoSignalsInterfaceBaseServiceLifecycleController IMPL =
    new NoSignalsInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected INoSignalsInterfaceServiceProvider getProviderInstance()
        {
            return NoSignalsInterfaceServiceProvider.get();
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

    public static INoSignalsInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
