package tbIfaceimport.tbIfaceimportjniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbIfaceimport.tbIfaceimport_api.IEmptyIfEventListener;
import tbIfaceimport.tbIfaceimport_api.IEmptyIf;
import tbIfaceimport.tbIfaceimport_android_service.EmptyIfServiceAdapter;
import tbIfaceimport.tbIfaceimportjniservice.EmptyIfJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class EmptyIfJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "EmptyIfJniStarter";



    public static IEmptyIf start(Context context) {
        stop(context);
        androidService = new Intent(context, EmptyIfServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        EmptyIfJniServiceFactory factory = EmptyIfJniServiceFactory.get();
        Log.i(TAG, "starter: factory set for EmptyIfJniServiceFactory");
        return EmptyIfServiceAdapter.setService(factory);
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