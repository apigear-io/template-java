package tbSame2.tbSame2jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSame2.tbSame2_api.ISameEnum2InterfaceEventListener;
import tbSame2.tbSame2_api.ISameEnum2Interface;
import apigear.android.lifecycle.BindingLifecycleRegistry;
import apigear.android.lifecycle.IBindingLifecycleCoordinator;
import tbSame2.tbSame2_android_service.SameEnum2InterfaceServiceAdapter;
import tbSame2.tbSame2jniservice.SameEnum2InterfaceJniServiceProvider;
import tbSame2.tbSame2_android_service.SameEnum2InterfaceBaseServiceLifecycleController;
import tbSame2.tbSame2_android_service.ISameEnum2InterfaceServiceProvider;


// This class provides concrete implementation, for SameEnum2InterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see SameEnum2InterfaceBaseServiceLifecycleController for the details.
public class SameEnum2InterfaceJniServiceStarter
{
    private static final String TAG = "SameEnum2InterfaceJniStarter";

    private static final SameEnum2InterfaceBaseServiceLifecycleController IMPL =
    new SameEnum2InterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected ISameEnum2InterfaceServiceProvider getProviderInstance()
        {
            return SameEnum2InterfaceJniServiceProvider.get();
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

    public static ISameEnum2Interface start(Context ctx)
    {
        ISameEnum2Interface result = IMPL.start(ctx);
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
