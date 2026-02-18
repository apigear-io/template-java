package testbed1.testbed1jniservice;

import android.os.Messenger;
import android.util.Log;

import testbed1.testbed1_api.IStructInterface;
import testbed1.testbed1_api.AbstractStructInterface;
import testbed1.testbed1_api.IStructInterfaceEventListener;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_android_messenger.StructBoolParcelable;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_android_messenger.StructFloatParcelable;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_android_messenger.StructIntParcelable;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_android_messenger.StructStringParcelable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class StructInterfaceJniService extends AbstractStructInterface {


    private final static String TAG = "StructInterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public StructInterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setPropBool(StructBool propBool)
    {
        Log.i(TAG, "request setPropBool called, will call native ");
        nativeSetPropBool(propBool);
    }

    @Override
    public StructBool getPropBool()
    {
        Log.i(TAG, "request getPropBool called, will call native ");
        return nativeGetPropBool();
    }

  
    @Override
    public void setPropInt(StructInt propInt)
    {
        Log.i(TAG, "request setPropInt called, will call native ");
        nativeSetPropInt(propInt);
    }

    @Override
    public StructInt getPropInt()
    {
        Log.i(TAG, "request getPropInt called, will call native ");
        return nativeGetPropInt();
    }

  
    @Override
    public void setPropFloat(StructFloat propFloat)
    {
        Log.i(TAG, "request setPropFloat called, will call native ");
        nativeSetPropFloat(propFloat);
    }

    @Override
    public StructFloat getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called, will call native ");
        return nativeGetPropFloat();
    }

  
    @Override
    public void setPropString(StructString propString)
    {
        Log.i(TAG, "request setPropString called, will call native ");
        nativeSetPropString(propString);
    }

    @Override
    public StructString getPropString()
    {
        Log.i(TAG, "request getPropString called, will call native ");
        return nativeGetPropString();
    }

  
    // methods

    @Override
    public StructBool funcBool(StructBool paramBool) {
        Log.i(TAG, "request method funcBool called, will call native");
        return nativeFuncBool(paramBool);
    }

    @Override
    public  CompletableFuture<StructBool> funcBoolAsync(StructBool paramBool) {
        return CompletableFuture.supplyAsync(
                () -> {return funcBool(paramBool); },
                executor);
    }

    @Override
    public StructInt funcInt(StructInt paramInt) {
        Log.i(TAG, "request method funcInt called, will call native");
        return nativeFuncInt(paramInt);
    }

    @Override
    public  CompletableFuture<StructInt> funcIntAsync(StructInt paramInt) {
        return CompletableFuture.supplyAsync(
                () -> {return funcInt(paramInt); },
                executor);
    }

    @Override
    public StructFloat funcFloat(StructFloat paramFloat) {
        Log.i(TAG, "request method funcFloat called, will call native");
        return nativeFuncFloat(paramFloat);
    }

    @Override
    public  CompletableFuture<StructFloat> funcFloatAsync(StructFloat paramFloat) {
        return CompletableFuture.supplyAsync(
                () -> {return funcFloat(paramFloat); },
                executor);
    }

    @Override
    public StructString funcString(StructString paramString) {
        Log.i(TAG, "request method funcString called, will call native");
        return nativeFuncString(paramString);
    }

    @Override
    public  CompletableFuture<StructString> funcStringAsync(StructString paramString) {
        return CompletableFuture.supplyAsync(
                () -> {return funcString(paramString); },
                executor);
    }    

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    private native void nativeSetPropBool(StructBool propBool);
    private native StructBool nativeGetPropBool();
  
    private native void nativeSetPropInt(StructInt propInt);
    private native StructInt nativeGetPropInt();
  
    private native void nativeSetPropFloat(StructFloat propFloat);
    private native StructFloat nativeGetPropFloat();
  
    private native void nativeSetPropString(StructString propString);
    private native StructString nativeGetPropString();
  
    // methods
    private native StructBool nativeFuncBool(StructBool paramBool);
    private native StructInt nativeFuncInt(StructInt paramInt);
    private native StructFloat nativeFuncFloat(StructFloat paramFloat);
    private native StructString nativeFuncString(StructString paramString);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    //In theory event listener interface
    public void onPropBoolChanged(StructBool newValue)
    {
         Log.i(TAG, "onPropBoolChanged, will pass notification to all listeners");
         firePropBoolChanged(newValue);
    }
    public void onPropIntChanged(StructInt newValue)
    {
         Log.i(TAG, "onPropIntChanged, will pass notification to all listeners");
         firePropIntChanged(newValue);
    }
    public void onPropFloatChanged(StructFloat newValue)
    {
         Log.i(TAG, "onPropFloatChanged, will pass notification to all listeners");
         firePropFloatChanged(newValue);
    }
    public void onPropStringChanged(StructString newValue)
    {
         Log.i(TAG, "onPropStringChanged, will pass notification to all listeners");
         firePropStringChanged(newValue);
    }
    public void onSigBool(StructBool paramBool)
    {
        Log.i(TAG, "onSigBool, will pass notification to all listeners");
        fireSigBool(paramBool);
    }
    public void onSigInt(StructInt paramInt)
    {
        Log.i(TAG, "onSigInt, will pass notification to all listeners");
        fireSigInt(paramInt);
    }
    public void onSigFloat(StructFloat paramFloat)
    {
        Log.i(TAG, "onSigFloat, will pass notification to all listeners");
        fireSigFloat(paramFloat);
    }
    public void onSigString(StructString paramString)
    {
        Log.i(TAG, "onSigString, will pass notification to all listeners");
        fireSigString(paramString);
    }

}
