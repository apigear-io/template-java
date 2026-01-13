package counter.counterjniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import counter.counter_api.ICounterEventListener;
import counter.counter_api.ICounter;
import counter.counter_android_service.CounterServiceAdapter;
import counter.counterjniservice.CounterJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class CounterJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "CounterJniStarter";



    public static ICounter start(Context context) {
        stop(context);
        androidService = new Intent(context, CounterServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        CounterJniServiceFactory factory = CounterJniServiceFactory.get();
        Log.i(TAG, "starter: factory set for CounterJniServiceFactory");
        return CounterServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        CounterJniServiceFactory factory = CounterJniServiceFactory.get();
        factory.clear();
        CounterServiceAdapter.setService(null);
        if (androidService != null)
        {
            Log.i(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}
