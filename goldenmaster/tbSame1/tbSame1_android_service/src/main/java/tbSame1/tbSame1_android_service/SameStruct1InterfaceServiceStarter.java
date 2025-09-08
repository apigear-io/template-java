package tbSame1.tbSame1_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSame1.tbSame1_api.ISameStruct1InterfaceEventListener;
import tbSame1.tbSame1_api.ISameStruct1Interface;
import tbSame1.tbSame1_android_service.SameStruct1InterfaceServiceAdapter;
import tbSame1.tbSame1_android_service.SameStruct1InterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package tbSame1.tbSame1_impl; .
public class SameStruct1InterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "SameStruct1InterfaceStarter";



    public static ISameStruct1Interface start(Context context) {
        stop(context);
        androidService = new Intent(context, SameStruct1InterfaceServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        SameStruct1InterfaceServiceFactory factory = SameStruct1InterfaceServiceFactory.get();
        Log.w(TAG, "starter: factory set for SameStruct1InterfaceServiceFactory");
        return SameStruct1InterfaceServiceAdapter.setService(factory);
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