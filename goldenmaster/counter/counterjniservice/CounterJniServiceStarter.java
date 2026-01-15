package counter.counterjniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import counter.counter_api.ICounterEventListener;
import counter.counter_api.ICounter;
import counter.counter_android_service.CounterServiceAdapter;
import counter.counterjniservice.CounterJniServiceProvider;
import counter.counter_android_service.CounterBaseServiceLifecycleController;


// This class provides concrete implementation, for CounterBaseServiceLifecycleController,
// which describes the lifetime of an android server and controlls the provided to the server backend lifetime.
// This class sets type of backend provided to the service to Jni Bridge type.
// Please see CounterBaseServiceLifecycleController for the details.
public class CounterJniServiceStarter
{
    private static final String TAG = "CounterJniStarter";

    private static final CounterBaseServiceLifecycleController IMPL =
    new CounterBaseServiceLifecycleController()
    {
        @Override
        protected String getTag()
        {
            return TAG;
        }

        @Override
        protected ICounterServiceProvider getProviderInstance()
        {
            return CounterJniServiceProvider.get();
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

    public static ICounter start(Context ctx)
    {
        return IMPL.start(ctx);
    }

    public static void stop(Context ctx)
    {
        IMPL.stop(ctx);
    }

    private static native void nativeOnAndroidServiceConnectionStatusChanged(boolean status);
}
