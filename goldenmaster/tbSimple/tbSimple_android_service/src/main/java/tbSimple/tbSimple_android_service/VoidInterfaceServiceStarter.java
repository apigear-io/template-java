package tbSimple.tbSimple_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbSimple.tbSimple_api.IVoidInterfaceEventListener;
import tbSimple.tbSimple_api.IVoidInterface;
import tbSimple.tbSimple_android_service.VoidInterfaceServiceAdapter;
import tbSimple.tbSimple_android_service.VoidInterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package tbSimple.tbSimple_impl; .
public class VoidInterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "VoidInterfaceStarter";

    public static IVoidInterface start(Context context)
    {
        stop(context);
        androidService = new Intent(context, VoidInterfaceServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        VoidInterfaceServiceFactory factory = VoidInterfaceServiceFactory.get();
        Log.i(TAG, "starter: factory set for VoidInterfaceServiceFactory");
        return VoidInterfaceServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        VoidInterfaceServiceFactory factory = VoidInterfaceServiceFactory.get();
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
