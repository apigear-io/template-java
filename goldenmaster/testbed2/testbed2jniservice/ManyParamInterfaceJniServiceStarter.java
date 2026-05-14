package testbed2.testbed2jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed2.testbed2_api.IManyParamInterfaceEventListener;
import testbed2.testbed2_api.IManyParamInterface;
import apigear.android.lifecycle.BindingLifecycleRegistry;
import apigear.android.lifecycle.IBindingLifecycleCoordinator;
import testbed2.testbed2_android_service.ManyParamInterfaceServiceAdapter;
import testbed2.testbed2jniservice.ManyParamInterfaceJniServiceProvider;
import testbed2.testbed2_android_service.ManyParamInterfaceBaseServiceLifecycleController;
import testbed2.testbed2_android_service.IManyParamInterfaceServiceProvider;


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
        protected IManyParamInterfaceServiceProvider getProviderInstance()
        {
            return ManyParamInterfaceJniServiceProvider.get();
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

    // Stable Runnable reference for coordinator register/unregister. Static
    // because the starter itself is a static facade over a single IMPL.
    private static Runnable sCleanup;

    public static IManyParamInterface start(Context ctx)
    {
        IManyParamInterface result = IMPL.start(ctx);
        if (result != null)
        {
            // Re-entrant start: drop any previous registration before replacing.
            IBindingLifecycleCoordinator coordinator = BindingLifecycleRegistry.get();
            if (sCleanup != null && coordinator != null)
            {
                coordinator.unregisterCleanup(sCleanup);
            }
            final Context capturedCtx = ctx;
            sCleanup = () -> stop(capturedCtx);
            if (coordinator != null)
            {
                coordinator.registerCleanup(sCleanup);
            }
        }
        return result;
    }

    public static void stop(Context ctx)
    {
        if (sCleanup != null)
        {
            IBindingLifecycleCoordinator coordinator = BindingLifecycleRegistry.get();
            if (coordinator != null)
            {
                coordinator.unregisterCleanup(sCleanup);
            }
            sCleanup = null;
        }
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
