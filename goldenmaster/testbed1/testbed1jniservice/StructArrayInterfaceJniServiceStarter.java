package testbed1.testbed1jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed1.testbed1_api.IStructArrayInterfaceEventListener;
import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_android_service.StructArrayInterfaceServiceAdapter;
import testbed1.testbed1jniservice.StructArrayInterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class StructArrayInterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "StructArrayInterfaceJniStarter";



    public static IStructArrayInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, StructArrayInterfaceServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        StructArrayInterfaceJniServiceFactory factory = StructArrayInterfaceJniServiceFactory.get();
        Log.w(TAG, "starter: factory set for StructArrayInterfaceJniServiceFactory");
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