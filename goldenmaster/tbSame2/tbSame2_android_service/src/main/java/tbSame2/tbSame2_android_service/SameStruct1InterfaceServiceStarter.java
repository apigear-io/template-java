package tbSame2.tbSame2_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSame2.tbSame2_api.ISameStruct1InterfaceEventListener;
import tbSame2.tbSame2_api.ISameStruct1Interface;
import tbSame2.tbSame2_android_service.SameStruct1InterfaceServiceAdapter;
import tbSame2.tbSame2_android_service.SameStruct1InterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package tbSame2.tbSame2_impl; .
public class SameStruct1InterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "SameStruct1InterfaceStarter";



    public static ISameStruct1Interface start(Context context) {
        stop(context);
        androidService = new Intent(context, SameStruct1InterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        SameStruct1InterfaceServiceFactory factory = SameStruct1InterfaceServiceFactory.get();
        Log.i(TAG, "starter: factory set for SameStruct1InterfaceServiceFactory");
        return SameStruct1InterfaceServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        if (androidService != null)
        {
            Log.i(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}