package testbed2.testbed2jniservice;

import android.util.Log;

import testbed2.testbed2_api.INestedStruct2Interface;
import testbed2.testbed2_api.AbstractNestedStruct2Interface;
import testbed2.testbed2_api.INestedStruct2InterfaceEventListener;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_android_messenger.NestedStruct1Parcelable;
import testbed2.testbed2_api.NestedStruct2;
import testbed2.testbed2_android_messenger.NestedStruct2Parcelable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class NestedStruct2InterfaceJniService extends AbstractNestedStruct2Interface {


    private final static String TAG = "NestedStruct2InterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingFutures
        = new ConcurrentHashMap<>();

    public NestedStruct2InterfaceJniService()
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

  
    @Override
    public void setProp2(NestedStruct2 prop2)
    {
        Log.i(TAG, "request setProp2 called, will call native ");
        nativeSetProp2(prop2);
    }

    @Override
    public NestedStruct2 getProp2()
    {
        Log.i(TAG, "request getProp2 called, will call native ");
        return nativeGetProp2();
    }

  
    // methods

    @Override
    public NestedStruct1 func1(NestedStruct1 param1) {
        Log.i(TAG, "request method func1 called");
        try {
            return func1Async(param1).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "func1 sync call timed out");
            return new NestedStruct1();
        } catch (Exception e) {
            Log.w(TAG, "func1 sync call failed: " + e.getMessage());
            return new NestedStruct1();
        }
    }

    @Override
    public CompletableFuture<NestedStruct1> func1Async(NestedStruct1 param1) {
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
    public NestedStruct1 func2(NestedStruct1 param1, NestedStruct2 param2) {
        Log.i(TAG, "request method func2 called");
        try {
            return func2Async(param1, param2).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "func2 sync call timed out");
            return new NestedStruct1();
        } catch (Exception e) {
            Log.w(TAG, "func2 sync call failed: " + e.getMessage());
            return new NestedStruct1();
        }
    }

    @Override
    public CompletableFuture<NestedStruct1> func2Async(NestedStruct1 param1, NestedStruct2 param2) {
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
    private native void nativeSetProp1(NestedStruct1 prop1);
    private native NestedStruct1 nativeGetProp1();
  
    private native void nativeSetProp2(NestedStruct2 prop2);
    private native NestedStruct2 nativeGetProp2();
  
    // methods (async, returns false if native service unavailable)
    private native boolean nativeFunc1Async(String callId, NestedStruct1 param1);
    private native boolean nativeFunc2Async(String callId, NestedStruct1 param1, NestedStruct2 param2);

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
    public void onFunc1Result(NestedStruct1 result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFunc2Result(NestedStruct1 result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }

    //In theory event listener interface
    public void onProp1Changed(NestedStruct1 newValue)
    {
         Log.i(TAG, "onProp1Changed, will pass notification to all listeners");
         fireProp1Changed(newValue);
    }
    public void onProp2Changed(NestedStruct2 newValue)
    {
         Log.i(TAG, "onProp2Changed, will pass notification to all listeners");
         fireProp2Changed(newValue);
    }
    public void onSig1(NestedStruct1 param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }
    public void onSig2(NestedStruct1 param1, NestedStruct2 param2)
    {
        Log.i(TAG, "onSig2, will pass notification to all listeners");
        fireSig2(param1, param2);
    }

}
