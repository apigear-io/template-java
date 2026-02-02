package tbIfaceimport.tbIfaceimportjniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbIfaceimport.tbIfaceimport_api.IEmptyIfEventListener;
import tbIfaceimport.tbIfaceimport_api.IEmptyIf;
import tbIfaceimport.tbIfaceimport_android_service.EmptyIfServiceAdapter;
import tbIfaceimport.tbIfaceimportjniservice.EmptyIfJniServiceProvider;
import tbIfaceimport.tbIfaceimport_android_service.EmptyIfBaseServiceLifecycleController;
import tbIfaceimport.tbIfaceimport_android_service.IEmptyIfServiceProvider;


// This class provides concrete implementation, for EmptyIfBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see EmptyIfBaseServiceLifecycleController for the details.
public class EmptyIfJniServiceStarter
{
    private static final String TAG = "EmptyIfJniStarter";

    private static final EmptyIfBaseServiceLifecycleController IMPL =
    new EmptyIfBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IEmptyIfServiceProvider getProviderInstance()
        {
            return EmptyIfJniServiceProvider.get();
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

    public static IEmptyIf start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
