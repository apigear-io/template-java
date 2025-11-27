package tbRefIfaces.tbRefIfaces_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIfEventListener;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_android_service.SimpleLocalIfServiceAdapter;
import tbRefIfaces.tbRefIfaces_android_service.SimpleLocalIfServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package tbRefIfaces.tbRefIfaces_impl; .
public class SimpleLocalIfServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "SimpleLocalIfStarter";



    public static ISimpleLocalIf start(Context context) {
        stop(context);
        androidService = new Intent(context, SimpleLocalIfServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        SimpleLocalIfServiceFactory factory = SimpleLocalIfServiceFactory.get();
        Log.i(TAG, "starter: factory set for SimpleLocalIfServiceFactory");
        return SimpleLocalIfServiceAdapter.setService(factory);
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