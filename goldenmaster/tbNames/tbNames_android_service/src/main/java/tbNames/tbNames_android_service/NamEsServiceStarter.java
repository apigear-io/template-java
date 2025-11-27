package tbNames.tbNames_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbNames.tbNames_api.INamEsEventListener;
import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_android_service.NamEsServiceAdapter;
import tbNames.tbNames_android_service.NamEsServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package tbNames.tbNames_impl; .
public class NamEsServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "NamEsStarter";



    public static INamEs start(Context context) {
        stop(context);
        androidService = new Intent(context, NamEsServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        NamEsServiceFactory factory = NamEsServiceFactory.get();
        Log.i(TAG, "starter: factory set for NamEsServiceFactory");
        return NamEsServiceAdapter.setService(factory);
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