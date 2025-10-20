package testbed2.testbed2jniclient;

import testbed2.testbed2_api.INestedStruct3Interface;
import testbed2.testbed2_api.AbstractNestedStruct3Interface;
import testbed2.testbed2_api.INestedStruct3InterfaceEventListener;

import testbed2.testbed2_android_client.NestedStruct3InterfaceClient;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_android_messenger.NestedStruct1Parcelable;
import testbed2.testbed2_api.NestedStruct2;
import testbed2.testbed2_android_messenger.NestedStruct2Parcelable;
import testbed2.testbed2_api.NestedStruct3;
import testbed2.testbed2_android_messenger.NestedStruct3Parcelable;
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class NestedStruct3InterfaceJniClient extends AbstractNestedStruct3Interface implements INestedStruct3InterfaceEventListener
{

    private static final String TAG = "NestedStruct3InterfaceJniClient";

    private NestedStruct3InterfaceClient mMessengerClient = null;


    private static String ModuleName = "testbed2.testbed2jniservice.NestedStruct3InterfaceJniService";
    private String lastServicePackage ="";

    @Override
    public boolean _isReady()
    {
        return mMessengerClient._isReady();
    }
    @Override
    public void setProp1(NestedStruct1 prop1)
    {
        Log.i(TAG, "got request from ue, setProp1" + (prop1));
        mMessengerClient.setProp1(prop1);
    }
    @Override
    public NestedStruct1 getProp1()
    {
        Log.i(TAG, "got request from ue, getProp1");
        return mMessengerClient.getProp1();
    }
    
    @Override
    public void setProp2(NestedStruct2 prop2)
    {
        Log.i(TAG, "got request from ue, setProp2" + (prop2));
        mMessengerClient.setProp2(prop2);
    }
    @Override
    public NestedStruct2 getProp2()
    {
        Log.i(TAG, "got request from ue, getProp2");
        return mMessengerClient.getProp2();
    }
    
    @Override
    public void setProp3(NestedStruct3 prop3)
    {
        Log.i(TAG, "got request from ue, setProp3" + (prop3));
        mMessengerClient.setProp3(prop3);
    }
    @Override
    public NestedStruct3 getProp3()
    {
        Log.i(TAG, "got request from ue, getProp3");
        return mMessengerClient.getProp3();
    }
    
     public NestedStruct1 func1(NestedStruct1 param1)
     {
        Log.v(TAG, "Blocking callfunc1 - should not be used ");
        return mMessengerClient.func1(param1);
    }

    public void func1Async(String callId, NestedStruct1 param1){
        Log.v(TAG, "non blocking call func1 ");
        mMessengerClient.func1Async(param1).thenAccept(i -> {
            nativeOnFunc1Result(i, callId);});
    }

    //Should not be called directly, use func1Async(String callId, NestedStruct1 param1)
    public CompletableFuture<NestedStruct1> func1Async(NestedStruct1 param1)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func1Async(param1);
    }
     public NestedStruct1 func2(NestedStruct1 param1, NestedStruct2 param2)
     {
        Log.v(TAG, "Blocking callfunc2 - should not be used ");
        return mMessengerClient.func2(param1, param2);
    }

    public void func2Async(String callId, NestedStruct1 param1, NestedStruct2 param2){
        Log.v(TAG, "non blocking call func2 ");
        mMessengerClient.func2Async(param1, param2).thenAccept(i -> {
            nativeOnFunc2Result(i, callId);});
    }

    //Should not be called directly, use func2Async(String callId, NestedStruct1 param1, NestedStruct2 param2)
    public CompletableFuture<NestedStruct1> func2Async(NestedStruct1 param1, NestedStruct2 param2)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func2Async(param1, param2);
    }
     public NestedStruct1 func3(NestedStruct1 param1, NestedStruct2 param2, NestedStruct3 param3)
     {
        Log.v(TAG, "Blocking callfunc3 - should not be used ");
        return mMessengerClient.func3(param1, param2, param3);
    }

    public void func3Async(String callId, NestedStruct1 param1, NestedStruct2 param2, NestedStruct3 param3){
        Log.v(TAG, "non blocking call func3 ");
        mMessengerClient.func3Async(param1, param2, param3).thenAccept(i -> {
            nativeOnFunc3Result(i, callId);});
    }

    //Should not be called directly, use func3Async(String callId, NestedStruct1 param1, NestedStruct2 param2, NestedStruct3 param3)
    public CompletableFuture<NestedStruct1> func3Async(NestedStruct1 param1, NestedStruct2 param2, NestedStruct3 param3)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func3Async(param1, param2, param3);
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
            mMessengerClient = new NestedStruct3InterfaceClient(ctx, connectionID);
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
    public void onProp1Changed(NestedStruct1 newValue)
    {
        Log.w(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp1Changed(newValue);
    }
    @Override
    public void onProp2Changed(NestedStruct2 newValue)
    {
        Log.w(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp2Changed(newValue);
    }
    @Override
    public void onProp3Changed(NestedStruct3 newValue)
    {
        Log.w(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp3Changed(newValue);
    }
    @Override
    public void onSig1(NestedStruct1 param1)
    {
        Log.w(TAG, "NOTIFICATION from messenger client Signal sig1 "+ " " + param1);
        nativeOnSig1(param1);
    }
    @Override
    public void onSig2(NestedStruct1 param1, NestedStruct2 param2)
    {
        Log.w(TAG, "NOTIFICATION from messenger client Signal sig2 "+ " " + param1+ " " + param2);
        nativeOnSig2(param1, param2);
    }
    @Override
    public void onSig3(NestedStruct1 param1, NestedStruct2 param2, NestedStruct3 param3)
    {
        Log.w(TAG, "NOTIFICATION from messenger client Signal sig3 "+ " " + param1+ " " + param2+ " " + param3);
        nativeOnSig3(param1, param2, param3);
    }
     private native void nativeOnProp1Changed(NestedStruct1 prop1);
     private native void nativeOnProp2Changed(NestedStruct2 prop2);
     private native void nativeOnProp3Changed(NestedStruct3 prop3);
    private native void nativeOnSig1(NestedStruct1 param1);
    private native void nativeOnSig2(NestedStruct1 param1, NestedStruct2 param2);
    private native void nativeOnSig3(NestedStruct1 param1, NestedStruct2 param2, NestedStruct3 param3);
    private native void nativeOnFunc1Result(NestedStruct1 result, String callId);
    private native void nativeOnFunc2Result(NestedStruct1 result, String callId);
    private native void nativeOnFunc3Result(NestedStruct1 result, String callId);
    private native void nativeIsReady(boolean isReady);
}
