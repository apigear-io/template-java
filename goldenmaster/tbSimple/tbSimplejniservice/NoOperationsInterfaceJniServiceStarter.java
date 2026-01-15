package tbSimple.tbSimplejniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.INoOperationsInterfaceEventListener;
import tbSimple.tbSimple_api.INoOperationsInterface;
import tbSimple.tbSimple_android_service.NoOperationsInterfaceServiceAdapter;
import tbSimple.tbSimplejniservice.NoOperationsInterfaceJniServiceFactory;
import tbSimple.tbSimple_android_service.NoOperationsInterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for NoOperationsInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see NoOperationsInterfaceBaseServiceLifecycleController for the details.
public class NoOperationsInterfaceJniServiceStarter
{
    private static final String TAG = "NoOperationsInterfaceJniStarter";

    private static final NoOperationsInterfaceBaseServiceLifecycleController IMPL =
    new NoOperationsInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected INoOperationsInterfaceServiceFactory getFactoryInstance()
        {
            return NoOperationsInterfaceJniServiceFactory.get();
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

    public static INoOperationsInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
