package tbSimple.tbSimplejniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.INoSignalsInterfaceEventListener;
import tbSimple.tbSimple_api.INoSignalsInterface;
import tbSimple.tbSimple_android_service.NoSignalsInterfaceServiceAdapter;
import tbSimple.tbSimplejniservice.NoSignalsInterfaceJniServiceProvider;
import tbSimple.tbSimple_android_service.NoSignalsInterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for NoSignalsInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see NoSignalsInterfaceBaseServiceLifecycleController for the details.
public class NoSignalsInterfaceJniServiceStarter
{
    private static final String TAG = "NoSignalsInterfaceJniStarter";

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
            return NoSignalsInterfaceJniServiceProvider.get();
        }

        //Important note, onAndroidServiceConnectionStatusChanged(true) is always called when service starts,
        // but the onAndroidServiceConnectionStatusChanged(false) is called only when the service died,
        // not when it is stopped explicitly.
        @Override
        protected void onAndroidServiceConnectionStatusChanged(boolean status)
        {
            nativeOnAndroidServiceConnectionStatusChanged(status);
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

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
