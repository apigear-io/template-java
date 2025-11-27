package tbSimple.tbSimple_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.INoOperationsInterfaceEventListener;
import tbSimple.tbSimple_api.INoOperationsInterface;
import tbSimple.tbSimple_android_service.NoOperationsInterfaceServiceAdapter;
import tbSimple.tbSimple_android_service.NoOperationsInterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package tbSimple.tbSimple_impl; .
public class NoOperationsInterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "NoOperationsInterfaceStarter";



    public static INoOperationsInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, NoOperationsInterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        NoOperationsInterfaceServiceFactory factory = NoOperationsInterfaceServiceFactory.get();
        Log.i(TAG, "starter: factory set for NoOperationsInterfaceServiceFactory");
        return NoOperationsInterfaceServiceAdapter.setService(factory);
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