package tbRefIfaces.tbRefIfacesjniservice;

import android.util.Log;
import android.content.Context;
import android.content.Intent;

import tbRefIfaces.tbRefIfaces_api.IParentIfEventListener;
import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_android_service.ParentIfServiceAdapter;
import tbRefIfaces.tbRefIfacesjniservice.ParentIfJniServiceFactory;


//Use this class to manage lifetime of android server with native backend service.
public class ParentIfJniServiceStarter {

    static Intent androidService = null;
    private static final String TAG = "ParentIfJniStarter";



    public static IParentIf start(Context context) {
        stop(context);
        androidService = new Intent(context, ParentIfServiceAdapter.class);
        Log.w(TAG, "starter: created intent");
        context.startService(androidService);
        Log.w(TAG, "starter: started intent (service) ");
        ParentIfJniServiceFactory factory = ParentIfJniServiceFactory.get();
        Log.w(TAG, "starter: factory set for ParentIfJniServiceFactory");
        return ParentIfServiceAdapter.setService(factory);
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