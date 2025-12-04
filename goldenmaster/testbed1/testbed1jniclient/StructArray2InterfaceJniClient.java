package testbed1.testbed1jniclient;

import testbed1.testbed1_api.IStructArray2Interface;
import testbed1.testbed1_api.AbstractStructArray2Interface;
import testbed1.testbed1_api.IStructArray2InterfaceEventListener;

import testbed1.testbed1_android_client.StructArray2InterfaceClient;
import testbed1.testbed1_api.Enum0;
import testbed1.testbed1_android_messenger.Enum0Parcelable;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_android_messenger.StructBoolParcelable;
import testbed1.testbed1_api.StructBoolWithArray;
import testbed1.testbed1_android_messenger.StructBoolWithArrayParcelable;
import testbed1.testbed1_api.StructEnumWithArray;
import testbed1.testbed1_android_messenger.StructEnumWithArrayParcelable;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_android_messenger.StructFloatParcelable;
import testbed1.testbed1_api.StructFloatWithArray;
import testbed1.testbed1_android_messenger.StructFloatWithArrayParcelable;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_android_messenger.StructIntParcelable;
import testbed1.testbed1_api.StructIntWithArray;
import testbed1.testbed1_android_messenger.StructIntWithArrayParcelable;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_android_messenger.StructStringParcelable;
import testbed1.testbed1_api.StructStringWithArray;
import testbed1.testbed1_android_messenger.StructStringWithArrayParcelable;
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class StructArray2InterfaceJniClient extends AbstractStructArray2Interface implements IStructArray2InterfaceEventListener
{

    private static final String TAG = "StructArray2InterfaceJniClient";

    private StructArray2InterfaceClient mMessengerClient = null;


    private static String ModuleName = "testbed1.testbed1jniservice.StructArray2InterfaceJniService";
    private String lastServicePackage ="";

    @Override
    public boolean _isReady()
    {
        return mMessengerClient._isReady();
    }
    @Override
    public void setPropBool(StructBoolWithArray propBool)
    {
        Log.i(TAG, "got request from ue, setPropBool" + (propBool));
        mMessengerClient.setPropBool(propBool);
    }
    @Override
    public StructBoolWithArray getPropBool()
    {
        Log.i(TAG, "got request from ue, getPropBool");
        return mMessengerClient.getPropBool();
    }
    
    @Override
    public void setPropInt(StructIntWithArray propInt)
    {
        Log.i(TAG, "got request from ue, setPropInt" + (propInt));
        mMessengerClient.setPropInt(propInt);
    }
    @Override
    public StructIntWithArray getPropInt()
    {
        Log.i(TAG, "got request from ue, getPropInt");
        return mMessengerClient.getPropInt();
    }
    
    @Override
    public void setPropFloat(StructFloatWithArray propFloat)
    {
        Log.i(TAG, "got request from ue, setPropFloat" + (propFloat));
        mMessengerClient.setPropFloat(propFloat);
    }
    @Override
    public StructFloatWithArray getPropFloat()
    {
        Log.i(TAG, "got request from ue, getPropFloat");
        return mMessengerClient.getPropFloat();
    }
    
    @Override
    public void setPropString(StructStringWithArray propString)
    {
        Log.i(TAG, "got request from ue, setPropString" + (propString));
        mMessengerClient.setPropString(propString);
    }
    @Override
    public StructStringWithArray getPropString()
    {
        Log.i(TAG, "got request from ue, getPropString");
        return mMessengerClient.getPropString();
    }
    
    @Override
    public void setPropEnum(StructEnumWithArray propEnum)
    {
        Log.i(TAG, "got request from ue, setPropEnum" + (propEnum));
        mMessengerClient.setPropEnum(propEnum);
    }
    @Override
    public StructEnumWithArray getPropEnum()
    {
        Log.i(TAG, "got request from ue, getPropEnum");
        return mMessengerClient.getPropEnum();
    }
    
     public StructBool[] funcBool(StructBoolWithArray paramBool)
     {
        Log.v(TAG, "Blocking callfuncBool - should not be used ");
        return mMessengerClient.funcBool(paramBool);
    }

    public void funcBoolAsync(String callId, StructBoolWithArray paramBool){
        Log.v(TAG, "non blocking call funcBool ");
        mMessengerClient.funcBoolAsync(paramBool).thenAccept(i -> {
            nativeOnFuncBoolResult(i, callId);});
    }

    //Should not be called directly, use funcBoolAsync(String callId, StructBoolWithArray paramBool)
    public CompletableFuture<StructBool[]> funcBoolAsync(StructBoolWithArray paramBool)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcBoolAsync(paramBool);
    }
     public StructInt[] funcInt(StructIntWithArray paramInt)
     {
        Log.v(TAG, "Blocking callfuncInt - should not be used ");
        return mMessengerClient.funcInt(paramInt);
    }

    public void funcIntAsync(String callId, StructIntWithArray paramInt){
        Log.v(TAG, "non blocking call funcInt ");
        mMessengerClient.funcIntAsync(paramInt).thenAccept(i -> {
            nativeOnFuncIntResult(i, callId);});
    }

    //Should not be called directly, use funcIntAsync(String callId, StructIntWithArray paramInt)
    public CompletableFuture<StructInt[]> funcIntAsync(StructIntWithArray paramInt)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcIntAsync(paramInt);
    }
     public StructFloat[] funcFloat(StructFloatWithArray paramFloat)
     {
        Log.v(TAG, "Blocking callfuncFloat - should not be used ");
        return mMessengerClient.funcFloat(paramFloat);
    }

    public void funcFloatAsync(String callId, StructFloatWithArray paramFloat){
        Log.v(TAG, "non blocking call funcFloat ");
        mMessengerClient.funcFloatAsync(paramFloat).thenAccept(i -> {
            nativeOnFuncFloatResult(i, callId);});
    }

    //Should not be called directly, use funcFloatAsync(String callId, StructFloatWithArray paramFloat)
    public CompletableFuture<StructFloat[]> funcFloatAsync(StructFloatWithArray paramFloat)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcFloatAsync(paramFloat);
    }
     public StructString[] funcString(StructStringWithArray paramString)
     {
        Log.v(TAG, "Blocking callfuncString - should not be used ");
        return mMessengerClient.funcString(paramString);
    }

    public void funcStringAsync(String callId, StructStringWithArray paramString){
        Log.v(TAG, "non blocking call funcString ");
        mMessengerClient.funcStringAsync(paramString).thenAccept(i -> {
            nativeOnFuncStringResult(i, callId);});
    }

    //Should not be called directly, use funcStringAsync(String callId, StructStringWithArray paramString)
    public CompletableFuture<StructString[]> funcStringAsync(StructStringWithArray paramString)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcStringAsync(paramString);
    }
     public Enum0[] funcEnum(StructEnumWithArray paramEnum)
     {
        Log.v(TAG, "Blocking callfuncEnum - should not be used ");
        return mMessengerClient.funcEnum(paramEnum);
    }

    public void funcEnumAsync(String callId, StructEnumWithArray paramEnum){
        Log.v(TAG, "non blocking call funcEnum ");
        mMessengerClient.funcEnumAsync(paramEnum).thenAccept(i -> {
            nativeOnFuncEnumResult(i, callId);});
    }

    //Should not be called directly, use funcEnumAsync(String callId, StructEnumWithArray paramEnum)
    public CompletableFuture<Enum0[]> funcEnumAsync(StructEnumWithArray paramEnum)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcEnumAsync(paramEnum);
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
            mMessengerClient = new StructArray2InterfaceClient(ctx, connectionID);
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
        Log.i(TAG, "Connection state changed "+isReady);
        nativeIsReady(isReady);
    }

    //Event listener
    @Override
    public void onPropBoolChanged(StructBoolWithArray newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropBoolChanged(newValue);
    }
    @Override
    public void onPropIntChanged(StructIntWithArray newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropIntChanged(newValue);
    }
    @Override
    public void onPropFloatChanged(StructFloatWithArray newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropFloatChanged(newValue);
    }
    @Override
    public void onPropStringChanged(StructStringWithArray newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropStringChanged(newValue);
    }
    @Override
    public void onPropEnumChanged(StructEnumWithArray newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropEnumChanged(newValue);
    }
    @Override
    public void onSigBool(StructBoolWithArray paramBool)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigBool "+ " " + paramBool);
        nativeOnSigBool(paramBool);
    }
    @Override
    public void onSigInt(StructIntWithArray paramInt)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigInt "+ " " + paramInt);
        nativeOnSigInt(paramInt);
    }
    @Override
    public void onSigFloat(StructFloatWithArray paramFloat)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigFloat "+ " " + paramFloat);
        nativeOnSigFloat(paramFloat);
    }
    @Override
    public void onSigString(StructStringWithArray paramString)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigString "+ " " + paramString);
        nativeOnSigString(paramString);
    }
     private native void nativeOnPropBoolChanged(StructBoolWithArray propBool);
     private native void nativeOnPropIntChanged(StructIntWithArray propInt);
     private native void nativeOnPropFloatChanged(StructFloatWithArray propFloat);
     private native void nativeOnPropStringChanged(StructStringWithArray propString);
     private native void nativeOnPropEnumChanged(StructEnumWithArray propEnum);
    private native void nativeOnSigBool(StructBoolWithArray paramBool);
    private native void nativeOnSigInt(StructIntWithArray paramInt);
    private native void nativeOnSigFloat(StructFloatWithArray paramFloat);
    private native void nativeOnSigString(StructStringWithArray paramString);
    private native void nativeOnFuncBoolResult(StructBool[] result, String callId);
    private native void nativeOnFuncIntResult(StructInt[] result, String callId);
    private native void nativeOnFuncFloatResult(StructFloat[] result, String callId);
    private native void nativeOnFuncStringResult(StructString[] result, String callId);
    private native void nativeOnFuncEnumResult(Enum0[] result, String callId);
    private native void nativeIsReady(boolean isReady);
}
