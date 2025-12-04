package testbed1.testbed1jniservice;

import android.os.Messenger;
import android.util.Log;

import testbed1.testbed1_api.IStructArray2Interface;
import testbed1.testbed1_api.AbstractStructArray2Interface;
import testbed1.testbed1_api.IStructArray2InterfaceEventListener;
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

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class StructArray2InterfaceJniService extends AbstractStructArray2Interface {


    private final static String TAG = "StructArray2InterfaceJniService";
    private static boolean isServiceReady = false;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public StructArray2InterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setPropBool(StructBoolWithArray propBool)
    {
        Log.i(TAG, "request setPropBool called, will call native ");
        nativeSetPropBool(propBool);
    }

    @Override
    public StructBoolWithArray getPropBool()
    {
        Log.i(TAG, "request getPropBool called, will call native ");
        return nativeGetPropBool();
    }

  
    @Override
    public void setPropInt(StructIntWithArray propInt)
    {
        Log.i(TAG, "request setPropInt called, will call native ");
        nativeSetPropInt(propInt);
    }

    @Override
    public StructIntWithArray getPropInt()
    {
        Log.i(TAG, "request getPropInt called, will call native ");
        return nativeGetPropInt();
    }

  
    @Override
    public void setPropFloat(StructFloatWithArray propFloat)
    {
        Log.i(TAG, "request setPropFloat called, will call native ");
        nativeSetPropFloat(propFloat);
    }

    @Override
    public StructFloatWithArray getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called, will call native ");
        return nativeGetPropFloat();
    }

  
    @Override
    public void setPropString(StructStringWithArray propString)
    {
        Log.i(TAG, "request setPropString called, will call native ");
        nativeSetPropString(propString);
    }

    @Override
    public StructStringWithArray getPropString()
    {
        Log.i(TAG, "request getPropString called, will call native ");
        return nativeGetPropString();
    }

  
    @Override
    public void setPropEnum(StructEnumWithArray propEnum)
    {
        Log.i(TAG, "request setPropEnum called, will call native ");
        nativeSetPropEnum(propEnum);
    }

    @Override
    public StructEnumWithArray getPropEnum()
    {
        Log.i(TAG, "request getPropEnum called, will call native ");
        return nativeGetPropEnum();
    }

  
    // methods

    @Override
    public StructBool[] funcBool(StructBoolWithArray paramBool) {
        Log.i(TAG, "request method funcBool called, will call native");
        return nativeFuncBool(paramBool);
    }

    @Override
    public  CompletableFuture<StructBool[]> funcBoolAsync(StructBoolWithArray paramBool) {
        return CompletableFuture.supplyAsync(
                () -> {return funcBool(paramBool); },
                executor);
    }

    @Override
    public StructInt[] funcInt(StructIntWithArray paramInt) {
        Log.i(TAG, "request method funcInt called, will call native");
        return nativeFuncInt(paramInt);
    }

    @Override
    public  CompletableFuture<StructInt[]> funcIntAsync(StructIntWithArray paramInt) {
        return CompletableFuture.supplyAsync(
                () -> {return funcInt(paramInt); },
                executor);
    }

    @Override
    public StructFloat[] funcFloat(StructFloatWithArray paramFloat) {
        Log.i(TAG, "request method funcFloat called, will call native");
        return nativeFuncFloat(paramFloat);
    }

    @Override
    public  CompletableFuture<StructFloat[]> funcFloatAsync(StructFloatWithArray paramFloat) {
        return CompletableFuture.supplyAsync(
                () -> {return funcFloat(paramFloat); },
                executor);
    }

    @Override
    public StructString[] funcString(StructStringWithArray paramString) {
        Log.i(TAG, "request method funcString called, will call native");
        return nativeFuncString(paramString);
    }

    @Override
    public  CompletableFuture<StructString[]> funcStringAsync(StructStringWithArray paramString) {
        return CompletableFuture.supplyAsync(
                () -> {return funcString(paramString); },
                executor);
    }

    @Override
    public Enum0[] funcEnum(StructEnumWithArray paramEnum) {
        Log.i(TAG, "request method funcEnum called, will call native");
        return nativeFuncEnum(paramEnum);
    }

    @Override
    public  CompletableFuture<Enum0[]> funcEnumAsync(StructEnumWithArray paramEnum) {
        return CompletableFuture.supplyAsync(
                () -> {return funcEnum(paramEnum); },
                executor);
    }    

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    private native void nativeSetPropBool(StructBoolWithArray propBool);
    private native StructBoolWithArray nativeGetPropBool();
  
    private native void nativeSetPropInt(StructIntWithArray propInt);
    private native StructIntWithArray nativeGetPropInt();
  
    private native void nativeSetPropFloat(StructFloatWithArray propFloat);
    private native StructFloatWithArray nativeGetPropFloat();
  
    private native void nativeSetPropString(StructStringWithArray propString);
    private native StructStringWithArray nativeGetPropString();
  
    private native void nativeSetPropEnum(StructEnumWithArray propEnum);
    private native StructEnumWithArray nativeGetPropEnum();
  
    // methods
    private native StructBool[] nativeFuncBool(StructBoolWithArray paramBool);
    private native StructInt[] nativeFuncInt(StructIntWithArray paramInt);
    private native StructFloat[] nativeFuncFloat(StructFloatWithArray paramFloat);
    private native StructString[] nativeFuncString(StructStringWithArray paramString);
    private native Enum0[] nativeFuncEnum(StructEnumWithArray paramEnum);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    //In theory event listener interface
    public void onPropBoolChanged(StructBoolWithArray newValue)
    {
         Log.i(TAG, "onPropBoolChanged, will pass notification to all listeners");
         firePropBoolChanged(newValue);
    }
    public void onPropIntChanged(StructIntWithArray newValue)
    {
         Log.i(TAG, "onPropIntChanged, will pass notification to all listeners");
         firePropIntChanged(newValue);
    }
    public void onPropFloatChanged(StructFloatWithArray newValue)
    {
         Log.i(TAG, "onPropFloatChanged, will pass notification to all listeners");
         firePropFloatChanged(newValue);
    }
    public void onPropStringChanged(StructStringWithArray newValue)
    {
         Log.i(TAG, "onPropStringChanged, will pass notification to all listeners");
         firePropStringChanged(newValue);
    }
    public void onPropEnumChanged(StructEnumWithArray newValue)
    {
         Log.i(TAG, "onPropEnumChanged, will pass notification to all listeners");
         firePropEnumChanged(newValue);
    }
    public void onSigBool(StructBoolWithArray paramBool)
    {
        Log.i(TAG, "onSigBool, will pass notification to all listeners");
        fireSigBool(paramBool);
    }
    public void onSigInt(StructIntWithArray paramInt)
    {
        Log.i(TAG, "onSigInt, will pass notification to all listeners");
        fireSigInt(paramInt);
    }
    public void onSigFloat(StructFloatWithArray paramFloat)
    {
        Log.i(TAG, "onSigFloat, will pass notification to all listeners");
        fireSigFloat(paramFloat);
    }
    public void onSigString(StructStringWithArray paramString)
    {
        Log.i(TAG, "onSigString, will pass notification to all listeners");
        fireSigString(paramString);
    }

}
