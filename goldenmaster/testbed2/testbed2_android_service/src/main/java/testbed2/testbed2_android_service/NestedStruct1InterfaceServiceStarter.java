package testbed2.testbed2_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed2.testbed2_api.INestedStruct1InterfaceEventListener;
import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_android_service.NestedStruct1InterfaceServiceAdapter;
import testbed2.testbed2_android_service.NestedStruct1InterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package testbed2.testbed2_impl; .
public class NestedStruct1InterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "NestedStruct1InterfaceStarter";

    public static INestedStruct1Interface start(Context context)
    {
        stop(context);
        androidService = new Intent(context, NestedStruct1InterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        NestedStruct1InterfaceServiceFactory factory = NestedStruct1InterfaceServiceFactory.get();
        Log.i(TAG, "starter: factory set for NestedStruct1InterfaceServiceFactory");
        return NestedStruct1InterfaceServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        NestedStruct1InterfaceServiceFactory factory = NestedStruct1InterfaceServiceFactory.get();
        factory.clear();
        NestedStruct1InterfaceServiceAdapter.setService(null);
        if (androidService != null)
        {
            Log.i(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}
