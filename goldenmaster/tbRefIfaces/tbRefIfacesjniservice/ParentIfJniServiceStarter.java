package tbRefIfaces.tbRefIfacesjniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbRefIfaces.tbRefIfaces_api.IParentIfEventListener;
import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_android_service.ParentIfServiceAdapter;
import tbRefIfaces.tbRefIfacesjniservice.ParentIfJniServiceProvider;
import tbRefIfaces.tbRefIfaces_android_service.ParentIfBaseServiceLifecycleController;
import tbRefIfaces.tbRefIfaces_android_service.IParentIfServiceProvider;


// This class provides concrete implementation, for ParentIfBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see ParentIfBaseServiceLifecycleController for the details.
public class ParentIfJniServiceStarter
{
    private static final String TAG = "ParentIfJniStarter";

    private static final ParentIfBaseServiceLifecycleController IMPL =
    new ParentIfBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IParentIfServiceProvider getProviderInstance()
        {
            return ParentIfJniServiceProvider.get();
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

    public static IParentIf start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
