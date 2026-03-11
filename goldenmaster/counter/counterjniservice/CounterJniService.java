package counter.counterjniservice;

import android.util.Log;

import counter.counter_api.ICounter;
import counter.counter_api.AbstractCounter;
import counter.counter_api.ICounterEventListener;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class CounterJniService extends AbstractCounter {


    private final static String TAG = "CounterJniService";
    private static volatile boolean isServiceReady = false;
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingFutures
        = new ConcurrentHashMap<>();

    public CounterJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setVector(customTypes.customTypes_api.Vector3D vector)
    {
        Log.i(TAG, "request setVector called, will call native ");
        nativeSetVector(vector);
    }

    @Override
    public customTypes.customTypes_api.Vector3D getVector()
    {
        Log.i(TAG, "request getVector called, will call native ");
        return nativeGetVector();
    }

  
    @Override
    public void setExternVector(org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector)
    {
        Log.i(TAG, "request setExternVector called, will call native ");
        nativeSetExternVector(extern_vector);
    }

    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D getExternVector()
    {
        Log.i(TAG, "request getExternVector called, will call native ");
        return nativeGetExternVector();
    }

  
    @Override
    public void setVectorArray(customTypes.customTypes_api.Vector3D[] vectorArray)
    {
        Log.i(TAG, "request setVectorArray called, will call native ");
        nativeSetVectorArray(vectorArray);
    }

    @Override
    public customTypes.customTypes_api.Vector3D[] getVectorArray()
    {
        Log.i(TAG, "request getVectorArray called, will call native ");
        return nativeGetVectorArray();
    }

  
    @Override
    public void setExternVectorArray(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray)
    {
        Log.i(TAG, "request setExternVectorArray called, will call native ");
        nativeSetExternVectorArray(extern_vectorArray);
    }

    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] getExternVectorArray()
    {
        Log.i(TAG, "request getExternVectorArray called, will call native ");
        return nativeGetExternVectorArray();
    }

  
    // methods

    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D increment(org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec) {
        Log.i(TAG, "request method increment called");
        try {
            return incrementAsync(vec).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "increment sync call timed out");
            return new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
        } catch (Exception e) {
            Log.w(TAG, "increment sync call failed: " + e.getMessage());
            return new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
        }
    }

    @Override
    public CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> incrementAsync(org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeIncrementAsync(callId, vec);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for increment"));
        }
        return future;
    }

    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] incrementArray(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec) {
        Log.i(TAG, "request method incrementArray called");
        try {
            return incrementArrayAsync(vec).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "incrementArray sync call timed out");
            return new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]{};
        } catch (Exception e) {
            Log.w(TAG, "incrementArray sync call failed: " + e.getMessage());
            return new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]{};
        }
    }

    @Override
    public CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]> incrementArrayAsync(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeIncrementArrayAsync(callId, vec);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for incrementArray"));
        }
        return future;
    }

    @Override
    public customTypes.customTypes_api.Vector3D decrement(customTypes.customTypes_api.Vector3D vec) {
        Log.i(TAG, "request method decrement called");
        try {
            return decrementAsync(vec).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "decrement sync call timed out");
            return new customTypes.customTypes_api.Vector3D();
        } catch (Exception e) {
            Log.w(TAG, "decrement sync call failed: " + e.getMessage());
            return new customTypes.customTypes_api.Vector3D();
        }
    }

    @Override
    public CompletableFuture<customTypes.customTypes_api.Vector3D> decrementAsync(customTypes.customTypes_api.Vector3D vec) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeDecrementAsync(callId, vec);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for decrement"));
        }
        return future;
    }

    @Override
    public customTypes.customTypes_api.Vector3D[] decrementArray(customTypes.customTypes_api.Vector3D[] vec) {
        Log.i(TAG, "request method decrementArray called");
        try {
            return decrementArrayAsync(vec).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "decrementArray sync call timed out");
            return new customTypes.customTypes_api.Vector3D[]{};
        } catch (Exception e) {
            Log.w(TAG, "decrementArray sync call failed: " + e.getMessage());
            return new customTypes.customTypes_api.Vector3D[]{};
        }
    }

    @Override
    public CompletableFuture<customTypes.customTypes_api.Vector3D[]> decrementArrayAsync(customTypes.customTypes_api.Vector3D[] vec) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeDecrementArrayAsync(callId, vec);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for decrementArray"));
        }
        return future;
    }

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    private native void nativeSetVector(customTypes.customTypes_api.Vector3D vector);
    private native customTypes.customTypes_api.Vector3D nativeGetVector();
  
    private native void nativeSetExternVector(org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector);
    private native org.apache.commons.math3.geometry.euclidean.threed.Vector3D nativeGetExternVector();
  
    private native void nativeSetVectorArray(customTypes.customTypes_api.Vector3D[] vectorArray);
    private native customTypes.customTypes_api.Vector3D[] nativeGetVectorArray();
  
    private native void nativeSetExternVectorArray(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray);
    private native org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] nativeGetExternVectorArray();
  
    // methods (async, returns false if native service unavailable)
    private native boolean nativeIncrementAsync(String callId, org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec);
    private native boolean nativeIncrementArrayAsync(String callId, org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec);
    private native boolean nativeDecrementAsync(String callId, customTypes.customTypes_api.Vector3D vec);
    private native boolean nativeDecrementArrayAsync(String callId, customTypes.customTypes_api.Vector3D[] vec);

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
    public void onIncrementResult(org.apache.commons.math3.geometry.euclidean.threed.Vector3D result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onIncrementArrayResult(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onDecrementResult(customTypes.customTypes_api.Vector3D result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onDecrementArrayResult(customTypes.customTypes_api.Vector3D[] result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }

    //In theory event listener interface
    public void onVectorChanged(customTypes.customTypes_api.Vector3D newValue)
    {
         Log.i(TAG, "onVectorChanged, will pass notification to all listeners");
         fireVectorChanged(newValue);
    }
    public void onExternVectorChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D newValue)
    {
         Log.i(TAG, "onExternVectorChanged, will pass notification to all listeners");
         fireExternVectorChanged(newValue);
    }
    public void onVectorArrayChanged(customTypes.customTypes_api.Vector3D[] newValue)
    {
         Log.i(TAG, "onVectorArrayChanged, will pass notification to all listeners");
         fireVectorArrayChanged(newValue);
    }
    public void onExternVectorArrayChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] newValue)
    {
         Log.i(TAG, "onExternVectorArrayChanged, will pass notification to all listeners");
         fireExternVectorArrayChanged(newValue);
    }
    public void onValueChanged(customTypes.customTypes_api.Vector3D vector, org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector, customTypes.customTypes_api.Vector3D[] vectorArray, org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray)
    {
        Log.i(TAG, "onValueChanged, will pass notification to all listeners");
        fireValueChanged(vector, extern_vector, vectorArray, extern_vectorArray);
    }

}
