package tbSimple.tbSimplejniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.ISimpleArrayInterfaceEventListener;
import tbSimple.tbSimple_api.ISimpleArrayInterface;
import tbSimple.tbSimple_android_service.SimpleArrayInterfaceServiceAdapter;
import tbSimple.tbSimplejniservice.SimpleArrayInterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class SimpleArrayInterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "SimpleArrayInterfaceJniStarter";



    public static ISimpleArrayInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, SimpleArrayInterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        SimpleArrayInterfaceJniServiceFactory factory = SimpleArrayInterfaceJniServiceFactory.get();
        Log.i(TAG, "starter: factory set for SimpleArrayInterfaceJniServiceFactory");
        return SimpleArrayInterfaceServiceAdapter.setService(factory);
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