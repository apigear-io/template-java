package testbed2.testbed2jniservice;

import android.util.Log;

import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_api.AbstractNestedStruct1Interface;
import testbed2.testbed2_api.INestedStruct1InterfaceEventListener;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_android_messenger.NestedStruct1Parcelable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class NestedStruct1InterfaceJniService extends AbstractNestedStruct1Interface {


    private final static String TAG = "NestedStruct1InterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingFutures
        = new ConcurrentHashMap<>();

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
        Log.i(TAG, "request method funcNoReturnValue called");
        try {
            funcNoReturnValueAsync(param1).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcNoReturnValue sync call timed out");
        } catch (Exception e) {
            Log.w(TAG, "funcNoReturnValue sync call failed: " + e.getMessage());
        }
    }

    @Override
    public CompletableFuture<Void> funcNoReturnValueAsync(NestedStruct1 param1) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Void> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFuncNoReturnValueAsync(callId, param1);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for funcNoReturnValue"));
        }
        return future;
    }

    @Override
    public NestedStruct1 funcNoParams() {
        Log.i(TAG, "request method funcNoParams called");
        try {
            return funcNoParamsAsync().get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcNoParams sync call timed out");
            return new NestedStruct1();
        } catch (Exception e) {
            Log.w(TAG, "funcNoParams sync call failed: " + e.getMessage());
            return new NestedStruct1();
        }
    }

    @Override
    public CompletableFuture<NestedStruct1> funcNoParamsAsync() {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFuncNoParamsAsync(callId);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for funcNoParams"));
        }
        return future;
    }

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
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    private native void nativeSetProp1(NestedStruct1 prop1);
    private native NestedStruct1 nativeGetProp1();
  
    // methods (async, returns false if native service unavailable)
    private native boolean nativeFuncNoReturnValueAsync(String callId, NestedStruct1 param1);
    private native boolean nativeFuncNoParamsAsync(String callId);
    private native boolean nativeFunc1Async(String callId, NestedStruct1 param1);

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
    public void onFuncNoReturnValueResult(String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Void> typedFuture = (CompletableFuture<Void>) future;
            typedFuture.complete(null);
        }
    }
    public void onFuncNoParamsResult(NestedStruct1 result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFunc1Result(NestedStruct1 result, String callId) {
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
    public void onSig1(NestedStruct1 param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }

}
