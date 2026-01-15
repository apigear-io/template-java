package tbRefIfaces.tbRefIfaces_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIfEventListener;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_android_service.SimpleLocalIfServiceAdapter;
import tbRefIfaces.tbRefIfaces_android_service.SimpleLocalIfServiceFactory;
import tbRefIfaces.tbRefIfaces_android_service.SimpleLocalIfBaseServiceLifecycleController;


// This class provides concrete implementation, for SimpleLocalIfBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package tbRefIfaces.tbRefIfaces_impl;.
// Please see SimpleLocalIfBaseServiceLifecycleController for the details.
public class SimpleLocalIfServiceStarter
{
    private static final String TAG = "SimpleLocalIfStarter";

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

    private static final SimpleLocalIfBaseServiceLifecycleController IMPL =
    new SimpleLocalIfBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected ISimpleLocalIfServiceFactory getFactoryInstance()
        {
            return SimpleLocalIfServiceFactory.get();
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

    public static ISimpleLocalIf start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
