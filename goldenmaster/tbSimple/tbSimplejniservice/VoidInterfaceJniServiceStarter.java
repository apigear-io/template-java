package tbSimple.tbSimplejniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.IVoidInterfaceEventListener;
import tbSimple.tbSimple_api.IVoidInterface;
import tbSimple.tbSimple_android_service.VoidInterfaceServiceAdapter;
import tbSimple.tbSimplejniservice.VoidInterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class VoidInterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "VoidInterfaceJniStarter";



    public static IVoidInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, VoidInterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        VoidInterfaceJniServiceFactory factory = VoidInterfaceJniServiceFactory.get();
        Log.i(TAG, "starter: factory set for VoidInterfaceJniServiceFactory");
        return VoidInterfaceServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        VoidInterfaceJniServiceFactory factory = VoidInterfaceJniServiceFactory.get();
        factory.clear();
        VoidInterfaceServiceAdapter.setService(null);
        if (androidService != null)
        {
            Log.i(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}
