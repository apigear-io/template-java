package counter.counter_impl;

import android.os.Messenger;
import android.util.Log;

import counter.counter_api.ICounter;
import counter.counter_api.AbstractCounter;
import counter.counter_api.ICounterEventListener;


import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.Arrays;


public class CounterService extends AbstractCounter {

    private final static String TAG = "CounterService";
    private static boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);
    private customTypes.customTypes_api.Vector3D m_vector = new customTypes.customTypes_api.Vector3D();
    private org.apache.commons.math3.geometry.euclidean.threed.Vector3D m_extern_vector = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
    private customTypes.customTypes_api.Vector3D[] m_vectorArray = new customTypes.customTypes_api.Vector3D[]{};
    private org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] m_extern_vectorArray = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]{};

    public CounterService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setVector(customTypes.customTypes_api.Vector3D vector)
    {
        Log.i(TAG, "request setVector called ");
        if ( (m_vector != null && ! m_vector.equals(vector))
        || (m_vector == null && vector != null ))
        {
            m_vector = vector;
            onVectorChanged(m_vector);
        }

    }

    @Override
    public customTypes.customTypes_api.Vector3D getVector()
    {
        Log.i(TAG, "request getVector called,");
        return m_vector;
    }

  
    @Override
    public void setExternVector(org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector)
    {
        Log.i(TAG, "request setExternVector called ");
        if ( (m_extern_vector != null && ! m_extern_vector.equals(extern_vector))
        || (m_extern_vector == null && extern_vector != null ))
        {
            m_extern_vector = extern_vector;
            onExternVectorChanged(m_extern_vector);
        }

    }

    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D getExternVector()
    {
        Log.i(TAG, "request getExternVector called,");
        return m_extern_vector;
    }

  
    @Override
    public void setVectorArray(customTypes.customTypes_api.Vector3D[] vectorArray)
    {
        Log.i(TAG, "request setVectorArray called ");
        if (! Arrays.equals(m_vectorArray, vectorArray))
        {
            m_vectorArray = vectorArray;
            onVectorArrayChanged(m_vectorArray);
        }

    }

    @Override
    public customTypes.customTypes_api.Vector3D[] getVectorArray()
    {
        Log.i(TAG, "request getVectorArray called,");
        return m_vectorArray;
    }

  
    @Override
    public void setExternVectorArray(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray)
    {
        Log.i(TAG, "request setExternVectorArray called ");
        if (! Arrays.equals(m_extern_vectorArray, extern_vectorArray))
        {
            m_extern_vectorArray = extern_vectorArray;
            onExternVectorArrayChanged(m_extern_vectorArray);
        }

    }

    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] getExternVectorArray()
    {
        Log.i(TAG, "request getExternVectorArray called,");
        return m_extern_vectorArray;
    }

  
    // methods

    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D increment(org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec) {
        Log.w(TAG, "request method increment called, returnig default");
        return new org.apache.commons.math3.geometry.euclidean.threed.Vector3D(0.0, 0.0, 0.0);
    }

    @Override
    public  CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> incrementAsync(org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec) {
        return CompletableFuture.supplyAsync(
                () -> {return increment(vec); },
                executor);
    }

    @Override
    public org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] incrementArray(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec) {
        Log.w(TAG, "request method incrementArray called, returnig default");
        return new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]{};
    }

    @Override
    public  CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]> incrementArrayAsync(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec) {
        return CompletableFuture.supplyAsync(
                () -> {return incrementArray(vec); },
                executor);
    }

    @Override
    public customTypes.customTypes_api.Vector3D decrement(customTypes.customTypes_api.Vector3D vec) {
        Log.w(TAG, "request method decrement called, returnig default");
        return new customTypes.customTypes_api.Vector3D();
    }

    @Override
    public  CompletableFuture<customTypes.customTypes_api.Vector3D> decrementAsync(customTypes.customTypes_api.Vector3D vec) {
        return CompletableFuture.supplyAsync(
                () -> {return decrement(vec); },
                executor);
    }

    @Override
    public customTypes.customTypes_api.Vector3D[] decrementArray(customTypes.customTypes_api.Vector3D[] vec) {
        Log.w(TAG, "request method decrementArray called, returnig default");
        return new customTypes.customTypes_api.Vector3D[]{};
    }

    @Override
    public  CompletableFuture<customTypes.customTypes_api.Vector3D[]> decrementArrayAsync(customTypes.customTypes_api.Vector3D[] vec) {
        return CompletableFuture.supplyAsync(
                () -> {return decrementArray(vec); },
                executor);
    }    

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    //In theory event listener interface
    private void onVectorChanged(customTypes.customTypes_api.Vector3D newValue)
    {
         Log.i(TAG, "onVectorChanged, will pass notification to all listeners");
         fireVectorChanged(newValue);
    }
    private void onExternVectorChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D newValue)
    {
         Log.i(TAG, "onExternVectorChanged, will pass notification to all listeners");
         fireExternVectorChanged(newValue);
    }
    private void onVectorArrayChanged(customTypes.customTypes_api.Vector3D[] newValue)
    {
         Log.i(TAG, "onVectorArrayChanged, will pass notification to all listeners");
         fireVectorArrayChanged(newValue);
    }
    private void onExternVectorArrayChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] newValue)
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