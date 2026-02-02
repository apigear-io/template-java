package testbed2.testbed2jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed2.testbed2_api.INestedStruct3InterfaceEventListener;
import testbed2.testbed2_api.INestedStruct3Interface;
import testbed2.testbed2_android_service.NestedStruct3InterfaceServiceAdapter;
import testbed2.testbed2jniservice.NestedStruct3InterfaceJniServiceProvider;
import testbed2.testbed2_android_service.NestedStruct3InterfaceBaseServiceLifecycleController;
import testbed2.testbed2_android_service.INestedStruct3InterfaceServiceProvider;


// This class provides concrete implementation, for NestedStruct3InterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see NestedStruct3InterfaceBaseServiceLifecycleController for the details.
public class NestedStruct3InterfaceJniServiceStarter
{
    private static final String TAG = "NestedStruct3InterfaceJniStarter";

    private static final NestedStruct3InterfaceBaseServiceLifecycleController IMPL =
    new NestedStruct3InterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected INestedStruct3InterfaceServiceProvider getProviderInstance()
        {
            return NestedStruct3InterfaceJniServiceProvider.get();
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

    public static INestedStruct3Interface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
