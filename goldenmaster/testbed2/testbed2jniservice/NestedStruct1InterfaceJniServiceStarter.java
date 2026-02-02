package testbed2.testbed2jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed2.testbed2_api.INestedStruct1InterfaceEventListener;
import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_android_service.NestedStruct1InterfaceServiceAdapter;
import testbed2.testbed2jniservice.NestedStruct1InterfaceJniServiceProvider;
import testbed2.testbed2_android_service.NestedStruct1InterfaceBaseServiceLifecycleController;
import testbed2.testbed2_android_service.INestedStruct1InterfaceServiceProvider;


// This class provides concrete implementation, for NestedStruct1InterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see NestedStruct1InterfaceBaseServiceLifecycleController for the details.
public class NestedStruct1InterfaceJniServiceStarter
{
    private static final String TAG = "NestedStruct1InterfaceJniStarter";

    private static final NestedStruct1InterfaceBaseServiceLifecycleController IMPL =
    new NestedStruct1InterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected INestedStruct1InterfaceServiceProvider getProviderInstance()
        {
            return NestedStruct1InterfaceJniServiceProvider.get();
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

    public static INestedStruct1Interface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
