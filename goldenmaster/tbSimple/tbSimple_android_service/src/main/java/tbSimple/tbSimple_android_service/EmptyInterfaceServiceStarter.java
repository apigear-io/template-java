package tbSimple.tbSimple_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.IEmptyInterfaceEventListener;
import tbSimple.tbSimple_api.IEmptyInterface;
import tbSimple.tbSimple_android_service.EmptyInterfaceServiceAdapter;
import tbSimple.tbSimple_android_service.EmptyInterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package tbSimple.tbSimple_impl; .
public class EmptyInterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "EmptyInterfaceStarter";



    public static IEmptyInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, EmptyInterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        EmptyInterfaceServiceFactory factory = EmptyInterfaceServiceFactory.get();
        Log.i(TAG, "starter: factory set for EmptyInterfaceServiceFactory");
        return EmptyInterfaceServiceAdapter.setService(factory);
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