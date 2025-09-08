package testbed2.testbed2_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed2.testbed2_api.IManyParamInterfaceEventListener;
import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_android_service.ManyParamInterfaceServiceAdapter;
import testbed2.testbed2_android_service.ManyParamInterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package testbed2.testbed2_impl; .
public class ManyParamInterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "ManyParamInterfaceStarter";



    public static IManyParamInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, ManyParamInterfaceServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        ManyParamInterfaceServiceFactory factory = ManyParamInterfaceServiceFactory.get();
        Log.w(TAG, "starter: factory set for ManyParamInterfaceServiceFactory");
        return ManyParamInterfaceServiceAdapter.setService(factory);
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