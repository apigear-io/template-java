package tbSimple.tbSimplejniclient;

import tbSimple.tbSimple_api.IEmptyInterface;
import tbSimple.tbSimple_api.AbstractEmptyInterface;
import tbSimple.tbSimple_api.IEmptyInterfaceEventListener;

import tbSimple.tbSimple_android_client.EmptyInterfaceClient;
import apigear.android.lifecycle.BindingLifecycleRegistry;
import apigear.android.lifecycle.IBindingLifecycleCoordinator;
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class EmptyInterfaceJniClient extends AbstractEmptyInterface implements IEmptyInterfaceEventListener
{

    private static final String TAG = "EmptyInterfaceJniClient";

    private EmptyInterfaceClient mMessengerClient = null;


    private static String ModuleName = "tbSimple.tbSimplejniservice.EmptyInterfaceJniService";
    private String lastServicePackage ="";

    // Stable Runnable reference so coordinator register/unregister see the same
    // instance. Initialized once per JniClient; method-reference resolution at
    // field-init time gives a single Runnable bound to this::unbind.
    private final Runnable mCleanup = this::unbind;

    @Override
    public boolean _isReady()
    {
        return mMessengerClient != null ? mMessengerClient._isReady() : false;
    }

    public boolean bind(Context ctx, String packageName, String connectionID){
        Log.v(TAG, "natice client: bind " + packageName);
        boolean res = initServiceConnection(ctx, packageName, connectionID);
        if (res)
        {
            IBindingLifecycleCoordinator coordinator = BindingLifecycleRegistry.get();
            if (coordinator != null)
            {
                coordinator.registerCleanup(mCleanup);
            }
        }
        return res;
    }

    public void unbind(){
        Log.v(TAG, "native client: unbind " + lastServicePackage);
        IBindingLifecycleCoordinator coordinator = BindingLifecycleRegistry.get();
        if (coordinator != null)
        {
            coordinator.unregisterCleanup(mCleanup);
        }
        if (mMessengerClient != null)
        {
            mMessengerClient.unbindFromService();
        }
    }

    private boolean initServiceConnection(Context ctx, String servicePackage, String connectionID)
    {
        if (mMessengerClient == null)
        {
            mMessengerClient = new EmptyInterfaceClient(ctx, connectionID);
            Log.i(TAG, "client created ");
            mMessengerClient.addEventListener(this);
        }
        if (!lastServicePackage.equals(servicePackage) &&  mMessengerClient.isBoundToService()) {
            unbind();
        }
        lastServicePackage = servicePackage;
        boolean res = mMessengerClient.bindToService(lastServicePackage);
        Log.v(TAG, "Bind " + res+": to "+lastServicePackage);
        return res;
    }

    @Override
    public void on_readyStatusChanged(boolean isReady) {
        Log.i(TAG, "Will call native Connection state changed "+isReady);
        nativeIsReady(isReady);
    }

    //Event listener
    private native void nativeIsReady(boolean isReady);
}
