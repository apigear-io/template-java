package tbSimple.tbSimple_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.INoSignalsInterfaceEventListener;
import tbSimple.tbSimple_api.INoSignalsInterface;
import tbSimple.tbSimple_android_service.NoSignalsInterfaceServiceAdapter;
import tbSimple.tbSimple_android_service.NoSignalsInterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package tbSimple.tbSimple_impl; .
public class NoSignalsInterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "NoSignalsInterfaceStarter";



    public static INoSignalsInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, NoSignalsInterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        NoSignalsInterfaceServiceFactory factory = NoSignalsInterfaceServiceFactory.get();
        Log.i(TAG, "starter: factory set for NoSignalsInterfaceServiceFactory");
        return NoSignalsInterfaceServiceAdapter.setService(factory);
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