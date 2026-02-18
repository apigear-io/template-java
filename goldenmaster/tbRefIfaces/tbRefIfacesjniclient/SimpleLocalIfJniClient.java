package tbRefIfaces.tbRefIfacesjniclient;

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_api.AbstractSimpleLocalIf;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIfEventListener;

import tbRefIfaces.tbRefIfaces_android_client.SimpleLocalIfClient;
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
    * It returns result via nativeOnIntMethodResult with the same callId.
    *
    * @param callId async call identifier
    */
    public void intMethodAsync(String callId, int param){
        Log.v(TAG, "non blocking call intMethod ");
        mMessengerClient.intMethodAsync(param).thenAccept(i -> {
            nativeOnIntMethodResult(i, callId);});
    }

    @Override
    public CompletableFuture<Integer> intMethodAsync(int param)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.intMethodAsync(param);
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
    private native void nativeIsReady(boolean isReady);
}
