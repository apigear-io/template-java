package testbed1.testbed1jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed1.testbed1_api.IStructArray2InterfaceEventListener;
import testbed1.testbed1_api.IStructArray2Interface;
import testbed1.testbed1_android_service.StructArray2InterfaceServiceAdapter;
import testbed1.testbed1jniservice.StructArray2InterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class StructArray2InterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "StructArray2InterfaceJniStarter";



    public static IStructArray2Interface start(Context context) {
        stop(context);
        androidService = new Intent(context, StructArray2InterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        StructArray2InterfaceJniServiceFactory factory = StructArray2InterfaceJniServiceFactory.get();
        Log.i(TAG, "starter: factory set for StructArray2InterfaceJniServiceFactory");
        return StructArray2InterfaceServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        StructArray2InterfaceJniServiceFactory factory = StructArray2InterfaceJniServiceFactory.get();
        factory.clear();
        StructArray2InterfaceServiceAdapter.setService(null);
        if (androidService != null)
        {
            Log.i(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}
