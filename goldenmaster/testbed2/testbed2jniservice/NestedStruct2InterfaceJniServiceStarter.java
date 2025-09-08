package testbed2.testbed2jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed2.testbed2_api.INestedStruct2InterfaceEventListener;
import testbed2.testbed2_api.INestedStruct2Interface;
import testbed2.testbed2_android_service.NestedStruct2InterfaceServiceAdapter;
import testbed2.testbed2jniservice.NestedStruct2InterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class NestedStruct2InterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "NestedStruct2InterfaceJniStarter";



    public static INestedStruct2Interface start(Context context) {
        stop(context);
        androidService = new Intent(context, NestedStruct2InterfaceServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        NestedStruct2InterfaceJniServiceFactory factory = NestedStruct2InterfaceJniServiceFactory.get();
        Log.w(TAG, "starter: factory set for NestedStruct2InterfaceJniServiceFactory");
        return NestedStruct2InterfaceServiceAdapter.setService(factory);
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