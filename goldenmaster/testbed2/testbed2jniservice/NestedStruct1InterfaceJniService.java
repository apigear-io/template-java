package testbed2.testbed2jniservice;

import android.os.Messenger;
import android.util.Log;

import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_api.AbstractNestedStruct1Interface;
import testbed2.testbed2_api.INestedStruct1InterfaceEventListener;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_android_messenger.NestedStruct1Parcelable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class NestedStruct1InterfaceJniService extends AbstractNestedStruct1Interface {


    private final static String TAG = "NestedStruct1InterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public NestedStruct1InterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setProp1(NestedStruct1 prop1)
    {
        Log.i(TAG, "request setProp1 called, will call native ");
        nativeSetProp1(prop1);
    }

    @Override
    public NestedStruct1 getProp1()
    {
        Log.i(TAG, "request getProp1 called, will call native ");
        return nativeGetProp1();
    }

  
    // methods

    @Override
    public void funcNoReturnValue(NestedStruct1 param1) {
        Log.i(TAG, "request method funcNoReturnValue called, will call native");
         nativeFuncNoReturnValue(param1);
    }

    @Override
    public  CompletableFuture<Void> funcNoReturnValueAsync(NestedStruct1 param1) {
        try {
            return CompletableFuture.runAsync(
                    () -> { funcNoReturnValue(param1); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Void> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public NestedStruct1 funcNoParams() {
        Log.i(TAG, "request method funcNoParams called, will call native");
        return nativeFuncNoParams();
    }

    @Override
    public  CompletableFuture<NestedStruct1> funcNoParamsAsync() {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcNoParams(); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<NestedStruct1> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public NestedStruct1 func1(NestedStruct1 param1) {
        Log.i(TAG, "request method func1 called, will call native");
        return nativeFunc1(param1);
    }

    @Override
    public  CompletableFuture<NestedStruct1> func1Async(NestedStruct1 param1) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func1(param1); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<NestedStruct1> f = new CompletableFuture<>();
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

    // Called on Native Impl Service
    private native void nativeSetProp1(NestedStruct1 prop1);
    private native NestedStruct1 nativeGetProp1();
  
    // methods
    private native void nativeFuncNoReturnValue(NestedStruct1 param1);
    private native NestedStruct1 nativeFuncNoParams();
    private native NestedStruct1 nativeFunc1(NestedStruct1 param1);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    //In theory event listener interface
    public void onProp1Changed(NestedStruct1 newValue)
    {
         Log.i(TAG, "onProp1Changed, will pass notification to all listeners");
         fireProp1Changed(newValue);
    }
    public void onSig1(NestedStruct1 param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }

}
