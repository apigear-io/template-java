package tbSimple.tbSimplejniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.ISimpleArrayInterfaceEventListener;
import tbSimple.tbSimple_api.ISimpleArrayInterface;
import tbSimple.tbSimple_android_service.SimpleArrayInterfaceServiceAdapter;
import tbSimple.tbSimplejniservice.SimpleArrayInterfaceJniServiceProvider;
import tbSimple.tbSimple_android_service.SimpleArrayInterfaceBaseServiceLifecycleController;
import tbSimple.tbSimple_android_service.ISimpleArrayInterfaceServiceProvider;


// This class provides concrete implementation, for SimpleArrayInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see SimpleArrayInterfaceBaseServiceLifecycleController for the details.
public class SimpleArrayInterfaceJniServiceStarter
{
    private static final String TAG = "SimpleArrayInterfaceJniStarter";

    private static final SimpleArrayInterfaceBaseServiceLifecycleController IMPL =
    new SimpleArrayInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected ISimpleArrayInterfaceServiceProvider getProviderInstance()
        {
            return SimpleArrayInterfaceJniServiceProvider.get();
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

    public static ISimpleArrayInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
