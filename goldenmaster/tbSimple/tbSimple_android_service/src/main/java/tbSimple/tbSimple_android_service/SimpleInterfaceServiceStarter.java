package tbSimple.tbSimple_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.ISimpleInterfaceEventListener;
import tbSimple.tbSimple_api.ISimpleInterface;
import tbSimple.tbSimple_android_service.SimpleInterfaceServiceAdapter;
import tbSimple.tbSimple_android_service.SimpleInterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package tbSimple.tbSimple_impl; .
public class SimpleInterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "SimpleInterfaceStarter";



    public static ISimpleInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, SimpleInterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        SimpleInterfaceServiceFactory factory = SimpleInterfaceServiceFactory.get();
        Log.i(TAG, "starter: factory set for SimpleInterfaceServiceFactory");
        return SimpleInterfaceServiceAdapter.setService(factory);
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