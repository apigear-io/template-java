package tbSimple.tbSimplejniclient;

import tbSimple.tbSimple_api.INoPropertiesInterface;
import tbSimple.tbSimple_api.AbstractNoPropertiesInterface;
import tbSimple.tbSimple_api.INoPropertiesInterfaceEventListener;

import tbSimple.tbSimple_android_client.NoPropertiesInterfaceClient;
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class NoPropertiesInterfaceJniClient extends AbstractNoPropertiesInterface implements INoPropertiesInterfaceEventListener
{

    private static final String TAG = "NoPropertiesInterfaceJniClient";

    private NoPropertiesInterfaceClient mMessengerClient = null;


    private static String ModuleName = "tbSimple.tbSimplejniservice.NoPropertiesInterfaceJniService";
    private String lastServicePackage ="";

    @Override
    public boolean _isReady()
    {
        return mMessengerClient._isReady();
    }
     public void funcVoid()
     {
        Log.v(TAG, "Blocking callfuncVoid - should not be used ");
         mMessengerClient.funcVoid();
    }

    public void funcVoidAsync(String callId){
        Log.v(TAG, "non blocking call funcVoid ");
        mMessengerClient.funcVoidAsync().thenAccept(i -> {
            nativeOnFuncVoidResult(callId);});
    }

    //Should not be called directly, use funcVoidAsync(String callId, )
    public CompletableFuture<Void> funcVoidAsync()
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcVoidAsync();
    }
     public boolean funcBool(boolean paramBool)
     {
        Log.v(TAG, "Blocking callfuncBool - should not be used ");
        return mMessengerClient.funcBool(paramBool);
    }

    public void funcBoolAsync(String callId, boolean paramBool){
        Log.v(TAG, "non blocking call funcBool ");
        mMessengerClient.funcBoolAsync(paramBool).thenAccept(i -> {
            nativeOnFuncBoolResult(i, callId);});
    }

    //Should not be called directly, use funcBoolAsync(String callId, boolean paramBool)
    public CompletableFuture<Boolean> funcBoolAsync(boolean paramBool)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcBoolAsync(paramBool);
    }

    public boolean bind(Context ctx, String packageName, String connectionID){
        Log.v(TAG, "natice client: bind " + packageName);
        return initServiceConnection(ctx, packageName, connectionID);
    }

    public void unbind(){
        Log.v(TAG, "native client: unbind " + lastServicePackage);
        if (mMessengerClient != null)
        {
            mMessengerClient.unbindFromService();
        }
    }

    private boolean initServiceConnection(Context ctx, String servicePackage, String connectionID)
    {
        if (mMessengerClient == null)
        {
            mMessengerClient = new NoPropertiesInterfaceClient(ctx, connectionID);
            Log.i(TAG, "client created ");
            mMessengerClient.addEventListener(this);
        }
        if (lastServicePackage != servicePackage &&  mMessengerClient.isBoundToService()) {
            unbind();
        }
        lastServicePackage = servicePackage;
        boolean res = mMessengerClient.bindToService(lastServicePackage);
        Log.v(TAG, "Bind " + res+": to "+lastServicePackage);
        return res;
    }

    @Override
    public void on_readyStatusChanged(boolean isReady) {
        Log.i(TAG, "Connection state changed "+isReady);
        nativeIsReady(isReady);
    }

    //Event listener
    @Override
    public void onSigVoid()
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigVoid ");
        nativeOnSigVoid();
    }
    @Override
    public void onSigBool(boolean paramBool)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigBool "+ " " + paramBool);
        nativeOnSigBool(paramBool);
    }
    private native void nativeOnSigVoid();
    private native void nativeOnSigBool(boolean paramBool);
    private native void nativeOnFuncVoidResult(String callId);
    private native void nativeOnFuncBoolResult(boolean result, String callId);
    private native void nativeIsReady(boolean isReady);
}
