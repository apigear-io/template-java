package tbSame2.tbSame2jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSame2.tbSame2_api.ISameEnum1InterfaceEventListener;
import tbSame2.tbSame2_api.ISameEnum1Interface;
import apigear.android.lifecycle.BindingLifecycleRegistry;
import apigear.android.lifecycle.IBindingLifecycleCoordinator;
import tbSame2.tbSame2_android_service.SameEnum1InterfaceServiceAdapter;
import tbSame2.tbSame2jniservice.SameEnum1InterfaceJniServiceProvider;
import tbSame2.tbSame2_android_service.SameEnum1InterfaceBaseServiceLifecycleController;
import tbSame2.tbSame2_android_service.ISameEnum1InterfaceServiceProvider;


// This class provides concrete implementation, for SameEnum1InterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see SameEnum1InterfaceBaseServiceLifecycleController for the details.
public class SameEnum1InterfaceJniServiceStarter
{
    private static final String TAG = "SameEnum1InterfaceJniStarter";

    private static final SameEnum1InterfaceBaseServiceLifecycleController IMPL =
    new SameEnum1InterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected ISameEnum1InterfaceServiceProvider getProviderInstance()
        {
            return SameEnum1InterfaceJniServiceProvider.get();
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

    public static ISameEnum1Interface start(Context ctx)
    {
        ISameEnum1Interface result = IMPL.start(ctx);
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
