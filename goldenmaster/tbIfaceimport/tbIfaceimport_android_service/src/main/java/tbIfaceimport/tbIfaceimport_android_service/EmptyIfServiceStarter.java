package tbIfaceimport.tbIfaceimport_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbIfaceimport.tbIfaceimport_api.IEmptyIfEventListener;
import tbIfaceimport.tbIfaceimport_api.IEmptyIf;
import tbIfaceimport.tbIfaceimport_android_service.EmptyIfServiceAdapter;
import tbIfaceimport.tbIfaceimport_android_service.EmptyIfServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package tbIfaceimport.tbIfaceimport_impl; .
public class EmptyIfServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "EmptyIfStarter";



    public static IEmptyIf start(Context context) {
        stop(context);
        androidService = new Intent(context, EmptyIfServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        EmptyIfServiceFactory factory = EmptyIfServiceFactory.get();
        Log.w(TAG, "starter: factory set for EmptyIfServiceFactory");
        return EmptyIfServiceAdapter.setService(factory);
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