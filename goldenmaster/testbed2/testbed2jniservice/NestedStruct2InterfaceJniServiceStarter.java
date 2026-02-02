package testbed2.testbed2jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed2.testbed2_api.INestedStruct2InterfaceEventListener;
import testbed2.testbed2_api.INestedStruct2Interface;
import testbed2.testbed2_android_service.NestedStruct2InterfaceServiceAdapter;
import testbed2.testbed2jniservice.NestedStruct2InterfaceJniServiceProvider;
import testbed2.testbed2_android_service.NestedStruct2InterfaceBaseServiceLifecycleController;
import testbed2.testbed2_android_service.INestedStruct2InterfaceServiceProvider;


// This class provides concrete implementation, for NestedStruct2InterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see NestedStruct2InterfaceBaseServiceLifecycleController for the details.
public class NestedStruct2InterfaceJniServiceStarter
{
    private static final String TAG = "NestedStruct2InterfaceJniStarter";

    private static final NestedStruct2InterfaceBaseServiceLifecycleController IMPL =
    new NestedStruct2InterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected INestedStruct2InterfaceServiceProvider getProviderInstance()
        {
            return NestedStruct2InterfaceJniServiceProvider.get();
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

    public static INestedStruct2Interface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
