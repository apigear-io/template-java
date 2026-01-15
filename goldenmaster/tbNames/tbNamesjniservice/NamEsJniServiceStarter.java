package tbNames.tbNamesjniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbNames.tbNames_api.INamEsEventListener;
import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_android_service.NamEsServiceAdapter;
import tbNames.tbNamesjniservice.NamEsJniServiceFactory;
import tbNames.tbNames_android_service.NamEsBaseServiceLifecycleController;


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
        protected INamEsServiceFactory getFactoryInstance()
        {
            return NamEsJniServiceFactory.get();
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

    public static INamEs start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
