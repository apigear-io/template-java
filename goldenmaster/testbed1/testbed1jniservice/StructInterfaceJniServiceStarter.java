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
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        StructInterfaceJniServiceFactory factory = StructInterfaceJniServiceFactory.get();
        Log.w(TAG, "starter: factory set for StructInterfaceJniServiceFactory");
        return StructInterfaceServiceAdapter.setService(factory);
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