package counter.counter_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import counter.counter_api.ICounterEventListener;
import counter.counter_api.ICounter;
import counter.counter_android_service.CounterServiceAdapter;
import counter.counter_android_service.CounterServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package counter.counter_impl; .
public class CounterServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "CounterStarter";



    public static ICounter start(Context context) {
        stop(context);
        androidService = new Intent(context, CounterServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        CounterServiceFactory factory = CounterServiceFactory.get();
        Log.i(TAG, "starter: factory set for CounterServiceFactory");
        return CounterServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        if (androidService != null)
        {
            Log.i(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}