package testbed2.testbed2_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed2.testbed2_api.INestedStruct2InterfaceEventListener;
import testbed2.testbed2_api.INestedStruct2Interface;
import testbed2.testbed2_android_service.NestedStruct2InterfaceServiceAdapter;
import testbed2.testbed2_android_service.NestedStruct2InterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package testbed2.testbed2_impl; .
public class NestedStruct2InterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "NestedStruct2InterfaceStarter";

    public static INestedStruct2Interface start(Context context)
    {
        stop(context);
        androidService = new Intent(context, NestedStruct2InterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        NestedStruct2InterfaceServiceFactory factory = NestedStruct2InterfaceServiceFactory.get();
        Log.i(TAG, "starter: factory set for NestedStruct2InterfaceServiceFactory");
        return NestedStruct2InterfaceServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        NestedStruct2InterfaceServiceFactory factory = NestedStruct2InterfaceServiceFactory.get();
        factory.clear();
        NestedStruct2InterfaceServiceAdapter.setService(null);
        if (androidService != null)
        {
            Log.i(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}
