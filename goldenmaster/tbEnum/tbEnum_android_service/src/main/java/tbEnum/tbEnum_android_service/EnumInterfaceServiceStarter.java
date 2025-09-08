package tbEnum.tbEnum_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbEnum.tbEnum_api.IEnumInterfaceEventListener;
import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_android_service.EnumInterfaceServiceAdapter;
import tbEnum.tbEnum_android_service.EnumInterfaceServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package tbEnum.tbEnum_impl; .
public class EnumInterfaceServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "EnumInterfaceStarter";



    public static IEnumInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, EnumInterfaceServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        EnumInterfaceServiceFactory factory = EnumInterfaceServiceFactory.get();
        Log.w(TAG, "starter: factory set for EnumInterfaceServiceFactory");
        return EnumInterfaceServiceAdapter.setService(factory);
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