package testbed1.testbed1jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed1.testbed1_api.IStructInterfaceEventListener;
import testbed1.testbed1_api.IStructInterface;
import testbed1.testbed1_android_service.StructInterfaceServiceAdapter;
import testbed1.testbed1jniservice.StructInterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class StructInterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "StructInterfaceJniStarter";



    public static IStructInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, StructInterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        StructInterfaceJniServiceFactory factory = StructInterfaceJniServiceFactory.get();
        Log.i(TAG, "starter: factory set for StructInterfaceJniServiceFactory");
        return StructInterfaceServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        StructInterfaceJniServiceFactory factory = StructInterfaceJniServiceFactory.get();
        factory.clear();
        StructInterfaceServiceAdapter.setService(null);
        if (androidService != null)
        {
            Log.i(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}
