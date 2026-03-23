package testbed2.testbed2jniservice;

import android.os.Messenger;
import android.util.Log;

import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_api.AbstractManyParamInterface;
import testbed2.testbed2_api.IManyParamInterfaceEventListener;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class ManyParamInterfaceJniService extends AbstractManyParamInterface {


    private final static String TAG = "ManyParamInterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ManyParamInterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setProp1(int prop1)
    {
        Log.i(TAG, "request setProp1 called, will call native ");
        nativeSetProp1(prop1);
    }

    @Override
    public int getProp1()
    {
        Log.i(TAG, "request getProp1 called, will call native ");
        return nativeGetProp1();
    }

  
    @Override
    public void setProp2(int prop2)
    {
        Log.i(TAG, "request setProp2 called, will call native ");
        nativeSetProp2(prop2);
    }

    @Override
    public int getProp2()
    {
        Log.i(TAG, "request getProp2 called, will call native ");
        return nativeGetProp2();
    }

  
    @Override
    public void setProp3(int prop3)
    {
        Log.i(TAG, "request setProp3 called, will call native ");
        nativeSetProp3(prop3);
    }

    @Override
    public int getProp3()
    {
        Log.i(TAG, "request getProp3 called, will call native ");
        return nativeGetProp3();
    }

  
    @Override
    public void setProp4(int prop4)
    {
        Log.i(TAG, "request setProp4 called, will call native ");
        nativeSetProp4(prop4);
    }

    @Override
    public int getProp4()
    {
        Log.i(TAG, "request getProp4 called, will call native ");
        return nativeGetProp4();
    }

  
    // methods

    @Override
    public int func1(int param1) {
        Log.i(TAG, "request method func1 called, will call native");
        return nativeFunc1(param1);
    }

    @Override
    public  CompletableFuture<Integer> func1Async(int param1) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func1(param1); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Integer> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public int func2(int param1, int param2) {
        Log.i(TAG, "request method func2 called, will call native");
        return nativeFunc2(param1, param2);
    }

    @Override
    public  CompletableFuture<Integer> func2Async(int param1, int param2) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func2(param1, param2); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Integer> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public int func3(int param1, int param2, int param3) {
        Log.i(TAG, "request method func3 called, will call native");
        return nativeFunc3(param1, param2, param3);
    }

    @Override
    public  CompletableFuture<Integer> func3Async(int param1, int param2, int param3) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func3(param1, param2, param3); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Integer> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public int func4(int param1, int param2, int param3, int param4) {
        Log.i(TAG, "request method func4 called, will call native");
        return nativeFunc4(param1, param2, param3, param4);
    }

    @Override
    public  CompletableFuture<Integer> func4Async(int param1, int param2, int param3, int param4) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func4(param1, param2, param3, param4); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Integer> f = new CompletableFuture<>();
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
    private native void nativeSetProp1(int prop1);
    private native int nativeGetProp1();
  
    private native void nativeSetProp2(int prop2);
    private native int nativeGetProp2();
  
    private native void nativeSetProp3(int prop3);
    private native int nativeGetProp3();
  
    private native void nativeSetProp4(int prop4);
    private native int nativeGetProp4();
  
    // methods
    private native int nativeFunc1(int param1);
    private native int nativeFunc2(int param1, int param2);
    private native int nativeFunc3(int param1, int param2, int param3);
    private native int nativeFunc4(int param1, int param2, int param3, int param4);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    //In theory event listener interface
    public void onProp1Changed(int newValue)
    {
         Log.i(TAG, "onProp1Changed, will pass notification to all listeners");
         fireProp1Changed(newValue);
    }
    public void onProp2Changed(int newValue)
    {
         Log.i(TAG, "onProp2Changed, will pass notification to all listeners");
         fireProp2Changed(newValue);
    }
    public void onProp3Changed(int newValue)
    {
         Log.i(TAG, "onProp3Changed, will pass notification to all listeners");
         fireProp3Changed(newValue);
    }
    public void onProp4Changed(int newValue)
    {
         Log.i(TAG, "onProp4Changed, will pass notification to all listeners");
         fireProp4Changed(newValue);
    }
    public void onSig1(int param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }
    public void onSig2(int param1, int param2)
    {
        Log.i(TAG, "onSig2, will pass notification to all listeners");
        fireSig2(param1, param2);
    }
    public void onSig3(int param1, int param2, int param3)
    {
        Log.i(TAG, "onSig3, will pass notification to all listeners");
        fireSig3(param1, param2, param3);
    }
    public void onSig4(int param1, int param2, int param3, int param4)
    {
        Log.i(TAG, "onSig4, will pass notification to all listeners");
        fireSig4(param1, param2, param3, param4);
    }

}
