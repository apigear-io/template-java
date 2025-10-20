package testbed1.testbed1_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed1.testbed1_api.IStructArray2InterfaceEventListener;
import testbed1.testbed1_api.IStructArray2Interface;
import testbed1.testbed1_android_service.StructArray2InterfaceServiceAdapter;
import testbed1.testbed1_android_service.StructArray2InterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package testbed1.testbed1_impl; .
public class StructArray2InterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "StructArray2InterfaceStarter";



    public static IStructArray2Interface start(Context context) {
        stop(context);
        androidService = new Intent(context, StructArray2InterfaceServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        StructArray2InterfaceServiceFactory factory = StructArray2InterfaceServiceFactory.get();
        Log.w(TAG, "starter: factory set for StructArray2InterfaceServiceFactory");
        return StructArray2InterfaceServiceAdapter.setService(factory);
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