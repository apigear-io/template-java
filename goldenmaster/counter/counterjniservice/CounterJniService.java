package counter.counterjniservice;

import android.os.Messenger;
import android.util.Log;

import counter.counter_api.ICounter;
import counter.counter_api.AbstractCounter;
import counter.counter_api.ICounterEventListener;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class CounterJniService extends AbstractCounter {


    private final static String TAG = "CounterJniService";
    private static volatile boolean isServiceReady = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

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
        Log.i(TAG, "request method increment called, will call native");
        return nativeIncrement(vec);
    }

    @Override
    public  CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> incrementAsync(org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return increment(vec); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] incrementArray(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec) {
        Log.i(TAG, "request method incrementArray called, will call native");
        return nativeIncrementArray(vec);
    }

    @Override
    public  CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]> incrementArrayAsync(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return incrementArray(vec); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public customTypes.customTypes_api.Vector3D decrement(customTypes.customTypes_api.Vector3D vec) {
        Log.i(TAG, "request method decrement called, will call native");
        return nativeDecrement(vec);
    }

    @Override
    public  CompletableFuture<customTypes.customTypes_api.Vector3D> decrementAsync(customTypes.customTypes_api.Vector3D vec) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return decrement(vec); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<customTypes.customTypes_api.Vector3D> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public customTypes.customTypes_api.Vector3D[] decrementArray(customTypes.customTypes_api.Vector3D[] vec) {
        Log.i(TAG, "request method decrementArray called, will call native");
        return nativeDecrementArray(vec);
    }

    @Override
    public  CompletableFuture<customTypes.customTypes_api.Vector3D[]> decrementArrayAsync(customTypes.customTypes_api.Vector3D[] vec) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return decrementArray(vec); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<customTypes.customTypes_api.Vector3D[]> f = new CompletableFuture<>();
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
    private native void nativeSetVector(customTypes.customTypes_api.Vector3D vector);
    private native customTypes.customTypes_api.Vector3D nativeGetVector();
  
    private native void nativeSetExternVector(org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector);
    private native org.apache.commons.math3.geometry.euclidean.threed.Vector3D nativeGetExternVector();
  
    private native void nativeSetVectorArray(customTypes.customTypes_api.Vector3D[] vectorArray);
    private native customTypes.customTypes_api.Vector3D[] nativeGetVectorArray();
  
    private native void nativeSetExternVectorArray(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray);
    private native org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] nativeGetExternVectorArray();
  
    // methods
    private native org.apache.commons.math3.geometry.euclidean.threed.Vector3D nativeIncrement(org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec);
    private native org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] nativeIncrementArray(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec);
    private native customTypes.customTypes_api.Vector3D nativeDecrement(customTypes.customTypes_api.Vector3D vec);
    private native customTypes.customTypes_api.Vector3D[] nativeDecrementArray(customTypes.customTypes_api.Vector3D[] vec);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
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
