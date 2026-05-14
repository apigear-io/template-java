package tbSimple.tbSimplejniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.IVoidInterfaceEventListener;
import tbSimple.tbSimple_api.IVoidInterface;
import apigear.android.lifecycle.BindingLifecycleRegistry;
import apigear.android.lifecycle.IBindingLifecycleCoordinator;
import tbSimple.tbSimple_android_service.VoidInterfaceServiceAdapter;
import tbSimple.tbSimplejniservice.VoidInterfaceJniServiceProvider;
import tbSimple.tbSimple_android_service.VoidInterfaceBaseServiceLifecycleController;
import tbSimple.tbSimple_android_service.IVoidInterfaceServiceProvider;


// This class provides concrete implementation, for VoidInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see VoidInterfaceBaseServiceLifecycleController for the details.
public class VoidInterfaceJniServiceStarter
{
    private static final String TAG = "VoidInterfaceJniStarter";

    private static final VoidInterfaceBaseServiceLifecycleController IMPL =
    new VoidInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IVoidInterfaceServiceProvider getProviderInstance()
        {
            return VoidInterfaceJniServiceProvider.get();
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

    public static IVoidInterface start(Context ctx)
    {
        IVoidInterface result = IMPL.start(ctx);
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
