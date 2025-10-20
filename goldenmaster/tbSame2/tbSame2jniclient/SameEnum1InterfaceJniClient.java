package tbSame2.tbSame2jniclient;

import tbSame2.tbSame2_api.ISameEnum1Interface;
import tbSame2.tbSame2_api.AbstractSameEnum1Interface;
import tbSame2.tbSame2_api.ISameEnum1InterfaceEventListener;

import tbSame2.tbSame2_android_client.SameEnum1InterfaceClient;
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_android_messenger.Enum1Parcelable;
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class SameEnum1InterfaceJniClient extends AbstractSameEnum1Interface implements ISameEnum1InterfaceEventListener
{

    private static final String TAG = "SameEnum1InterfaceJniClient";

    private SameEnum1InterfaceClient mMessengerClient = null;


    private static String ModuleName = "tbSame2.tbSame2jniservice.SameEnum1InterfaceJniService";
    private String lastServicePackage ="";

    @Override
    public boolean _isReady()
    {
        return mMessengerClient._isReady();
    }
    @Override
    public void setProp1(Enum1 prop1)
    {
        Log.i(TAG, "got request from ue, setProp1" + (prop1));
        mMessengerClient.setProp1(prop1);
    }
    @Override
    public Enum1 getProp1()
    {
        Log.i(TAG, "got request from ue, getProp1");
        return mMessengerClient.getProp1();
    }
    
     public Enum1 func1(Enum1 param1)
     {
        Log.v(TAG, "Blocking callfunc1 - should not be used ");
        return mMessengerClient.func1(param1);
    }

    public void func1Async(String callId, Enum1 param1){
        Log.v(TAG, "non blocking call func1 ");
        mMessengerClient.func1Async(param1).thenAccept(i -> {
            nativeOnFunc1Result(i, callId);});
    }

    //Should not be called directly, use func1Async(String callId, Enum1 param1)
    public CompletableFuture<Enum1> func1Async(Enum1 param1)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func1Async(param1);
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
            mMessengerClient = new SameEnum1InterfaceClient(ctx, connectionID);
            Log.w(TAG, "client created ");
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
        Log.w(TAG, "Connection state changed "+isReady);
        nativeIsReady(isReady);
    }

    //Event listener
    @Override
    public void onProp1Changed(Enum1 newValue)
    {
        Log.w(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp1Changed(newValue);
    }
    @Override
    public void onSig1(Enum1 param1)
    {
        Log.w(TAG, "NOTIFICATION from messenger client Signal sig1 "+ " " + param1);
        nativeOnSig1(param1);
    }
     private native void nativeOnProp1Changed(Enum1 prop1);
    private native void nativeOnSig1(Enum1 param1);
    private native void nativeOnFunc1Result(Enum1 result, String callId);
    private native void nativeIsReady(boolean isReady);
}
