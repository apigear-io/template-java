package tbSimple.tbSimplejniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.IEmptyInterfaceEventListener;
import tbSimple.tbSimple_api.IEmptyInterface;
import tbSimple.tbSimple_android_service.EmptyInterfaceServiceAdapter;
import tbSimple.tbSimplejniservice.EmptyInterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class EmptyInterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "EmptyInterfaceJniStarter";



    public static IEmptyInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, EmptyInterfaceServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        EmptyInterfaceJniServiceFactory factory = EmptyInterfaceJniServiceFactory.get();
        Log.w(TAG, "starter: factory set for EmptyInterfaceJniServiceFactory");
        return EmptyInterfaceServiceAdapter.setService(factory);
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