package testbed2.testbed2_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed2.testbed2_api.INestedStruct3InterfaceEventListener;
import testbed2.testbed2_api.INestedStruct3Interface;
import testbed2.testbed2_android_service.NestedStruct3InterfaceServiceAdapter;
import testbed2.testbed2_android_service.NestedStruct3InterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package testbed2.testbed2_impl; .
public class NestedStruct3InterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "NestedStruct3InterfaceStarter";



    public static INestedStruct3Interface start(Context context) {
        stop(context);
        androidService = new Intent(context, NestedStruct3InterfaceServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        NestedStruct3InterfaceServiceFactory factory = NestedStruct3InterfaceServiceFactory.get();
        Log.w(TAG, "starter: factory set for NestedStruct3InterfaceServiceFactory");
        return NestedStruct3InterfaceServiceAdapter.setService(factory);
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