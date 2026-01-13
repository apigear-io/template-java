package tbSimple.tbSimplejniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.ISimpleInterfaceEventListener;
import tbSimple.tbSimple_api.ISimpleInterface;
import tbSimple.tbSimple_android_service.SimpleInterfaceServiceAdapter;
import tbSimple.tbSimplejniservice.SimpleInterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class SimpleInterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "SimpleInterfaceJniStarter";



    public static ISimpleInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, SimpleInterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        SimpleInterfaceJniServiceFactory factory = SimpleInterfaceJniServiceFactory.get();
        Log.i(TAG, "starter: factory set for SimpleInterfaceJniServiceFactory");
        return SimpleInterfaceServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        SimpleInterfaceJniServiceFactory factory = SimpleInterfaceJniServiceFactory.get();
        factory.clear();
        SimpleInterfaceServiceAdapter.setService(null);
        if (androidService != null)
        {
            Log.i(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}
