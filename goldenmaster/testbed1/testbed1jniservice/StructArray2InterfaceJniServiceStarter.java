package testbed1.testbed1jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed1.testbed1_api.IStructArray2InterfaceEventListener;
import testbed1.testbed1_api.IStructArray2Interface;
import testbed1.testbed1_android_service.StructArray2InterfaceServiceAdapter;
import testbed1.testbed1jniservice.StructArray2InterfaceJniServiceFactory;
import testbed1.testbed1_android_service.StructArray2InterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for StructArray2InterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see StructArray2InterfaceBaseServiceLifecycleController for the details.
public class StructArray2InterfaceJniServiceStarter
{
    private static final String TAG = "StructArray2InterfaceJniStarter";

    private static final StructArray2InterfaceBaseServiceLifecycleController IMPL =
    new StructArray2InterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IStructArray2InterfaceServiceFactory getFactoryInstance()
        {
            return StructArray2InterfaceJniServiceFactory.get();
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

    public static IStructArray2Interface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
