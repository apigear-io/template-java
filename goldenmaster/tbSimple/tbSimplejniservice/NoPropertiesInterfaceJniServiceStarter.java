package tbSimple.tbSimplejniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.INoPropertiesInterfaceEventListener;
import tbSimple.tbSimple_api.INoPropertiesInterface;
import tbSimple.tbSimple_android_service.NoPropertiesInterfaceServiceAdapter;
import tbSimple.tbSimplejniservice.NoPropertiesInterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class NoPropertiesInterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "NoPropertiesInterfaceJniStarter";



    public static INoPropertiesInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, NoPropertiesInterfaceServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        NoPropertiesInterfaceJniServiceFactory factory = NoPropertiesInterfaceJniServiceFactory.get();
        Log.w(TAG, "starter: factory set for NoPropertiesInterfaceJniServiceFactory");
        return NoPropertiesInterfaceServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        if (androidService != null)
        {
            Log.w(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}