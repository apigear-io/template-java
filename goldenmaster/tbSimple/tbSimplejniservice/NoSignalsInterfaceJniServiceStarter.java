package tbSimple.tbSimplejniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.INoSignalsInterfaceEventListener;
import tbSimple.tbSimple_api.INoSignalsInterface;
import tbSimple.tbSimple_android_service.NoSignalsInterfaceServiceAdapter;
import tbSimple.tbSimplejniservice.NoSignalsInterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class NoSignalsInterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "NoSignalsInterfaceJniStarter";



    public static INoSignalsInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, NoSignalsInterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        NoSignalsInterfaceJniServiceFactory factory = NoSignalsInterfaceJniServiceFactory.get();
        Log.i(TAG, "starter: factory set for NoSignalsInterfaceJniServiceFactory");
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