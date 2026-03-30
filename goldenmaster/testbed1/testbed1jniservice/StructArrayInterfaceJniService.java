package testbed1.testbed1jniservice;

import android.util.Log;

import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_api.AbstractStructArrayInterface;
import testbed1.testbed1_api.IStructArrayInterfaceEventListener;
import testbed1.testbed1_android_messenger.Conversions;
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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;


public class StructArrayInterfaceJniService extends AbstractStructArrayInterface {


    private final static String TAG = "StructArrayInterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public StructArrayInterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setPropBool(List<StructBool> propBool)
    {
        Log.i(TAG, "request setPropBool called, will call native ");
        nativeSetPropBool(Conversions.toArray(propBool, new StructBool[0]));
    }

    @Override
    public List<StructBool> getPropBool()
    {
        Log.i(TAG, "request getPropBool called, will call native ");
        return Conversions.toList(nativeGetPropBool());
    }

  
    @Override
    public void setPropInt(List<StructInt> propInt)
    {
        Log.i(TAG, "request setPropInt called, will call native ");
        nativeSetPropInt(Conversions.toArray(propInt, new StructInt[0]));
    }

    @Override
    public List<StructInt> getPropInt()
    {
        Log.i(TAG, "request getPropInt called, will call native ");
        return Conversions.toList(nativeGetPropInt());
    }

  
    @Override
    public void setPropFloat(List<StructFloat> propFloat)
    {
        Log.i(TAG, "request setPropFloat called, will call native ");
        nativeSetPropFloat(Conversions.toArray(propFloat, new StructFloat[0]));
    }

    @Override
    public List<StructFloat> getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called, will call native ");
        return Conversions.toList(nativeGetPropFloat());
    }

  
    @Override
    public void setPropString(List<StructString> propString)
    {
        Log.i(TAG, "request setPropString called, will call native ");
        nativeSetPropString(Conversions.toArray(propString, new StructString[0]));
    }

    @Override
    public List<StructString> getPropString()
    {
        Log.i(TAG, "request getPropString called, will call native ");
        return Conversions.toList(nativeGetPropString());
    }

  
    @Override
    public void setPropEnum(List<Enum0> propEnum)
    {
        Log.i(TAG, "request setPropEnum called, will call native ");
        nativeSetPropEnum(Conversions.toArray(propEnum, new Enum0[0]));
    }

    @Override
    public List<Enum0> getPropEnum()
    {
        Log.i(TAG, "request getPropEnum called, will call native ");
        return Conversions.toList(nativeGetPropEnum());
    }

  
    // methods

    @Override
    public List<StructBool> funcBool(List<StructBool> paramBool) {
        Log.i(TAG, "request method funcBool called, will call native");
        return Conversions.toList(nativeFuncBool(Conversions.toArray(paramBool, new StructBool[0])));
    }

    @Override
    public  CompletableFuture<List<StructBool>> funcBoolAsync(List<StructBool> paramBool) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcBool(paramBool); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<StructBool>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<StructInt> funcInt(List<StructInt> paramInt) {
        Log.i(TAG, "request method funcInt called, will call native");
        return Conversions.toList(nativeFuncInt(Conversions.toArray(paramInt, new StructInt[0])));
    }

    @Override
    public  CompletableFuture<List<StructInt>> funcIntAsync(List<StructInt> paramInt) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcInt(paramInt); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<StructInt>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<StructFloat> funcFloat(List<StructFloat> paramFloat) {
        Log.i(TAG, "request method funcFloat called, will call native");
        return Conversions.toList(nativeFuncFloat(Conversions.toArray(paramFloat, new StructFloat[0])));
    }

    @Override
    public  CompletableFuture<List<StructFloat>> funcFloatAsync(List<StructFloat> paramFloat) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcFloat(paramFloat); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<StructFloat>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<StructString> funcString(List<StructString> paramString) {
        Log.i(TAG, "request method funcString called, will call native");
        return Conversions.toList(nativeFuncString(Conversions.toArray(paramString, new StructString[0])));
    }

    @Override
    public  CompletableFuture<List<StructString>> funcStringAsync(List<StructString> paramString) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcString(paramString); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<StructString>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<Enum0> funcEnum(List<Enum0> paramEnum) {
        Log.i(TAG, "request method funcEnum called, will call native");
        return Conversions.toList(nativeFuncEnum(Conversions.toArray(paramEnum, new Enum0[0])));
    }

    @Override
    public  CompletableFuture<List<Enum0>> funcEnumAsync(List<Enum0> paramEnum) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcEnum(paramEnum); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<Enum0>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    @Override
    public void _shutdown() {
        isServiceReady = false;
        fire_readyStatusChanged(false);
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // Native methods — use array types for JNI compatibility
    private native void nativeSetPropBool(StructBool[] propBool);
    private native StructBool[] nativeGetPropBool();
  
    private native void nativeSetPropInt(StructInt[] propInt);
    private native StructInt[] nativeGetPropInt();
  
    private native void nativeSetPropFloat(StructFloat[] propFloat);
    private native StructFloat[] nativeGetPropFloat();
  
    private native void nativeSetPropString(StructString[] propString);
    private native StructString[] nativeGetPropString();
  
    private native void nativeSetPropEnum(Enum0[] propEnum);
    private native Enum0[] nativeGetPropEnum();
  
    private native StructBool[] nativeFuncBool(StructBool[] paramBool);
    private native StructInt[] nativeFuncInt(StructInt[] paramInt);
    private native StructFloat[] nativeFuncFloat(StructFloat[] paramFloat);
    private native StructString[] nativeFuncString(StructString[] paramString);
    private native Enum0[] nativeFuncEnum(Enum0[] paramEnum);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    // Callbacks from native — receive arrays, convert to List and fire events
    public void onPropBoolChanged(StructBool[] newValue)
    {
         Log.i(TAG, "onPropBoolChanged, will pass notification to all listeners");
         firePropBoolChanged(Conversions.toList(newValue));
    }
    public void onPropIntChanged(StructInt[] newValue)
    {
         Log.i(TAG, "onPropIntChanged, will pass notification to all listeners");
         firePropIntChanged(Conversions.toList(newValue));
    }
    public void onPropFloatChanged(StructFloat[] newValue)
    {
         Log.i(TAG, "onPropFloatChanged, will pass notification to all listeners");
         firePropFloatChanged(Conversions.toList(newValue));
    }
    public void onPropStringChanged(StructString[] newValue)
    {
         Log.i(TAG, "onPropStringChanged, will pass notification to all listeners");
         firePropStringChanged(Conversions.toList(newValue));
    }
    public void onPropEnumChanged(Enum0[] newValue)
    {
         Log.i(TAG, "onPropEnumChanged, will pass notification to all listeners");
         firePropEnumChanged(Conversions.toList(newValue));
    }
    public void onSigBool(StructBool[] paramBool)
    {
        Log.i(TAG, "onSigBool, will pass notification to all listeners");
        fireSigBool(Conversions.toList(paramBool));
    }
    public void onSigInt(StructInt[] paramInt)
    {
        Log.i(TAG, "onSigInt, will pass notification to all listeners");
        fireSigInt(Conversions.toList(paramInt));
    }
    public void onSigFloat(StructFloat[] paramFloat)
    {
        Log.i(TAG, "onSigFloat, will pass notification to all listeners");
        fireSigFloat(Conversions.toList(paramFloat));
    }
    public void onSigString(StructString[] paramString)
    {
        Log.i(TAG, "onSigString, will pass notification to all listeners");
        fireSigString(Conversions.toList(paramString));
    }
    public void onSigEnum(Enum0[] paramEnum)
    {
        Log.i(TAG, "onSigEnum, will pass notification to all listeners");
        fireSigEnum(Conversions.toList(paramEnum));
    }

}
