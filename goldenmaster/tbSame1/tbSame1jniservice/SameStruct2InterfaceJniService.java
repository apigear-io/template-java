package tbSame1.tbSame1jniservice;

import android.util.Log;

import tbSame1.tbSame1_api.ISameStruct2Interface;
import tbSame1.tbSame1_api.AbstractSameStruct2Interface;
import tbSame1.tbSame1_api.ISameStruct2InterfaceEventListener;
import tbSame1.tbSame1_api.Struct1;
import tbSame1.tbSame1_android_messenger.Struct1Parcelable;
import tbSame1.tbSame1_api.Struct2;
import tbSame1.tbSame1_android_messenger.Struct2Parcelable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class SameStruct2InterfaceJniService extends AbstractSameStruct2Interface {


    private final static String TAG = "SameStruct2InterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingFutures
        = new ConcurrentHashMap<>();

    public SameStruct2InterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setProp1(Struct2 prop1)
    {
        Log.i(TAG, "request setProp1 called, will call native ");
        nativeSetProp1(prop1);
    }

    @Override
    public Struct2 getProp1()
    {
        Log.i(TAG, "request getProp1 called, will call native ");
        return nativeGetProp1();
    }

  
    @Override
    public void setProp2(Struct2 prop2)
    {
        Log.i(TAG, "request setProp2 called, will call native ");
        nativeSetProp2(prop2);
    }

    @Override
    public Struct2 getProp2()
    {
        Log.i(TAG, "request getProp2 called, will call native ");
        return nativeGetProp2();
    }

  
    // methods

    @Override
    public Struct1 func1(Struct1 param1) {
        Log.i(TAG, "request method func1 called");
        try {
            return func1Async(param1).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "func1 sync call timed out");
            return new Struct1();
        } catch (Exception e) {
            Log.w(TAG, "func1 sync call failed: " + e.getMessage());
            return new Struct1();
        }
    }

    @Override
    public CompletableFuture<Struct1> func1Async(Struct1 param1) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFunc1Async(callId, param1);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for func1"));
        }
        return future;
    }

    @Override
    public Struct1 func2(Struct1 param1, Struct2 param2) {
        Log.i(TAG, "request method func2 called");
        try {
            return func2Async(param1, param2).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "func2 sync call timed out");
            return new Struct1();
        } catch (Exception e) {
            Log.w(TAG, "func2 sync call failed: " + e.getMessage());
            return new Struct1();
        }
    }

    @Override
    public CompletableFuture<Struct1> func2Async(Struct1 param1, Struct2 param2) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFunc2Async(callId, param1, param2);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for func2"));
        }
        return future;
    }

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    private native void nativeSetProp1(Struct2 prop1);
    private native Struct2 nativeGetProp1();
  
    private native void nativeSetProp2(Struct2 prop2);
    private native Struct2 nativeGetProp2();
  
    // methods (async, returns false if native service unavailable)
    private native boolean nativeFunc1Async(String callId, Struct1 param1);
    private native boolean nativeFunc2Async(String callId, Struct1 param1, Struct2 param2);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
        if (!value) {
            cancelAllPending();
        }
    }

    public void cancelAllPending() {
        for (String callId : pendingFutures.keySet()) {
            CompletableFuture<?> future = pendingFutures.remove(callId);
            if (future != null) {
                future.completeExceptionally(
                    new IllegalStateException("Service disconnected"));
            }
        }
    }

    // Operation result callbacks (called by native C++ continuations)
    public void onFunc1Result(Struct1 result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFunc2Result(Struct1 result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }

    //In theory event listener interface
    public void onProp1Changed(Struct2 newValue)
    {
         Log.i(TAG, "onProp1Changed, will pass notification to all listeners");
         fireProp1Changed(newValue);
    }
    public void onProp2Changed(Struct2 newValue)
    {
         Log.i(TAG, "onProp2Changed, will pass notification to all listeners");
         fireProp2Changed(newValue);
    }
    public void onSig1(Struct1 param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }
    public void onSig2(Struct1 param1, Struct2 param2)
    {
        Log.i(TAG, "onSig2, will pass notification to all listeners");
        fireSig2(param1, param2);
    }

}
