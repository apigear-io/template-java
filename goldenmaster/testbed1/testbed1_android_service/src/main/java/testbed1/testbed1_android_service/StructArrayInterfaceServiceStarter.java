package testbed1.testbed1_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed1.testbed1_api.IStructArrayInterfaceEventListener;
import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_android_service.StructArrayInterfaceServiceAdapter;
import testbed1.testbed1_android_service.StructArrayInterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package testbed1.testbed1_impl; .
public class StructArrayInterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "StructArrayInterfaceStarter";



    public static IStructArrayInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, StructArrayInterfaceServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        StructArrayInterfaceServiceFactory factory = StructArrayInterfaceServiceFactory.get();
        Log.w(TAG, "starter: factory set for StructArrayInterfaceServiceFactory");
        return StructArrayInterfaceServiceAdapter.setService(factory);
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