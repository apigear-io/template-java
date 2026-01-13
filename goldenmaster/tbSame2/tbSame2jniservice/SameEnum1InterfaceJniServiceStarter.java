package tbSame2.tbSame2jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSame2.tbSame2_api.ISameEnum1InterfaceEventListener;
import tbSame2.tbSame2_api.ISameEnum1Interface;
import tbSame2.tbSame2_android_service.SameEnum1InterfaceServiceAdapter;
import tbSame2.tbSame2jniservice.SameEnum1InterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class SameEnum1InterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "SameEnum1InterfaceJniStarter";



    public static ISameEnum1Interface start(Context context) {
        stop(context);
        androidService = new Intent(context, SameEnum1InterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        SameEnum1InterfaceJniServiceFactory factory = SameEnum1InterfaceJniServiceFactory.get();
        Log.i(TAG, "starter: factory set for SameEnum1InterfaceJniServiceFactory");
        return SameEnum1InterfaceServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        SameEnum1InterfaceJniServiceFactory factory = SameEnum1InterfaceJniServiceFactory.get();
        factory.clear();
        SameEnum1InterfaceServiceAdapter.setService(null);
        if (androidService != null)
        {
            Log.i(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}
