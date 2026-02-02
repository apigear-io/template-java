package tbSimple.tbSimplejniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.ISimpleInterfaceEventListener;
import tbSimple.tbSimple_api.ISimpleInterface;
import tbSimple.tbSimple_android_service.SimpleInterfaceServiceAdapter;
import tbSimple.tbSimplejniservice.SimpleInterfaceJniServiceProvider;
import tbSimple.tbSimple_android_service.SimpleInterfaceBaseServiceLifecycleController;
import tbSimple.tbSimple_android_service.ISimpleInterfaceServiceProvider;


// This class provides concrete implementation, for SimpleInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see SimpleInterfaceBaseServiceLifecycleController for the details.
public class SimpleInterfaceJniServiceStarter
{
    private static final String TAG = "SimpleInterfaceJniStarter";

    private static final SimpleInterfaceBaseServiceLifecycleController IMPL =
    new SimpleInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected ISimpleInterfaceServiceProvider getProviderInstance()
        {
            return SimpleInterfaceJniServiceProvider.get();
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

    public static ISimpleInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
