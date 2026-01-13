package tbRefIfaces.tbRefIfaces_android_service;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbRefIfaces.tbRefIfaces_api.IParentIfEventListener;
import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_android_service.ParentIfServiceAdapter;
import tbRefIfaces.tbRefIfaces_android_service.ParentIfServiceFactory;


//Use this class to manage lifetime of android server with Implemented backend service from package tbRefIfaces.tbRefIfaces_impl; .
public class ParentIfServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "ParentIfStarter";

    public static IParentIf start(Context context)
    {
        stop(context);
        androidService = new Intent(context, ParentIfServiceAdapter.class);
        Log.i(TAG, "starter: created intent");
        context.startService(androidService);
        Log.i(TAG, "starter: started intent (service) ");
        ParentIfServiceFactory factory = ParentIfServiceFactory.get();
        Log.i(TAG, "starter: factory set for ParentIfServiceFactory");
        return ParentIfServiceAdapter.setService(factory);
    }

    public static void stop(Context context)
    {
        ParentIfServiceFactory factory = ParentIfServiceFactory.get();
        factory.clear();
        ParentIfServiceAdapter.setService(null);
        if (androidService != null)
        {
            Log.i(TAG, "starter: stop the service");
            context.stopService(androidService);
        }
        androidService = null;
    }

}
