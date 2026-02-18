package testbed1.testbed1jniclient;

import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_api.AbstractStructArrayInterface;
import testbed1.testbed1_api.IStructArrayInterfaceEventListener;

import testbed1.testbed1_android_client.StructArrayInterfaceClient;
import testbed1.testbed1_api.Enum0;
import testbed1.testbed1_android_messenger.Enum0Parcelable;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_android_messenger.StructBoolParcelable;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_android_messenger.StructFloatParcelable;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_android_messenger.StructIntParcelable;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_android_messenger.StructStringParcelable;
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class StructArrayInterfaceJniClient extends AbstractStructArrayInterface implements IStructArrayInterfaceEventListener
{

    private static final String TAG = "StructArrayInterfaceJniClient";

    private StructArrayInterfaceClient mMessengerClient = null;


    private static String ModuleName = "testbed1.testbed1jniservice.StructArrayInterfaceJniService";
    private String lastServicePackage ="";

    @Override
    public boolean _isReady()
    {
        return mMessengerClient != null ? mMessengerClient._isReady() : false;
    }
    @Override
    public void setPropBool(StructBool[] propBool)
    {
        Log.i(TAG, "got request from ue, setPropBool" + (propBool));
        mMessengerClient.setPropBool(propBool);
    }
    @Override
    public StructBool[] getPropBool()
    {
        Log.i(TAG, "got request from ue, getPropBool");
        return mMessengerClient.getPropBool();
    }
    
    @Override
    public void setPropInt(StructInt[] propInt)
    {
        Log.i(TAG, "got request from ue, setPropInt" + (propInt));
        mMessengerClient.setPropInt(propInt);
    }
    @Override
    public StructInt[] getPropInt()
    {
        Log.i(TAG, "got request from ue, getPropInt");
        return mMessengerClient.getPropInt();
    }
    
    @Override
    public void setPropFloat(StructFloat[] propFloat)
    {
        Log.i(TAG, "got request from ue, setPropFloat" + (propFloat));
        mMessengerClient.setPropFloat(propFloat);
    }
    @Override
    public StructFloat[] getPropFloat()
    {
        Log.i(TAG, "got request from ue, getPropFloat");
        return mMessengerClient.getPropFloat();
    }
    
    @Override
    public void setPropString(StructString[] propString)
    {
        Log.i(TAG, "got request from ue, setPropString" + (propString));
        mMessengerClient.setPropString(propString);
    }
    @Override
    public StructString[] getPropString()
    {
        Log.i(TAG, "got request from ue, getPropString");
        return mMessengerClient.getPropString();
    }
    
    @Override
    public void setPropEnum(Enum0[] propEnum)
    {
        Log.i(TAG, "got request from ue, setPropEnum" + (propEnum));
        mMessengerClient.setPropEnum(propEnum);
    }
    @Override
    public Enum0[] getPropEnum()
    {
        Log.i(TAG, "got request from ue, getPropEnum");
        return mMessengerClient.getPropEnum();
    }
    
     @Override
     public StructBool[] funcBool(StructBool[] paramBool)
     {
        Log.v(TAG, "Blocking callfuncBool - should not be used ");
        return mMessengerClient.funcBool(paramBool);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * It returns result via nativeOnFuncBoolResult with the same callId.
    *
    * @param callId async call identifier
    */
    public void funcBoolAsync(String callId, StructBool[] paramBool){
        Log.v(TAG, "non blocking call funcBool ");
        mMessengerClient.funcBoolAsync(paramBool).thenAccept(i -> {
            nativeOnFuncBoolResult(i, callId);});
    }

    @Override
    public CompletableFuture<StructBool[]> funcBoolAsync(StructBool[] paramBool)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcBoolAsync(paramBool);
    }
     @Override
     public StructInt[] funcInt(StructInt[] paramInt)
     {
        Log.v(TAG, "Blocking callfuncInt - should not be used ");
        return mMessengerClient.funcInt(paramInt);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * It returns result via nativeOnFuncIntResult with the same callId.
    *
    * @param callId async call identifier
    */
    public void funcIntAsync(String callId, StructInt[] paramInt){
        Log.v(TAG, "non blocking call funcInt ");
        mMessengerClient.funcIntAsync(paramInt).thenAccept(i -> {
            nativeOnFuncIntResult(i, callId);});
    }

    @Override
    public CompletableFuture<StructInt[]> funcIntAsync(StructInt[] paramInt)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcIntAsync(paramInt);
    }
     @Override
     public StructFloat[] funcFloat(StructFloat[] paramFloat)
     {
        Log.v(TAG, "Blocking callfuncFloat - should not be used ");
        return mMessengerClient.funcFloat(paramFloat);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * It returns result via nativeOnFuncFloatResult with the same callId.
    *
    * @param callId async call identifier
    */
    public void funcFloatAsync(String callId, StructFloat[] paramFloat){
        Log.v(TAG, "non blocking call funcFloat ");
        mMessengerClient.funcFloatAsync(paramFloat).thenAccept(i -> {
            nativeOnFuncFloatResult(i, callId);});
    }

    @Override
    public CompletableFuture<StructFloat[]> funcFloatAsync(StructFloat[] paramFloat)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcFloatAsync(paramFloat);
    }
     @Override
     public StructString[] funcString(StructString[] paramString)
     {
        Log.v(TAG, "Blocking callfuncString - should not be used ");
        return mMessengerClient.funcString(paramString);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * It returns result via nativeOnFuncStringResult with the same callId.
    *
    * @param callId async call identifier
    */
    public void funcStringAsync(String callId, StructString[] paramString){
        Log.v(TAG, "non blocking call funcString ");
        mMessengerClient.funcStringAsync(paramString).thenAccept(i -> {
            nativeOnFuncStringResult(i, callId);});
    }

    @Override
    public CompletableFuture<StructString[]> funcStringAsync(StructString[] paramString)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcStringAsync(paramString);
    }
     @Override
     public Enum0[] funcEnum(Enum0[] paramEnum)
     {
        Log.v(TAG, "Blocking callfuncEnum - should not be used ");
        return mMessengerClient.funcEnum(paramEnum);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * It returns result via nativeOnFuncEnumResult with the same callId.
    *
    * @param callId async call identifier
    */
    public void funcEnumAsync(String callId, Enum0[] paramEnum){
        Log.v(TAG, "non blocking call funcEnum ");
        mMessengerClient.funcEnumAsync(paramEnum).thenAccept(i -> {
            nativeOnFuncEnumResult(i, callId);});
    }

    @Override
    public CompletableFuture<Enum0[]> funcEnumAsync(Enum0[] paramEnum)
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
            mMessengerClient = new StructArrayInterfaceClient(ctx, connectionID);
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
    public void onPropBoolChanged(StructBool[] newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropBoolChanged(newValue);
    }
    @Override
    public void onPropIntChanged(StructInt[] newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropIntChanged(newValue);
    }
    @Override
    public void onPropFloatChanged(StructFloat[] newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropFloatChanged(newValue);
    }
    @Override
    public void onPropStringChanged(StructString[] newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropStringChanged(newValue);
    }
    @Override
    public void onPropEnumChanged(Enum0[] newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropEnumChanged(newValue);
    }
    @Override
    public void onSigBool(StructBool[] paramBool)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigBool "+ " " + paramBool);
        nativeOnSigBool(paramBool);
    }
    @Override
    public void onSigInt(StructInt[] paramInt)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigInt "+ " " + paramInt);
        nativeOnSigInt(paramInt);
    }
    @Override
    public void onSigFloat(StructFloat[] paramFloat)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigFloat "+ " " + paramFloat);
        nativeOnSigFloat(paramFloat);
    }
    @Override
    public void onSigString(StructString[] paramString)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigString "+ " " + paramString);
        nativeOnSigString(paramString);
    }
    @Override
    public void onSigEnum(Enum0[] paramEnum)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigEnum "+ " " + paramEnum);
        nativeOnSigEnum(paramEnum);
    }
     private native void nativeOnPropBoolChanged(StructBool[] propBool);
     private native void nativeOnPropIntChanged(StructInt[] propInt);
     private native void nativeOnPropFloatChanged(StructFloat[] propFloat);
     private native void nativeOnPropStringChanged(StructString[] propString);
     private native void nativeOnPropEnumChanged(Enum0[] propEnum);
    private native void nativeOnSigBool(StructBool[] paramBool);
    private native void nativeOnSigInt(StructInt[] paramInt);
    private native void nativeOnSigFloat(StructFloat[] paramFloat);
    private native void nativeOnSigString(StructString[] paramString);
    private native void nativeOnSigEnum(Enum0[] paramEnum);
    private native void nativeOnFuncBoolResult(StructBool[] result, String callId);
    private native void nativeOnFuncIntResult(StructInt[] result, String callId);
    private native void nativeOnFuncFloatResult(StructFloat[] result, String callId);
    private native void nativeOnFuncStringResult(StructString[] result, String callId);
    private native void nativeOnFuncEnumResult(Enum0[] result, String callId);
    private native void nativeIsReady(boolean isReady);
}
