package tbSame1.tbSame1_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSame1.tbSame1_api.ISameEnum2InterfaceEventListener;
import tbSame1.tbSame1_api.ISameEnum2Interface;
import tbSame1.tbSame1_android_service.SameEnum2InterfaceServiceAdapter;
import tbSame1.tbSame1_android_service.SameEnum2InterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package tbSame1.tbSame1_impl; .
public class SameEnum2InterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "SameEnum2InterfaceStarter";



    public static ISameEnum2Interface start(Context context) {
        stop(context);
        androidService = new Intent(context, SameEnum2InterfaceServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        SameEnum2InterfaceServiceFactory factory = SameEnum2InterfaceServiceFactory.get();
        Log.w(TAG, "starter: factory set for SameEnum2InterfaceServiceFactory");
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