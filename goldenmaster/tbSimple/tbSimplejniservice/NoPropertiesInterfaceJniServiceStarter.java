package tbSimple.tbSimplejniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.INoPropertiesInterfaceEventListener;
import tbSimple.tbSimple_api.INoPropertiesInterface;
import apigear.android.lifecycle.BindingLifecycleRegistry;
import apigear.android.lifecycle.IBindingLifecycleCoordinator;
import tbSimple.tbSimple_android_service.NoPropertiesInterfaceServiceAdapter;
import tbSimple.tbSimplejniservice.NoPropertiesInterfaceJniServiceProvider;
import tbSimple.tbSimple_android_service.NoPropertiesInterfaceBaseServiceLifecycleController;
import tbSimple.tbSimple_android_service.INoPropertiesInterfaceServiceProvider;


// This class provides concrete implementation, for NoPropertiesInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see NoPropertiesInterfaceBaseServiceLifecycleController for the details.
public class NoPropertiesInterfaceJniServiceStarter
{
    private static final String TAG = "NoPropertiesInterfaceJniStarter";

    private static final NoPropertiesInterfaceBaseServiceLifecycleController IMPL =
    new NoPropertiesInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected INoPropertiesInterfaceServiceProvider getProviderInstance()
        {
            return NoPropertiesInterfaceJniServiceProvider.get();
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

    public static INoPropertiesInterface start(Context ctx)
    {
        INoPropertiesInterface result = IMPL.start(ctx);
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
