package tbSimple.tbSimplejniservice;

import android.util.Log;

import tbSimple.tbSimple_api.IVoidInterface;
import tbSimple.tbSimple_api.AbstractVoidInterface;
import tbSimple.tbSimple_api.IVoidInterfaceEventListener;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class VoidInterfaceJniService extends AbstractVoidInterface {


    private final static String TAG = "VoidInterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingFutures
        = new ConcurrentHashMap<>();

    public VoidInterfaceJniService()
    {
        fire_readyStatusChanged(true);
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
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    // methods (async, returns false if native service unavailable)
    private native boolean nativeFuncVoidAsync(String callId);

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

    //In theory event listener interface
    public void onSigVoid()
    {
        Log.i(TAG, "onSigVoid, will pass notification to all listeners");
        fireSigVoid();
    }

}
