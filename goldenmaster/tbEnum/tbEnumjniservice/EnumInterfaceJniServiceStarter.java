package tbEnum.tbEnumjniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbEnum.tbEnum_api.IEnumInterfaceEventListener;
import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_android_service.EnumInterfaceServiceAdapter;
import tbEnum.tbEnumjniservice.EnumInterfaceJniServiceProvider;
import tbEnum.tbEnum_android_service.EnumInterfaceBaseServiceLifecycleController;
import tbEnum.tbEnum_android_service.IEnumInterfaceServiceProvider;


// This class provides concrete implementation, for EnumInterfaceBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see EnumInterfaceBaseServiceLifecycleController for the details.
public class EnumInterfaceJniServiceStarter
{
    private static final String TAG = "EnumInterfaceJniStarter";

    private static final EnumInterfaceBaseServiceLifecycleController IMPL =
    new EnumInterfaceBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected IEnumInterfaceServiceProvider getProviderInstance()
        {
            return EnumInterfaceJniServiceProvider.get();
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

    public static IEnumInterface start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
