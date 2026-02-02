package tbSimple.tbSimplejniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.INoPropertiesInterfaceEventListener;
import tbSimple.tbSimple_api.INoPropertiesInterface;
import tbSimple.tbSimple_android_service.NoPropertiesInterfaceServiceAdapter;
import tbSimple.tbSimplejniservice.NoPropertiesInterfaceJniServiceProvider;
import tbSimple.tbSimple_android_service.NoPropertiesInterfaceBaseServiceLifecycleController;
import tbSimple.tbSimple_android_service.INoPropertiesInterfaceServiceProvider;


// This class provides concrete implementation, for NoPropertiesInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see NoPropertiesInterfaceBaseServiceLifecycleController for the details.
public class NoPropertiesInterfaceJniServiceStarter
{
    private static final String TAG = "NoPropertiesInterfaceJniStarter";

    private static final NoPropertiesInterfaceBaseServiceLifecycleController IMPL =
    new NoPropertiesInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected INoPropertiesInterfaceServiceProvider getProviderInstance()
        {
            return NoPropertiesInterfaceJniServiceProvider.get();
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

    public static INoPropertiesInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
