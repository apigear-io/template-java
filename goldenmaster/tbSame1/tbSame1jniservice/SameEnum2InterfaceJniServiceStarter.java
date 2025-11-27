package tbSame1.tbSame1jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSame1.tbSame1_api.ISameEnum2InterfaceEventListener;
import tbSame1.tbSame1_api.ISameEnum2Interface;
import tbSame1.tbSame1_android_service.SameEnum2InterfaceServiceAdapter;
import tbSame1.tbSame1jniservice.SameEnum2InterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class SameEnum2InterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "SameEnum2InterfaceJniStarter";



    public static ISameEnum2Interface start(Context context) {
        stop(context);
        androidService = new Intent(context, SameEnum2InterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        SameEnum2InterfaceJniServiceFactory factory = SameEnum2InterfaceJniServiceFactory.get();
        Log.i(TAG, "starter: factory set for SameEnum2InterfaceJniServiceFactory");
        return SameEnum2InterfaceServiceAdapter.setService(factory);
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