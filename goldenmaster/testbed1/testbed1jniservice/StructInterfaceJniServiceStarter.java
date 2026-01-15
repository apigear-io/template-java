package testbed1.testbed1jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed1.testbed1_api.IStructInterfaceEventListener;
import testbed1.testbed1_api.IStructInterface;
import testbed1.testbed1_android_service.StructInterfaceServiceAdapter;
import testbed1.testbed1jniservice.StructInterfaceJniServiceFactory;
import testbed1.testbed1_android_service.StructInterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for StructInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see StructInterfaceBaseServiceLifecycleController for the details.
public class StructInterfaceJniServiceStarter
{
    private static final String TAG = "StructInterfaceJniStarter";

    private static final StructInterfaceBaseServiceLifecycleController IMPL =
    new StructInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IStructInterfaceServiceFactory getFactoryInstance()
        {
            return StructInterfaceJniServiceFactory.get();
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

    public static IStructInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
