package tbSame2.tbSame2jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSame2.tbSame2_api.ISameEnum2InterfaceEventListener;
import tbSame2.tbSame2_api.ISameEnum2Interface;
import tbSame2.tbSame2_android_service.SameEnum2InterfaceServiceAdapter;
import tbSame2.tbSame2jniservice.SameEnum2InterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class SameEnum2InterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "SameEnum2InterfaceJniStarter";



    public static ISameEnum2Interface start(Context context) {
        stop(context);
        androidService = new Intent(context, SameEnum2InterfaceServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        SameEnum2InterfaceJniServiceFactory factory = SameEnum2InterfaceJniServiceFactory.get();
        Log.w(TAG, "starter: factory set for SameEnum2InterfaceJniServiceFactory");
        return SameEnum2InterfaceServiceAdapter.setService(factory);
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