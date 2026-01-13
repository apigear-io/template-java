package testbed2.testbed2jniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import testbed2.testbed2_api.IManyParamInterfaceEventListener;
import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_android_service.ManyParamInterfaceServiceAdapter;
import testbed2.testbed2jniservice.ManyParamInterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class ManyParamInterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "ManyParamInterfaceJniStarter";



    public static IManyParamInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, ManyParamInterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        ManyParamInterfaceJniServiceFactory factory = ManyParamInterfaceJniServiceFactory.get();
        Log.i(TAG, "starter: factory set for ManyParamInterfaceJniServiceFactory");
        return ManyParamInterfaceServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        ManyParamInterfaceJniServiceFactory factory = ManyParamInterfaceJniServiceFactory.get();
        factory.clear();
        ManyParamInterfaceServiceAdapter.setService(null);
        if (androidService != null)
        {
            Log.i(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}
