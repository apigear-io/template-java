package tbRefIfaces.tbRefIfacesjniservice;

import android.util.Log;

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_api.AbstractSimpleLocalIf;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIfEventListener;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class SimpleLocalIfJniService extends AbstractSimpleLocalIf {


    private final static String TAG = "SimpleLocalIfJniService";
    private static volatile boolean isServiceReady = false;
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingFutures
        = new ConcurrentHashMap<>();

    public SimpleLocalIfJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setIntProperty(int intProperty)
    {
        Log.i(TAG, "request setIntProperty called, will call native ");
        nativeSetIntProperty(intProperty);
    }

    @Override
    public int getIntProperty()
    {
        Log.i(TAG, "request getIntProperty called, will call native ");
        return nativeGetIntProperty();
    }

  
    // methods

    @Override
    public int intMethod(int param) {
        Log.i(TAG, "request method intMethod called");
        try {
            return intMethodAsync(param).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "intMethod sync call timed out");
            return 0;
        } catch (Exception e) {
            Log.w(TAG, "intMethod sync call failed: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public CompletableFuture<Integer> intMethodAsync(int param) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeIntMethodAsync(callId, param);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for intMethod"));
        }
        return future;
    }

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    private native void nativeSetIntProperty(int intProperty);
    private native int nativeGetIntProperty();
  
    // methods (async, returns false if native service unavailable)
    private native boolean nativeIntMethodAsync(String callId, int param);

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
    public void onIntMethodResult(int result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }

    //In theory event listener interface
    public void onIntPropertyChanged(int newValue)
    {
         Log.i(TAG, "onIntPropertyChanged, will pass notification to all listeners");
         fireIntPropertyChanged(newValue);
    }
    public void onIntSignal(int param)
    {
        Log.i(TAG, "onIntSignal, will pass notification to all listeners");
        fireIntSignal(param);
    }

}
