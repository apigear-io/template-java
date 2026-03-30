package tbSame2.tbSame2jniservice;

import android.util.Log;

import tbSame2.tbSame2_api.ISameEnum2Interface;
import tbSame2.tbSame2_api.AbstractSameEnum2Interface;
import tbSame2.tbSame2_api.ISameEnum2InterfaceEventListener;
import tbSame2.tbSame2_android_messenger.Conversions;
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_android_messenger.Enum1Parcelable;
import tbSame2.tbSame2_api.Enum2;
import tbSame2.tbSame2_android_messenger.Enum2Parcelable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;


public class SameEnum2InterfaceJniService extends AbstractSameEnum2Interface {


    private final static String TAG = "SameEnum2InterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SameEnum2InterfaceJniService()
    {
        fire_readyStatusChanged(true);
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

  
    // methods

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
    public Enum1 func2(Enum1 param1, Enum2 param2) {
        Log.i(TAG, "request method func2 called, will call native");
        return nativeFunc2(param1, param2);
    }

    @Override
    public  CompletableFuture<Enum1> func2Async(Enum1 param1, Enum2 param2) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func2(param1, param2); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Enum1> f = new CompletableFuture<>();
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
    private native void nativeSetProp1(Enum1 prop1);
    private native Enum1 nativeGetProp1();
  
    private native void nativeSetProp2(Enum2 prop2);
    private native Enum2 nativeGetProp2();
  
    private native Enum1 nativeFunc1(Enum1 param1);
    private native Enum1 nativeFunc2(Enum1 param1, Enum2 param2);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    // Callbacks from native — receive arrays, convert to List and fire events
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
    public void onSig1(Enum1 param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }
    public void onSig2(Enum1 param1, Enum2 param2)
    {
        Log.i(TAG, "onSig2, will pass notification to all listeners");
        fireSig2(param1, param2);
    }

}
