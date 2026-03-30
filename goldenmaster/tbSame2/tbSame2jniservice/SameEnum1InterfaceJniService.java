package tbSame2.tbSame2jniservice;

import android.util.Log;

import tbSame2.tbSame2_api.ISameEnum1Interface;
import tbSame2.tbSame2_api.AbstractSameEnum1Interface;
import tbSame2.tbSame2_api.ISameEnum1InterfaceEventListener;
import tbSame2.tbSame2_android_messenger.Conversions;
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_android_messenger.Enum1Parcelable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;


public class SameEnum1InterfaceJniService extends AbstractSameEnum1Interface {


    private final static String TAG = "SameEnum1InterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SameEnum1InterfaceJniService()
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
  
    private native Enum1 nativeFunc1(Enum1 param1);

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
    public void onSig1(Enum1 param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }

}
