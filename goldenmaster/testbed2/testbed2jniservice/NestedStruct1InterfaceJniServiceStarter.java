package testbed2.testbed2jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed2.testbed2_api.INestedStruct1InterfaceEventListener;
import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_android_service.NestedStruct1InterfaceServiceAdapter;
import testbed2.testbed2jniservice.NestedStruct1InterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class NestedStruct1InterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "NestedStruct1InterfaceJniStarter";



    public static INestedStruct1Interface start(Context context) {
        stop(context);
        androidService = new Intent(context, NestedStruct1InterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        NestedStruct1InterfaceJniServiceFactory factory = NestedStruct1InterfaceJniServiceFactory.get();
        Log.i(TAG, "starter: factory set for NestedStruct1InterfaceJniServiceFactory");
        return NestedStruct1InterfaceServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        NestedStruct1InterfaceJniServiceFactory factory = NestedStruct1InterfaceJniServiceFactory.get();
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
