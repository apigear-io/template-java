package tbSame2.tbSame2jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSame2.tbSame2_api.ISameStruct1InterfaceEventListener;
import tbSame2.tbSame2_api.ISameStruct1Interface;
import tbSame2.tbSame2_android_service.SameStruct1InterfaceServiceAdapter;
import tbSame2.tbSame2jniservice.SameStruct1InterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class SameStruct1InterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "SameStruct1InterfaceJniStarter";



    public static ISameStruct1Interface start(Context context) {
        stop(context);
        androidService = new Intent(context, SameStruct1InterfaceServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        SameStruct1InterfaceJniServiceFactory factory = SameStruct1InterfaceJniServiceFactory.get();
        Log.w(TAG, "starter: factory set for SameStruct1InterfaceJniServiceFactory");
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