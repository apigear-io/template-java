package tbRefIfaces.tbRefIfacesjniclient;

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_api.AbstractSimpleLocalIf;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIfEventListener;
import tbRefIfaces.tbRefIfaces_api.RemoteOperationException;

import tbRefIfaces.tbRefIfaces_android_client.SimpleLocalIfClient;
import apigear.android.lifecycle.BindingLifecycleRegistry;
import apigear.android.lifecycle.IBindingLifecycleCoordinator;
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class SimpleLocalIfJniClient extends AbstractSimpleLocalIf implements ISimpleLocalIfEventListener
{

    private static final String TAG = "SimpleLocalIfJniClient";

    private SimpleLocalIfClient mMessengerClient = null;


    private static String ModuleName = "tbRefIfaces.tbRefIfacesjniservice.SimpleLocalIfJniService";
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
    @Override
    public void setIntProperty(int intProperty)
    {
        Log.i(TAG, "got request from ue, setIntProperty" + (intProperty));
        mMessengerClient.setIntProperty(intProperty);
    }
    @Override
    public int getIntProperty()
    {
        Log.i(TAG, "got request from ue, getIntProperty");
        return mMessengerClient.getIntProperty();
    }
    
     @Override
     public int intMethod(int param)
     {
        Log.v(TAG, "Blocking callintMethod - should not be used ");
        return mMessengerClient.intMethod(param);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnIntMethodResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void intMethodAsync(String callId, int param){
        Log.v(TAG, "non blocking call intMethod ");
        mMessengerClient.intMethodAsync(param).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "intMethod async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnIntMethodResult(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<Integer> intMethodAsync(int param)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.intMethodAsync(param);
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
            mMessengerClient = new SimpleLocalIfClient(ctx, connectionID);
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
    @Override
    public void onIntPropertyChanged(int newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnIntPropertyChanged(newValue);
    }
    @Override
    public void onIntSignal(int param)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal intSignal "+ " " + param);
        nativeOnIntSignal(param);
    }
     private native void nativeOnIntPropertyChanged(int intProperty);
    private native void nativeOnIntSignal(int param);
    private native void nativeOnIntMethodResult(int result, String callId);
    private native void nativeAsyncOperationFailed(String callId, String errorMessage, int errorCode);
    private native void nativeIsReady(boolean isReady);
}
