package tbNames.tbNamesjniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbNames.tbNames_api.INamEsEventListener;
import tbNames.tbNames_api.INamEs;
import apigear.android.lifecycle.BindingLifecycleRegistry;
import apigear.android.lifecycle.IBindingLifecycleCoordinator;
import tbNames.tbNames_android_service.NamEsServiceAdapter;
import tbNames.tbNamesjniservice.NamEsJniServiceProvider;
import tbNames.tbNames_android_service.NamEsBaseServiceLifecycleController;
import tbNames.tbNames_android_service.INamEsServiceProvider;


// This class provides concrete implementation, for NamEsBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see NamEsBaseServiceLifecycleController for the details.
public class NamEsJniServiceStarter
{
    private static final String TAG = "NamEsJniStarter";

    private static final NamEsBaseServiceLifecycleController IMPL =
    new NamEsBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected INamEsServiceProvider getProviderInstance()
        {
            return NamEsJniServiceProvider.get();
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

    public static INamEs start(Context ctx)
    {
        INamEs result = IMPL.start(ctx);
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
