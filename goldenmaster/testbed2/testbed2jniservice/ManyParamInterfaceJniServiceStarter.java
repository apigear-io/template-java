package testbed2.testbed2jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed2.testbed2_api.IManyParamInterfaceEventListener;
import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_android_service.ManyParamInterfaceServiceAdapter;
import testbed2.testbed2jniservice.ManyParamInterfaceJniServiceFactory;
import testbed2.testbed2_android_service.ManyParamInterfaceBaseServiceLifecycleController;


// This class provides concrete implementation, for ManyParamInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see ManyParamInterfaceBaseServiceLifecycleController for the details.
public class ManyParamInterfaceJniServiceStarter
{
    private static final String TAG = "ManyParamInterfaceJniStarter";

    private static final ManyParamInterfaceBaseServiceLifecycleController IMPL =
    new ManyParamInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IManyParamInterfaceServiceFactory getFactoryInstance()
        {
            return ManyParamInterfaceJniServiceFactory.get();
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

    public static IManyParamInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
