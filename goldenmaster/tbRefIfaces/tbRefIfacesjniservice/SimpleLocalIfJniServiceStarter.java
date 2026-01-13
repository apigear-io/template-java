package tbRefIfaces.tbRefIfacesjniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIfEventListener;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_android_service.SimpleLocalIfServiceAdapter;
import tbRefIfaces.tbRefIfacesjniservice.SimpleLocalIfJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class SimpleLocalIfJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "SimpleLocalIfJniStarter";



    public static ISimpleLocalIf start(Context context) {
        stop(context);
        androidService = new Intent(context, SimpleLocalIfServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        SimpleLocalIfJniServiceFactory factory = SimpleLocalIfJniServiceFactory.get();
        Log.i(TAG, "starter: factory set for SimpleLocalIfJniServiceFactory");
        return SimpleLocalIfServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        SimpleLocalIfJniServiceFactory factory = SimpleLocalIfJniServiceFactory.get();
        factory.clear();
        SimpleLocalIfServiceAdapter.setService(null);
        if (androidService != null)
        {
            Log.i(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}
