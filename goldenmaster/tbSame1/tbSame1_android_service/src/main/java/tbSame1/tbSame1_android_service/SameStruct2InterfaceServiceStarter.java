package tbSame1.tbSame1_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSame1.tbSame1_api.ISameStruct2InterfaceEventListener;
import tbSame1.tbSame1_api.ISameStruct2Interface;
import tbSame1.tbSame1_android_service.SameStruct2InterfaceServiceAdapter;
import tbSame1.tbSame1_android_service.SameStruct2InterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package tbSame1.tbSame1_impl; .
public class SameStruct2InterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "SameStruct2InterfaceStarter";



    public static ISameStruct2Interface start(Context context) {
        stop(context);
        androidService = new Intent(context, SameStruct2InterfaceServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        SameStruct2InterfaceServiceFactory factory = SameStruct2InterfaceServiceFactory.get();
        Log.w(TAG, "starter: factory set for SameStruct2InterfaceServiceFactory");
        return SameStruct2InterfaceServiceAdapter.setService(factory);
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