package tbSimple.tbSimplejniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.INoOperationsInterfaceEventListener;
import tbSimple.tbSimple_api.INoOperationsInterface;
import tbSimple.tbSimple_android_service.NoOperationsInterfaceServiceAdapter;
import tbSimple.tbSimplejniservice.NoOperationsInterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class NoOperationsInterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "NoOperationsInterfaceJniStarter";



    public static INoOperationsInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, NoOperationsInterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        NoOperationsInterfaceJniServiceFactory factory = NoOperationsInterfaceJniServiceFactory.get();
        Log.i(TAG, "starter: factory set for NoOperationsInterfaceJniServiceFactory");
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