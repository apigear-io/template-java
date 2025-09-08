package tbEnum.tbEnumjniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbEnum.tbEnum_api.IEnumInterfaceEventListener;
import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_android_service.EnumInterfaceServiceAdapter;
import tbEnum.tbEnumjniservice.EnumInterfaceJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class EnumInterfaceJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "EnumInterfaceJniStarter";



    public static IEnumInterface start(Context context) {
        stop(context);
        androidService = new Intent(context, EnumInterfaceServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        EnumInterfaceJniServiceFactory factory = EnumInterfaceJniServiceFactory.get();
        Log.w(TAG, "starter: factory set for EnumInterfaceJniServiceFactory");
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