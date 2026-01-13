package tbSame2.tbSame2_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSame2.tbSame2_api.ISameStruct2InterfaceEventListener;
import tbSame2.tbSame2_api.ISameStruct2Interface;
import tbSame2.tbSame2_android_service.SameStruct2InterfaceServiceAdapter;
import tbSame2.tbSame2_android_service.SameStruct2InterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package tbSame2.tbSame2_impl; .
public class SameStruct2InterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "SameStruct2InterfaceStarter";

    public static ISameStruct2Interface start(Context context)
    {
        stop(context);
        androidService = new Intent(context, SameStruct2InterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        SameStruct2InterfaceServiceFactory factory = SameStruct2InterfaceServiceFactory.get();
        Log.i(TAG, "starter: factory set for SameStruct2InterfaceServiceFactory");
        return SameStruct2InterfaceServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        SameStruct2InterfaceServiceFactory factory = SameStruct2InterfaceServiceFactory.get();
        factory.clear();
        SameStruct2InterfaceServiceAdapter.setService(null);
        if (androidService != null)
        {
            Log.i(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}
