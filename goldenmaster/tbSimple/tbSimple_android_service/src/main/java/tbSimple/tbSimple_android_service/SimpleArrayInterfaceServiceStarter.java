package tbSimple.tbSimple_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.ISimpleArrayInterfaceEventListener;
import tbSimple.tbSimple_api.ISimpleArrayInterface;
import tbSimple.tbSimple_android_service.SimpleArrayInterfaceServiceAdapter;
import tbSimple.tbSimple_android_service.SimpleArrayInterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package tbSimple.tbSimple_impl; .
public class SimpleArrayInterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "SimpleArrayInterfaceStarter";

    public static ISimpleArrayInterface start(Context context)
    {
        stop(context);
        androidService = new Intent(context, SimpleArrayInterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        SimpleArrayInterfaceServiceFactory factory = SimpleArrayInterfaceServiceFactory.get();
        Log.i(TAG, "starter: factory set for SimpleArrayInterfaceServiceFactory");
        return SimpleArrayInterfaceServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        SimpleArrayInterfaceServiceFactory factory = SimpleArrayInterfaceServiceFactory.get();
        factory.clear();
        SimpleArrayInterfaceServiceAdapter.setService(null);
        if (androidService != null)
        {
            Log.i(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}
