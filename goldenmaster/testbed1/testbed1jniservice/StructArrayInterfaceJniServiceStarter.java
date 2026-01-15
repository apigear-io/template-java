package testbed1.testbed1jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed1.testbed1_api.IStructArrayInterfaceEventListener;
import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_android_service.StructArrayInterfaceServiceAdapter;
import testbed1.testbed1jniservice.StructArrayInterfaceJniServiceProvider;
import testbed1.testbed1_android_service.StructArrayInterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for StructArrayInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see StructArrayInterfaceBaseServiceLifecycleController for the details.
public class StructArrayInterfaceJniServiceStarter
{
    private static final String TAG = "StructArrayInterfaceJniStarter";

    private static final StructArrayInterfaceBaseServiceLifecycleController IMPL =
    new StructArrayInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IStructArrayInterfaceServiceProvider getProviderInstance()
        {
            return StructArrayInterfaceJniServiceProvider.get();
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

    public static IStructArrayInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
