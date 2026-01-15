package tbRefIfaces.tbRefIfacesjniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIfEventListener;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_android_service.SimpleLocalIfServiceAdapter;
import tbRefIfaces.tbRefIfacesjniservice.SimpleLocalIfJniServiceFactory;
import tbRefIfaces.tbRefIfaces_android_service.SimpleLocalIfBaseServiceLifecycleController;


// This class provides concrete implementation, for SimpleLocalIfBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see SimpleLocalIfBaseServiceLifecycleController for the details.
public class SimpleLocalIfJniServiceStarter
{
    private static final String TAG = "SimpleLocalIfJniStarter";

    private static final SimpleLocalIfBaseServiceLifecycleController IMPL =
    new SimpleLocalIfBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected ISimpleLocalIfServiceFactory getFactoryInstance()
        {
            return SimpleLocalIfJniServiceFactory.get();
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

    public static ISimpleLocalIf start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
