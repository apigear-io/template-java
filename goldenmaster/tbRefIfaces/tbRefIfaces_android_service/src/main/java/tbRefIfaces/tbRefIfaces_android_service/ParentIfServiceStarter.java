package tbRefIfaces.tbRefIfaces_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbRefIfaces.tbRefIfaces_api.IParentIfEventListener;
import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_android_service.ParentIfServiceAdapter;
import tbRefIfaces.tbRefIfaces_android_service.ParentIfServiceProvider;
import tbRefIfaces.tbRefIfaces_android_service.ParentIfBaseServiceLifecycleController;


// This class provides concrete implementation, for ParentIfBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service Implemented backend service from package tbRefIfaces.tbRefIfaces_impl;.
// Please see ParentIfBaseServiceLifecycleController for the details.
public class ParentIfServiceStarter
{
    private static final String TAG = "ParentIfStarter";

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

    private static final ParentIfBaseServiceLifecycleController IMPL =
    new ParentIfBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IParentIfServiceProvider getProviderInstance()
        {
            return ParentIfServiceProvider.get();
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

    public static IParentIf start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }
}
