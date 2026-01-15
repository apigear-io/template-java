package tbSimple.tbSimplejniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.IVoidInterfaceEventListener;
import tbSimple.tbSimple_api.IVoidInterface;
import tbSimple.tbSimple_android_service.VoidInterfaceServiceAdapter;
import tbSimple.tbSimplejniservice.VoidInterfaceJniServiceProvider;
import tbSimple.tbSimple_android_service.VoidInterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for VoidInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see VoidInterfaceBaseServiceLifecycleController for the details.
public class VoidInterfaceJniServiceStarter
{
    private static final String TAG = "VoidInterfaceJniStarter";

    private static final VoidInterfaceBaseServiceLifecycleController IMPL =
    new VoidInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IVoidInterfaceServiceProvider getProviderInstance()
        {
            return VoidInterfaceJniServiceProvider.get();
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

    public static IVoidInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
