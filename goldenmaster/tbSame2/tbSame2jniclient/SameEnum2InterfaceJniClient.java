package tbSame2.tbSame2jniclient;

import tbSame2.tbSame2_api.ISameEnum2Interface;
import tbSame2.tbSame2_api.AbstractSameEnum2Interface;
import tbSame2.tbSame2_api.ISameEnum2InterfaceEventListener;

import tbSame2.tbSame2_android_client.SameEnum2InterfaceClient;
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_android_messenger.Enum1Parcelable;
import tbSame2.tbSame2_api.Enum2;
import tbSame2.tbSame2_android_messenger.Enum2Parcelable;
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class SameEnum2InterfaceJniClient extends AbstractSameEnum2Interface implements ISameEnum2InterfaceEventListener
{

    private static final String TAG = "SameEnum2InterfaceJniClient";

    private SameEnum2InterfaceClient mMessengerClient = null;


    private static String ModuleName = "tbSame2.tbSame2jniservice.SameEnum2InterfaceJniService";
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
    
    @Override
    public void setProp2(Enum2 prop2)
    {
        Log.i(TAG, "got request from ue, setProp2" + (prop2));
        mMessengerClient.setProp2(prop2);
    }
    @Override
    public Enum2 getProp2()
    {
        Log.i(TAG, "got request from ue, getProp2");
        return mMessengerClient.getProp2();
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
     public Enum1 func2(Enum1 param1, Enum2 param2)
     {
        Log.v(TAG, "Blocking callfunc2 - should not be used ");
        return mMessengerClient.func2(param1, param2);
    }

    public void func2Async(String callId, Enum1 param1, Enum2 param2){
        Log.v(TAG, "non blocking call func2 ");
        mMessengerClient.func2Async(param1, param2).thenAccept(i -> {
            nativeOnFunc2Result(i, callId);});
    }

    //Should not be called directly, use func2Async(String callId, Enum1 param1, Enum2 param2)
    public CompletableFuture<Enum1> func2Async(Enum1 param1, Enum2 param2)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func2Async(param1, param2);
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
            mMessengerClient = new SameEnum2InterfaceClient(ctx, connectionID);
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
    public void onProp1Changed(Enum1 newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp1Changed(newValue);
    }
    @Override
    public void onProp2Changed(Enum2 newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp2Changed(newValue);
    }
    @Override
    public void onSig1(Enum1 param1)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sig1 "+ " " + param1);
        nativeOnSig1(param1);
    }
    @Override
    public void onSig2(Enum1 param1, Enum2 param2)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sig2 "+ " " + param1+ " " + param2);
        nativeOnSig2(param1, param2);
    }
     private native void nativeOnProp1Changed(Enum1 prop1);
     private native void nativeOnProp2Changed(Enum2 prop2);
    private native void nativeOnSig1(Enum1 param1);
    private native void nativeOnSig2(Enum1 param1, Enum2 param2);
    private native void nativeOnFunc1Result(Enum1 result, String callId);
    private native void nativeOnFunc2Result(Enum1 result, String callId);
    private native void nativeIsReady(boolean isReady);
}
