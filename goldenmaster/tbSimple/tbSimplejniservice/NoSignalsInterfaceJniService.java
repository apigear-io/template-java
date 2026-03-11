package tbSimple.tbSimplejniservice;

import android.util.Log;

import tbSimple.tbSimple_api.INoSignalsInterface;
import tbSimple.tbSimple_api.AbstractNoSignalsInterface;
import tbSimple.tbSimple_api.INoSignalsInterfaceEventListener;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class NoSignalsInterfaceJniService extends AbstractNoSignalsInterface {


    private final static String TAG = "NoSignalsInterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingFutures
        = new ConcurrentHashMap<>();

    public NoSignalsInterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setPropBool(boolean propBool)
    {
        Log.i(TAG, "request setPropBool called, will call native ");
        nativeSetPropBool(propBool);
    }

    @Override
    public boolean getPropBool()
    {
        Log.i(TAG, "request getPropBool called, will call native ");
        return nativeGetPropBool();
    }

  
    @Override
    public void setPropInt(int propInt)
    {
        Log.i(TAG, "request setPropInt called, will call native ");
        nativeSetPropInt(propInt);
    }

    @Override
    public int getPropInt()
    {
        Log.i(TAG, "request getPropInt called, will call native ");
        return nativeGetPropInt();
    }

  
    // methods

    @Override
    public void funcVoid() {
        Log.i(TAG, "request method funcVoid called");
        try {
            funcVoidAsync().get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcVoid sync call timed out");
        } catch (Exception e) {
            Log.w(TAG, "funcVoid sync call failed: " + e.getMessage());
        }
    }

    @Override
    public CompletableFuture<Void> funcVoidAsync() {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Void> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFuncVoidAsync(callId);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for funcVoid"));
        }
        return future;
    }

    @Override
    public boolean funcBool(boolean paramBool) {
        Log.i(TAG, "request method funcBool called");
        try {
            return funcBoolAsync(paramBool).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcBool sync call timed out");
            return false;
        } catch (Exception e) {
            Log.w(TAG, "funcBool sync call failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public CompletableFuture<Boolean> funcBoolAsync(boolean paramBool) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFuncBoolAsync(callId, paramBool);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for funcBool"));
        }
        return future;
    }

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    private native void nativeSetPropBool(boolean propBool);
    private native boolean nativeGetPropBool();
  
    private native void nativeSetPropInt(int propInt);
    private native int nativeGetPropInt();
  
    // methods (async, returns false if native service unavailable)
    private native boolean nativeFuncVoidAsync(String callId);
    private native boolean nativeFuncBoolAsync(String callId, boolean paramBool);

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
    public void onFuncVoidResult(String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Void> typedFuture = (CompletableFuture<Void>) future;
            typedFuture.complete(null);
        }
    }
    public void onFuncBoolResult(boolean result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }

    //In theory event listener interface
    public void onPropBoolChanged(boolean newValue)
    {
         Log.i(TAG, "onPropBoolChanged, will pass notification to all listeners");
         firePropBoolChanged(newValue);
    }
    public void onPropIntChanged(int newValue)
    {
         Log.i(TAG, "onPropIntChanged, will pass notification to all listeners");
         firePropIntChanged(newValue);
    }

}
