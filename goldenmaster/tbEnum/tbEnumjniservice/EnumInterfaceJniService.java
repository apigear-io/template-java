package tbEnum.tbEnumjniservice;

import android.util.Log;

import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_api.AbstractEnumInterface;
import tbEnum.tbEnum_api.IEnumInterfaceEventListener;
import tbEnum.tbEnum_android_messenger.Conversions;
import tbEnum.tbEnum_api.Enum0;
import tbEnum.tbEnum_android_messenger.Enum0Parcelable;
import tbEnum.tbEnum_api.Enum1;
import tbEnum.tbEnum_android_messenger.Enum1Parcelable;
import tbEnum.tbEnum_api.Enum2;
import tbEnum.tbEnum_android_messenger.Enum2Parcelable;
import tbEnum.tbEnum_api.Enum3;
import tbEnum.tbEnum_android_messenger.Enum3Parcelable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;


public class EnumInterfaceJniService extends AbstractEnumInterface {


    private final static String TAG = "EnumInterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public EnumInterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setProp0(Enum0 prop0)
    {
        Log.i(TAG, "request setProp0 called, will call native ");
        nativeSetProp0(prop0);
    }

    @Override
    public Enum0 getProp0()
    {
        Log.i(TAG, "request getProp0 called, will call native ");
        return nativeGetProp0();
    }

  
    @Override
    public void setProp1(Enum1 prop1)
    {
        Log.i(TAG, "request setProp1 called, will call native ");
        nativeSetProp1(prop1);
    }

    @Override
    public Enum1 getProp1()
    {
        Log.i(TAG, "request getProp1 called, will call native ");
        return nativeGetProp1();
    }

  
    @Override
    public void setProp2(Enum2 prop2)
    {
        Log.i(TAG, "request setProp2 called, will call native ");
        nativeSetProp2(prop2);
    }

    @Override
    public Enum2 getProp2()
    {
        Log.i(TAG, "request getProp2 called, will call native ");
        return nativeGetProp2();
    }

  
    @Override
    public void setProp3(Enum3 prop3)
    {
        Log.i(TAG, "request setProp3 called, will call native ");
        nativeSetProp3(prop3);
    }

    @Override
    public Enum3 getProp3()
    {
        Log.i(TAG, "request getProp3 called, will call native ");
        return nativeGetProp3();
    }

  
    // methods

    @Override
    public Enum0 func0(Enum0 param0) {
        Log.i(TAG, "request method func0 called, will call native");
        return nativeFunc0(param0);
    }

    @Override
    public  CompletableFuture<Enum0> func0Async(Enum0 param0) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func0(param0); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Enum0> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public Enum1 func1(Enum1 param1) {
        Log.i(TAG, "request method func1 called, will call native");
        return nativeFunc1(param1);
    }

    @Override
    public  CompletableFuture<Enum1> func1Async(Enum1 param1) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func1(param1); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Enum1> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public Enum2 func2(Enum2 param2) {
        Log.i(TAG, "request method func2 called, will call native");
        return nativeFunc2(param2);
    }

    @Override
    public  CompletableFuture<Enum2> func2Async(Enum2 param2) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func2(param2); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Enum2> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public Enum3 func3(Enum3 param3) {
        Log.i(TAG, "request method func3 called, will call native");
        return nativeFunc3(param3);
    }

    @Override
    public  CompletableFuture<Enum3> func3Async(Enum3 param3) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func3(param3); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Enum3> f = new CompletableFuture<>();
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
    private native void nativeSetProp0(Enum0 prop0);
    private native Enum0 nativeGetProp0();
  
    private native void nativeSetProp1(Enum1 prop1);
    private native Enum1 nativeGetProp1();
  
    private native void nativeSetProp2(Enum2 prop2);
    private native Enum2 nativeGetProp2();
  
    private native void nativeSetProp3(Enum3 prop3);
    private native Enum3 nativeGetProp3();
  
    private native Enum0 nativeFunc0(Enum0 param0);
    private native Enum1 nativeFunc1(Enum1 param1);
    private native Enum2 nativeFunc2(Enum2 param2);
    private native Enum3 nativeFunc3(Enum3 param3);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    // Callbacks from native — receive arrays, convert to List and fire events
    public void onProp0Changed(Enum0 newValue)
    {
         Log.i(TAG, "onProp0Changed, will pass notification to all listeners");
         fireProp0Changed(newValue);
    }
    public void onProp1Changed(Enum1 newValue)
    {
         Log.i(TAG, "onProp1Changed, will pass notification to all listeners");
         fireProp1Changed(newValue);
    }
    public void onProp2Changed(Enum2 newValue)
    {
         Log.i(TAG, "onProp2Changed, will pass notification to all listeners");
         fireProp2Changed(newValue);
    }
    public void onProp3Changed(Enum3 newValue)
    {
         Log.i(TAG, "onProp3Changed, will pass notification to all listeners");
         fireProp3Changed(newValue);
    }
    public void onSig0(Enum0 param0)
    {
        Log.i(TAG, "onSig0, will pass notification to all listeners");
        fireSig0(param0);
    }
    public void onSig1(Enum1 param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }
    public void onSig2(Enum2 param2)
    {
        Log.i(TAG, "onSig2, will pass notification to all listeners");
        fireSig2(param2);
    }
    public void onSig3(Enum3 param3)
    {
        Log.i(TAG, "onSig3, will pass notification to all listeners");
        fireSig3(param3);
    }

}
