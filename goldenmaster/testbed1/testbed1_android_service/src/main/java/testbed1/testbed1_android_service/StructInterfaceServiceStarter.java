package testbed1.testbed1_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed1.testbed1_api.IStructInterfaceEventListener;
import testbed1.testbed1_api.IStructInterface;
import testbed1.testbed1_android_service.StructInterfaceServiceAdapter;
import testbed1.testbed1_android_service.StructInterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package testbed1.testbed1_impl; .
public class StructInterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "StructInterfaceStarter";



    public static IStructInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, StructInterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        StructInterfaceServiceFactory factory = StructInterfaceServiceFactory.get();
        Log.i(TAG, "starter: factory set for StructInterfaceServiceFactory");
        return StructInterfaceServiceAdapter.setService(factory);
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