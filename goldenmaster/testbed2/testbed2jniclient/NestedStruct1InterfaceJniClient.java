package testbed2.testbed2jniclient;

import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_api.AbstractNestedStruct1Interface;
import testbed2.testbed2_api.INestedStruct1InterfaceEventListener;

import testbed2.testbed2_android_client.NestedStruct1InterfaceClient;
import testbed2.testbed2_api.Struct1;
import testbed2.testbed2_api.Struct2;
import testbed2.testbed2_api.Struct3;
import testbed2.testbed2_api.Struct4;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_api.NestedStruct2;
import testbed2.testbed2_api.NestedStruct3;
import testbed2.testbed2_api.Enum1;
import testbed2.testbed2_api.Enum2;
import testbed2.testbed2_api.Enum3;
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class NestedStruct1InterfaceJniClient extends AbstractNestedStruct1Interface implements INestedStruct1InterfaceEventListener
{

    private static final String TAG = "NestedStruct1InterfaceJniClient";

    private NestedStruct1InterfaceClient mMessengerClient = null;


    private static String ModuleName = "testbed2.testbed2jniservice.NestedStruct1InterfaceJniService";
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

    public boolean bind(Context ctx, String packageName, String connectionID){
        Log.v(TAG, "natice client: bind " + packageName);
        return initServiceConnection(ctx, packageName, connectionID);
    }

    public void unbind(){
        Log.v(TAG, "native client: unbind " + lastServicePackage);
        mMessengerClient.unbindFromService();
    }

    private boolean initServiceConnection(Context ctx, String servicePackage, String connectionID)
    {
        if (mMessengerClient == null)
        {
            mMessengerClient = new NestedStruct1InterfaceClient(ctx, connectionID);
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
    public void onSig1(NestedStruct1 param1)
    {
        Log.w(TAG, "NOTIFICATION from messenger client Signal sig1 "+ " " + param1);
        nativeOnSig1(param1);
    }
     private native void nativeOnProp1Changed(NestedStruct1 prop1);
    private native void nativeOnSig1(NestedStruct1 param1);
    private native void nativeOnFunc1Result(NestedStruct1 result, String callId);
    private native void nativeIsReady(boolean isReady);
}
