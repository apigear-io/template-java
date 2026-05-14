package testbed1.testbed1jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed1.testbed1_api.IStructArray2InterfaceEventListener;
import testbed1.testbed1_api.IStructArray2Interface;
import apigear.android.lifecycle.BindingLifecycleRegistry;
import apigear.android.lifecycle.IBindingLifecycleCoordinator;
import testbed1.testbed1_android_service.StructArray2InterfaceServiceAdapter;
import testbed1.testbed1jniservice.StructArray2InterfaceJniServiceProvider;
import testbed1.testbed1_android_service.StructArray2InterfaceBaseServiceLifecycleController;
import testbed1.testbed1_android_service.IStructArray2InterfaceServiceProvider;


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
        protected IStructArray2InterfaceServiceProvider getProviderInstance()
        {
            return StructArray2InterfaceJniServiceProvider.get();
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

    public static IStructArray2Interface start(Context ctx)
    {
        IStructArray2Interface result = IMPL.start(ctx);
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
