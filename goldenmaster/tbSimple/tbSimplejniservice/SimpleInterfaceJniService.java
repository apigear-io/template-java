package tbSimple.tbSimplejniservice;

import android.os.Messenger;
import android.util.Log;

import tbSimple.tbSimple_api.ISimpleInterface;
import tbSimple.tbSimple_api.AbstractSimpleInterface;
import tbSimple.tbSimple_api.ISimpleInterfaceEventListener;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class SimpleInterfaceJniService extends AbstractSimpleInterface {


    private final static String TAG = "SimpleInterfaceJniService";
    private static boolean isServiceReady = false;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SimpleInterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setPropBool(boolean propBool)
    {
        Log.i(TAG, "request setPropBool called, will call native ");
        nativeSetPropBool(propBool);
    }

    @Override
    public boolean getPropBool()
    {
        Log.i(TAG, "request getPropBool called, will call native ");
        return nativeGetPropBool();
    }

  
    @Override
    public void setPropInt(int propInt)
    {
        Log.i(TAG, "request setPropInt called, will call native ");
        nativeSetPropInt(propInt);
    }

    @Override
    public int getPropInt()
    {
        Log.i(TAG, "request getPropInt called, will call native ");
        return nativeGetPropInt();
    }

  
    @Override
    public void setPropInt32(int propInt32)
    {
        Log.i(TAG, "request setPropInt32 called, will call native ");
        nativeSetPropInt32(propInt32);
    }

    @Override
    public int getPropInt32()
    {
        Log.i(TAG, "request getPropInt32 called, will call native ");
        return nativeGetPropInt32();
    }

  
    @Override
    public void setPropInt64(long propInt64)
    {
        Log.i(TAG, "request setPropInt64 called, will call native ");
        nativeSetPropInt64(propInt64);
    }

    @Override
    public long getPropInt64()
    {
        Log.i(TAG, "request getPropInt64 called, will call native ");
        return nativeGetPropInt64();
    }

  
    @Override
    public void setPropFloat(float propFloat)
    {
        Log.i(TAG, "request setPropFloat called, will call native ");
        nativeSetPropFloat(propFloat);
    }

    @Override
    public float getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called, will call native ");
        return nativeGetPropFloat();
    }

  
    @Override
    public void setPropFloat32(float propFloat32)
    {
        Log.i(TAG, "request setPropFloat32 called, will call native ");
        nativeSetPropFloat32(propFloat32);
    }

    @Override
    public float getPropFloat32()
    {
        Log.i(TAG, "request getPropFloat32 called, will call native ");
        return nativeGetPropFloat32();
    }

  
    @Override
    public void setPropFloat64(double propFloat64)
    {
        Log.i(TAG, "request setPropFloat64 called, will call native ");
        nativeSetPropFloat64(propFloat64);
    }

    @Override
    public double getPropFloat64()
    {
        Log.i(TAG, "request getPropFloat64 called, will call native ");
        return nativeGetPropFloat64();
    }

  
    @Override
    public void setPropString(String propString)
    {
        Log.i(TAG, "request setPropString called, will call native ");
        nativeSetPropString(propString);
    }

    @Override
    public String getPropString()
    {
        Log.i(TAG, "request getPropString called, will call native ");
        return nativeGetPropString();
    }

  
    // methods

    @Override
    public void funcNoReturnValue(boolean paramBool) {
        Log.i(TAG, "request method funcNoReturnValue called, will call native");
         nativeFuncNoReturnValue(paramBool);
    }

    @Override
    public  CompletableFuture<Void> funcNoReturnValueAsync(boolean paramBool) {
        return CompletableFuture.runAsync(
                () -> { funcNoReturnValue(paramBool); },
                executor);
    }

    @Override
    public boolean funcNoParams() {
        Log.i(TAG, "request method funcNoParams called, will call native");
        return nativeFuncNoParams();
    }

    @Override
    public  CompletableFuture<Boolean> funcNoParamsAsync() {
        return CompletableFuture.supplyAsync(
                () -> {return funcNoParams(); },
                executor);
    }

    @Override
    public boolean funcBool(boolean paramBool) {
        Log.i(TAG, "request method funcBool called, will call native");
        return nativeFuncBool(paramBool);
    }

    @Override
    public  CompletableFuture<Boolean> funcBoolAsync(boolean paramBool) {
        return CompletableFuture.supplyAsync(
                () -> {return funcBool(paramBool); },
                executor);
    }

    @Override
    public int funcInt(int paramInt) {
        Log.i(TAG, "request method funcInt called, will call native");
        return nativeFuncInt(paramInt);
    }

    @Override
    public  CompletableFuture<Integer> funcIntAsync(int paramInt) {
        return CompletableFuture.supplyAsync(
                () -> {return funcInt(paramInt); },
                executor);
    }

    @Override
    public int funcInt32(int paramInt32) {
        Log.i(TAG, "request method funcInt32 called, will call native");
        return nativeFuncInt32(paramInt32);
    }

    @Override
    public  CompletableFuture<Integer> funcInt32Async(int paramInt32) {
        return CompletableFuture.supplyAsync(
                () -> {return funcInt32(paramInt32); },
                executor);
    }

    @Override
    public long funcInt64(long paramInt64) {
        Log.i(TAG, "request method funcInt64 called, will call native");
        return nativeFuncInt64(paramInt64);
    }

    @Override
    public  CompletableFuture<Long> funcInt64Async(long paramInt64) {
        return CompletableFuture.supplyAsync(
                () -> {return funcInt64(paramInt64); },
                executor);
    }

    @Override
    public float funcFloat(float paramFloat) {
        Log.i(TAG, "request method funcFloat called, will call native");
        return nativeFuncFloat(paramFloat);
    }

    @Override
    public  CompletableFuture<Float> funcFloatAsync(float paramFloat) {
        return CompletableFuture.supplyAsync(
                () -> {return funcFloat(paramFloat); },
                executor);
    }

    @Override
    public float funcFloat32(float paramFloat32) {
        Log.i(TAG, "request method funcFloat32 called, will call native");
        return nativeFuncFloat32(paramFloat32);
    }

    @Override
    public  CompletableFuture<Float> funcFloat32Async(float paramFloat32) {
        return CompletableFuture.supplyAsync(
                () -> {return funcFloat32(paramFloat32); },
                executor);
    }

    @Override
    public double funcFloat64(double paramFloat) {
        Log.i(TAG, "request method funcFloat64 called, will call native");
        return nativeFuncFloat64(paramFloat);
    }

    @Override
    public  CompletableFuture<Double> funcFloat64Async(double paramFloat) {
        return CompletableFuture.supplyAsync(
                () -> {return funcFloat64(paramFloat); },
                executor);
    }

    @Override
    public String funcString(String paramString) {
        Log.i(TAG, "request method funcString called, will call native");
        return nativeFuncString(paramString);
    }

    @Override
    public  CompletableFuture<String> funcStringAsync(String paramString) {
        return CompletableFuture.supplyAsync(
                () -> {return funcString(paramString); },
                executor);
    }    

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    private native void nativeSetPropBool(boolean propBool);
    private native boolean nativeGetPropBool();
  
    private native void nativeSetPropInt(int propInt);
    private native int nativeGetPropInt();
  
    private native void nativeSetPropInt32(int propInt32);
    private native int nativeGetPropInt32();
  
    private native void nativeSetPropInt64(long propInt64);
    private native long nativeGetPropInt64();
  
    private native void nativeSetPropFloat(float propFloat);
    private native float nativeGetPropFloat();
  
    private native void nativeSetPropFloat32(float propFloat32);
    private native float nativeGetPropFloat32();
  
    private native void nativeSetPropFloat64(double propFloat64);
    private native double nativeGetPropFloat64();
  
    private native void nativeSetPropString(String propString);
    private native String nativeGetPropString();
  
    // methods
    private native void nativeFuncNoReturnValue(boolean paramBool);
    private native boolean nativeFuncNoParams();
    private native boolean nativeFuncBool(boolean paramBool);
    private native int nativeFuncInt(int paramInt);
    private native int nativeFuncInt32(int paramInt32);
    private native long nativeFuncInt64(long paramInt64);
    private native float nativeFuncFloat(float paramFloat);
    private native float nativeFuncFloat32(float paramFloat32);
    private native double nativeFuncFloat64(double paramFloat);
    private native String nativeFuncString(String paramString);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    //In theory event listener interface
    public void onPropBoolChanged(boolean newValue)
    {
         Log.i(TAG, "onPropBoolChanged, will pass notification to all listeners");
         firePropBoolChanged(newValue);
    }
    public void onPropIntChanged(int newValue)
    {
         Log.i(TAG, "onPropIntChanged, will pass notification to all listeners");
         firePropIntChanged(newValue);
    }
    public void onPropInt32Changed(int newValue)
    {
         Log.i(TAG, "onPropInt32Changed, will pass notification to all listeners");
         firePropInt32Changed(newValue);
    }
    public void onPropInt64Changed(long newValue)
    {
         Log.i(TAG, "onPropInt64Changed, will pass notification to all listeners");
         firePropInt64Changed(newValue);
    }
    public void onPropFloatChanged(float newValue)
    {
         Log.i(TAG, "onPropFloatChanged, will pass notification to all listeners");
         firePropFloatChanged(newValue);
    }
    public void onPropFloat32Changed(float newValue)
    {
         Log.i(TAG, "onPropFloat32Changed, will pass notification to all listeners");
         firePropFloat32Changed(newValue);
    }
    public void onPropFloat64Changed(double newValue)
    {
         Log.i(TAG, "onPropFloat64Changed, will pass notification to all listeners");
         firePropFloat64Changed(newValue);
    }
    public void onPropStringChanged(String newValue)
    {
         Log.i(TAG, "onPropStringChanged, will pass notification to all listeners");
         firePropStringChanged(newValue);
    }
    public void onSigBool(boolean paramBool)
    {
        Log.i(TAG, "onSigBool, will pass notification to all listeners");
        fireSigBool(paramBool);
    }
    public void onSigInt(int paramInt)
    {
        Log.i(TAG, "onSigInt, will pass notification to all listeners");
        fireSigInt(paramInt);
    }
    public void onSigInt32(int paramInt32)
    {
        Log.i(TAG, "onSigInt32, will pass notification to all listeners");
        fireSigInt32(paramInt32);
    }
    public void onSigInt64(long paramInt64)
    {
        Log.i(TAG, "onSigInt64, will pass notification to all listeners");
        fireSigInt64(paramInt64);
    }
    public void onSigFloat(float paramFloat)
    {
        Log.i(TAG, "onSigFloat, will pass notification to all listeners");
        fireSigFloat(paramFloat);
    }
    public void onSigFloat32(float paramFloat32)
    {
        Log.i(TAG, "onSigFloat32, will pass notification to all listeners");
        fireSigFloat32(paramFloat32);
    }
    public void onSigFloat64(double paramFloat64)
    {
        Log.i(TAG, "onSigFloat64, will pass notification to all listeners");
        fireSigFloat64(paramFloat64);
    }
    public void onSigString(String paramString)
    {
        Log.i(TAG, "onSigString, will pass notification to all listeners");
        fireSigString(paramString);
    }

}
