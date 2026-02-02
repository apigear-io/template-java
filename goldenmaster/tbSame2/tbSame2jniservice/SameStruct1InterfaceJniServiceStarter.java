package tbSame2.tbSame2jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSame2.tbSame2_api.ISameStruct1InterfaceEventListener;
import tbSame2.tbSame2_api.ISameStruct1Interface;
import tbSame2.tbSame2_android_service.SameStruct1InterfaceServiceAdapter;
import tbSame2.tbSame2jniservice.SameStruct1InterfaceJniServiceProvider;
import tbSame2.tbSame2_android_service.SameStruct1InterfaceBaseServiceLifecycleController;
import tbSame2.tbSame2_android_service.ISameStruct1InterfaceServiceProvider;


// This class provides concrete implementation, for SameStruct1InterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see SameStruct1InterfaceBaseServiceLifecycleController for the details.
public class SameStruct1InterfaceJniServiceStarter
{
    private static final String TAG = "SameStruct1InterfaceJniStarter";

    private static final SameStruct1InterfaceBaseServiceLifecycleController IMPL =
    new SameStruct1InterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected ISameStruct1InterfaceServiceProvider getProviderInstance()
        {
            return SameStruct1InterfaceJniServiceProvider.get();
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

    public static ISameStruct1Interface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
