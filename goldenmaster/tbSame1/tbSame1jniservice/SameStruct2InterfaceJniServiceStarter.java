package tbSame1.tbSame1jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSame1.tbSame1_api.ISameStruct2InterfaceEventListener;
import tbSame1.tbSame1_api.ISameStruct2Interface;
import tbSame1.tbSame1_android_service.SameStruct2InterfaceServiceAdapter;
import tbSame1.tbSame1jniservice.SameStruct2InterfaceJniServiceFactory;
import tbSame1.tbSame1_android_service.SameStruct2InterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for SameStruct2InterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see SameStruct2InterfaceBaseServiceLifecycleController for the details.
public class SameStruct2InterfaceJniServiceStarter
{
    private static final String TAG = "SameStruct2InterfaceJniStarter";

    private static final SameStruct2InterfaceBaseServiceLifecycleController IMPL =
    new SameStruct2InterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected ISameStruct2InterfaceServiceFactory getFactoryInstance()
        {
            return SameStruct2InterfaceJniServiceFactory.get();
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

    public static ISameStruct2Interface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
