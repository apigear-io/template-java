package tbSame1.tbSame1jniservice;

import android.util.Log;

import tbSame1.tbSame1_api.ISameEnum1Interface;
import tbSame1.tbSame1_api.AbstractSameEnum1Interface;
import tbSame1.tbSame1_api.ISameEnum1InterfaceEventListener;
import tbSame1.tbSame1_api.Enum1;
import tbSame1.tbSame1_android_messenger.Enum1Parcelable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class SameEnum1InterfaceJniService extends AbstractSameEnum1Interface {


    private final static String TAG = "SameEnum1InterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingFutures
        = new ConcurrentHashMap<>();

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
        Log.i(TAG, "request method func1 called");
        try {
            return func1Async(param1).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "func1 sync call timed out");
            return Enum1.Value1;
        } catch (Exception e) {
            Log.w(TAG, "func1 sync call failed: " + e.getMessage());
            return Enum1.Value1;
        }
    }

    @Override
    public CompletableFuture<Enum1> func1Async(Enum1 param1) {
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
    private native void nativeSetProp1(Enum1 prop1);
    private native Enum1 nativeGetProp1();
  
    // methods (async, returns false if native service unavailable)
    private native boolean nativeFunc1Async(String callId, Enum1 param1);

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
    public void onFunc1Result(Enum1 result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }

    //In theory event listener interface
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
