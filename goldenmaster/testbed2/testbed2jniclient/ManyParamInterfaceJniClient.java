package testbed2.testbed2jniclient;

import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_api.AbstractManyParamInterface;
import testbed2.testbed2_api.IManyParamInterfaceEventListener;

import testbed2.testbed2_android_client.ManyParamInterfaceClient;
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



public class ManyParamInterfaceJniClient extends AbstractManyParamInterface implements IManyParamInterfaceEventListener
{

    private static final String TAG = "ManyParamInterfaceJniClient";

    private ManyParamInterfaceClient mMessengerClient = null;


    private static String ModuleName = "testbed2.testbed2jniservice.ManyParamInterfaceJniService";
    private String lastServicePackage ="";

    @Override
    public boolean _isReady()
    {
        return mMessengerClient._isReady();
    }
    @Override
    public void setProp1(int prop1)
    {
        Log.i(TAG, "got request from ue, setProp1" + (prop1));
        mMessengerClient.setProp1(prop1);
    }
    @Override
    public int getProp1()
    {
        Log.i(TAG, "got request from ue, getProp1");
        return mMessengerClient.getProp1();
    }
    
    @Override
    public void setProp2(int prop2)
    {
        Log.i(TAG, "got request from ue, setProp2" + (prop2));
        mMessengerClient.setProp2(prop2);
    }
    @Override
    public int getProp2()
    {
        Log.i(TAG, "got request from ue, getProp2");
        return mMessengerClient.getProp2();
    }
    
    @Override
    public void setProp3(int prop3)
    {
        Log.i(TAG, "got request from ue, setProp3" + (prop3));
        mMessengerClient.setProp3(prop3);
    }
    @Override
    public int getProp3()
    {
        Log.i(TAG, "got request from ue, getProp3");
        return mMessengerClient.getProp3();
    }
    
    @Override
    public void setProp4(int prop4)
    {
        Log.i(TAG, "got request from ue, setProp4" + (prop4));
        mMessengerClient.setProp4(prop4);
    }
    @Override
    public int getProp4()
    {
        Log.i(TAG, "got request from ue, getProp4");
        return mMessengerClient.getProp4();
    }
    
     public int func1(int param1)
     {
        Log.v(TAG, "Blocking callfunc1 - should not be used ");
        return mMessengerClient.func1(param1);
    }

    public void func1Async(String callId, int param1){
        Log.v(TAG, "non blocking call func1 ");
        mMessengerClient.func1Async(param1).thenAccept(i -> {
            nativeOnFunc1Result(i, callId);});
    }

    //Should not be called directly, use func1Async(String callId, int param1)
    public CompletableFuture<Integer> func1Async(int param1)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func1Async(param1);
    }
     public int func2(int param1, int param2)
     {
        Log.v(TAG, "Blocking callfunc2 - should not be used ");
        return mMessengerClient.func2(param1, param2);
    }

    public void func2Async(String callId, int param1, int param2){
        Log.v(TAG, "non blocking call func2 ");
        mMessengerClient.func2Async(param1, param2).thenAccept(i -> {
            nativeOnFunc2Result(i, callId);});
    }

    //Should not be called directly, use func2Async(String callId, int param1, int param2)
    public CompletableFuture<Integer> func2Async(int param1, int param2)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func2Async(param1, param2);
    }
     public int func3(int param1, int param2, int param3)
     {
        Log.v(TAG, "Blocking callfunc3 - should not be used ");
        return mMessengerClient.func3(param1, param2, param3);
    }

    public void func3Async(String callId, int param1, int param2, int param3){
        Log.v(TAG, "non blocking call func3 ");
        mMessengerClient.func3Async(param1, param2, param3).thenAccept(i -> {
            nativeOnFunc3Result(i, callId);});
    }

    //Should not be called directly, use func3Async(String callId, int param1, int param2, int param3)
    public CompletableFuture<Integer> func3Async(int param1, int param2, int param3)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func3Async(param1, param2, param3);
    }
     public int func4(int param1, int param2, int param3, int param4)
     {
        Log.v(TAG, "Blocking callfunc4 - should not be used ");
        return mMessengerClient.func4(param1, param2, param3, param4);
    }

    public void func4Async(String callId, int param1, int param2, int param3, int param4){
        Log.v(TAG, "non blocking call func4 ");
        mMessengerClient.func4Async(param1, param2, param3, param4).thenAccept(i -> {
            nativeOnFunc4Result(i, callId);});
    }

    //Should not be called directly, use func4Async(String callId, int param1, int param2, int param3, int param4)
    public CompletableFuture<Integer> func4Async(int param1, int param2, int param3, int param4)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func4Async(param1, param2, param3, param4);
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
            mMessengerClient = new ManyParamInterfaceClient(ctx, connectionID);
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
    public void onProp1Changed(int newValue)
    {
        Log.w(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp1Changed(newValue);
    }
    @Override
    public void onProp2Changed(int newValue)
    {
        Log.w(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp2Changed(newValue);
    }
    @Override
    public void onProp3Changed(int newValue)
    {
        Log.w(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp3Changed(newValue);
    }
    @Override
    public void onProp4Changed(int newValue)
    {
        Log.w(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp4Changed(newValue);
    }
    @Override
    public void onSig1(int param1)
    {
        Log.w(TAG, "NOTIFICATION from messenger client Signal sig1 "+ " " + param1);
        nativeOnSig1(param1);
    }
    @Override
    public void onSig2(int param1, int param2)
    {
        Log.w(TAG, "NOTIFICATION from messenger client Signal sig2 "+ " " + param1+ " " + param2);
        nativeOnSig2(param1, param2);
    }
    @Override
    public void onSig3(int param1, int param2, int param3)
    {
        Log.w(TAG, "NOTIFICATION from messenger client Signal sig3 "+ " " + param1+ " " + param2+ " " + param3);
        nativeOnSig3(param1, param2, param3);
    }
    @Override
    public void onSig4(int param1, int param2, int param3, int param4)
    {
        Log.w(TAG, "NOTIFICATION from messenger client Signal sig4 "+ " " + param1+ " " + param2+ " " + param3+ " " + param4);
        nativeOnSig4(param1, param2, param3, param4);
    }
     private native void nativeOnProp1Changed(int prop1);
     private native void nativeOnProp2Changed(int prop2);
     private native void nativeOnProp3Changed(int prop3);
     private native void nativeOnProp4Changed(int prop4);
    private native void nativeOnSig1(int param1);
    private native void nativeOnSig2(int param1, int param2);
    private native void nativeOnSig3(int param1, int param2, int param3);
    private native void nativeOnSig4(int param1, int param2, int param3, int param4);
    private native void nativeOnFunc1Result(int result, String callId);
    private native void nativeOnFunc2Result(int result, String callId);
    private native void nativeOnFunc3Result(int result, String callId);
    private native void nativeOnFunc4Result(int result, String callId);
    private native void nativeIsReady(boolean isReady);
}
