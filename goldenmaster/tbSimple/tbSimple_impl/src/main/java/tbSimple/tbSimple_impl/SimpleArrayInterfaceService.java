package tbSimple.tbSimple_impl;

import android.os.Messenger;
import android.util.Log;

import tbSimple.tbSimple_api.ISimpleArrayInterface;
import tbSimple.tbSimple_api.AbstractSimpleArrayInterface;
import tbSimple.tbSimple_api.ISimpleArrayInterfaceEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class SimpleArrayInterfaceService extends AbstractSimpleArrayInterface {

    private final static String TAG = "SimpleArrayInterfaceService";
    private volatile boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private List<Boolean> m_propBool = new ArrayList<>();
    private List<Integer> m_propInt = new ArrayList<>();
    private List<Integer> m_propInt32 = new ArrayList<>();
    private List<Long> m_propInt64 = new ArrayList<>();
    private List<Float> m_propFloat = new ArrayList<>();
    private List<Float> m_propFloat32 = new ArrayList<>();
    private List<Double> m_propFloat64 = new ArrayList<>();
    private List<String> m_propString = new ArrayList<>();
    private String m_propReadOnlyString = new String();

    public SimpleArrayInterfaceService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setPropBool(List<Boolean> propBool)
    {
        Log.i(TAG, "request setPropBool called ");
        if (!m_propBool.equals(propBool))
        {
            m_propBool = new ArrayList<>(propBool);
            onPropBoolChanged(new ArrayList<>(m_propBool));
        }

    }

    @Override
    public List<Boolean> getPropBool()
    {
        Log.i(TAG, "request getPropBool called,");
        return new ArrayList<>(m_propBool);
    }

  
    @Override
    public void setPropInt(List<Integer> propInt)
    {
        Log.i(TAG, "request setPropInt called ");
        if (!m_propInt.equals(propInt))
        {
            m_propInt = new ArrayList<>(propInt);
            onPropIntChanged(new ArrayList<>(m_propInt));
        }

    }

    @Override
    public List<Integer> getPropInt()
    {
        Log.i(TAG, "request getPropInt called,");
        return new ArrayList<>(m_propInt);
    }

  
    @Override
    public void setPropInt32(List<Integer> propInt32)
    {
        Log.i(TAG, "request setPropInt32 called ");
        if (!m_propInt32.equals(propInt32))
        {
            m_propInt32 = new ArrayList<>(propInt32);
            onPropInt32Changed(new ArrayList<>(m_propInt32));
        }

    }

    @Override
    public List<Integer> getPropInt32()
    {
        Log.i(TAG, "request getPropInt32 called,");
        return new ArrayList<>(m_propInt32);
    }

  
    @Override
    public void setPropInt64(List<Long> propInt64)
    {
        Log.i(TAG, "request setPropInt64 called ");
        if (!m_propInt64.equals(propInt64))
        {
            m_propInt64 = new ArrayList<>(propInt64);
            onPropInt64Changed(new ArrayList<>(m_propInt64));
        }

    }

    @Override
    public List<Long> getPropInt64()
    {
        Log.i(TAG, "request getPropInt64 called,");
        return new ArrayList<>(m_propInt64);
    }

  
    @Override
    public void setPropFloat(List<Float> propFloat)
    {
        Log.i(TAG, "request setPropFloat called ");
        if (!m_propFloat.equals(propFloat))
        {
            m_propFloat = new ArrayList<>(propFloat);
            onPropFloatChanged(new ArrayList<>(m_propFloat));
        }

    }

    @Override
    public List<Float> getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called,");
        return new ArrayList<>(m_propFloat);
    }

  
    @Override
    public void setPropFloat32(List<Float> propFloat32)
    {
        Log.i(TAG, "request setPropFloat32 called ");
        if (!m_propFloat32.equals(propFloat32))
        {
            m_propFloat32 = new ArrayList<>(propFloat32);
            onPropFloat32Changed(new ArrayList<>(m_propFloat32));
        }

    }

    @Override
    public List<Float> getPropFloat32()
    {
        Log.i(TAG, "request getPropFloat32 called,");
        return new ArrayList<>(m_propFloat32);
    }

  
    @Override
    public void setPropFloat64(List<Double> propFloat64)
    {
        Log.i(TAG, "request setPropFloat64 called ");
        if (!m_propFloat64.equals(propFloat64))
        {
            m_propFloat64 = new ArrayList<>(propFloat64);
            onPropFloat64Changed(new ArrayList<>(m_propFloat64));
        }

    }

    @Override
    public List<Double> getPropFloat64()
    {
        Log.i(TAG, "request getPropFloat64 called,");
        return new ArrayList<>(m_propFloat64);
    }

  
    @Override
    public void setPropString(List<String> propString)
    {
        Log.i(TAG, "request setPropString called ");
        if (!m_propString.equals(propString))
        {
            m_propString = new ArrayList<>(propString);
            onPropStringChanged(new ArrayList<>(m_propString));
        }

    }

    @Override
    public List<String> getPropString()
    {
        Log.i(TAG, "request getPropString called,");
        return new ArrayList<>(m_propString);
    }

  
    @Override
    public void setPropReadOnlyString(String propReadOnlyString)
    {
        Log.i(TAG, "request setPropReadOnlyString called ");
        if (m_propReadOnlyString != propReadOnlyString)
        {
            m_propReadOnlyString = propReadOnlyString;
            onPropReadOnlyStringChanged(m_propReadOnlyString);
        }

    }

    @Override
    public String getPropReadOnlyString()
    {
        Log.i(TAG, "request getPropReadOnlyString called,");
        return m_propReadOnlyString;
    }

  
    // methods

    @Override
    public List<Boolean> funcBool(List<Boolean> paramBool) {
        Log.i(TAG, "request method funcBool called, returnig default");
        return new ArrayList<>();
    }

    @Override
    public  CompletableFuture<List<Boolean>> funcBoolAsync(List<Boolean> paramBool) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcBool(paramBool); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<Boolean>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<Integer> funcInt(List<Integer> paramInt) {
        Log.i(TAG, "request method funcInt called, returnig default");
        return new ArrayList<>();
    }

    @Override
    public  CompletableFuture<List<Integer>> funcIntAsync(List<Integer> paramInt) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcInt(paramInt); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<Integer>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<Integer> funcInt32(List<Integer> paramInt32) {
        Log.i(TAG, "request method funcInt32 called, returnig default");
        return new ArrayList<>();
    }

    @Override
    public  CompletableFuture<List<Integer>> funcInt32Async(List<Integer> paramInt32) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcInt32(paramInt32); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<Integer>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<Long> funcInt64(List<Long> paramInt64) {
        Log.i(TAG, "request method funcInt64 called, returnig default");
        return new ArrayList<>();
    }

    @Override
    public  CompletableFuture<List<Long>> funcInt64Async(List<Long> paramInt64) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcInt64(paramInt64); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<Long>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<Float> funcFloat(List<Float> paramFloat) {
        Log.i(TAG, "request method funcFloat called, returnig default");
        return new ArrayList<>();
    }

    @Override
    public  CompletableFuture<List<Float>> funcFloatAsync(List<Float> paramFloat) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcFloat(paramFloat); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<Float>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<Float> funcFloat32(List<Float> paramFloat32) {
        Log.i(TAG, "request method funcFloat32 called, returnig default");
        return new ArrayList<>();
    }

    @Override
    public  CompletableFuture<List<Float>> funcFloat32Async(List<Float> paramFloat32) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcFloat32(paramFloat32); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<Float>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<Double> funcFloat64(List<Double> paramFloat) {
        Log.i(TAG, "request method funcFloat64 called, returnig default");
        return new ArrayList<>();
    }

    @Override
    public  CompletableFuture<List<Double>> funcFloat64Async(List<Double> paramFloat) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcFloat64(paramFloat); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<Double>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<String> funcString(List<String> paramString) {
        Log.i(TAG, "request method funcString called, returnig default");
        return new ArrayList<>();
    }

    @Override
    public  CompletableFuture<List<String>> funcStringAsync(List<String> paramString) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcString(paramString); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<String>> f = new CompletableFuture<>();
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

    //In theory event listener interface
    private void onPropBoolChanged(List<Boolean> newValue)
    {
         Log.i(TAG, "onPropBoolChanged, will pass notification to all listeners");
         firePropBoolChanged(newValue);
    }
    private void onPropIntChanged(List<Integer> newValue)
    {
         Log.i(TAG, "onPropIntChanged, will pass notification to all listeners");
         firePropIntChanged(newValue);
    }
    private void onPropInt32Changed(List<Integer> newValue)
    {
         Log.i(TAG, "onPropInt32Changed, will pass notification to all listeners");
         firePropInt32Changed(newValue);
    }
    private void onPropInt64Changed(List<Long> newValue)
    {
         Log.i(TAG, "onPropInt64Changed, will pass notification to all listeners");
         firePropInt64Changed(newValue);
    }
    private void onPropFloatChanged(List<Float> newValue)
    {
         Log.i(TAG, "onPropFloatChanged, will pass notification to all listeners");
         firePropFloatChanged(newValue);
    }
    private void onPropFloat32Changed(List<Float> newValue)
    {
         Log.i(TAG, "onPropFloat32Changed, will pass notification to all listeners");
         firePropFloat32Changed(newValue);
    }
    private void onPropFloat64Changed(List<Double> newValue)
    {
         Log.i(TAG, "onPropFloat64Changed, will pass notification to all listeners");
         firePropFloat64Changed(newValue);
    }
    private void onPropStringChanged(List<String> newValue)
    {
         Log.i(TAG, "onPropStringChanged, will pass notification to all listeners");
         firePropStringChanged(newValue);
    }
    private void onPropReadOnlyStringChanged(String newValue)
    {
         Log.i(TAG, "onPropReadOnlyStringChanged, will pass notification to all listeners");
         firePropReadOnlyStringChanged(newValue);
    }
    public void onSigBool(List<Boolean> paramBool)
    {
        Log.i(TAG, "onSigBool, will pass notification to all listeners");
        fireSigBool(paramBool);
    }
    public void onSigInt(List<Integer> paramInt)
    {
        Log.i(TAG, "onSigInt, will pass notification to all listeners");
        fireSigInt(paramInt);
    }
    public void onSigInt32(List<Integer> paramInt32)
    {
        Log.i(TAG, "onSigInt32, will pass notification to all listeners");
        fireSigInt32(paramInt32);
    }
    public void onSigInt64(List<Long> paramInt64)
    {
        Log.i(TAG, "onSigInt64, will pass notification to all listeners");
        fireSigInt64(paramInt64);
    }
    public void onSigFloat(List<Float> paramFloat)
    {
        Log.i(TAG, "onSigFloat, will pass notification to all listeners");
        fireSigFloat(paramFloat);
    }
    public void onSigFloat32(List<Float> paramFloa32)
    {
        Log.i(TAG, "onSigFloat32, will pass notification to all listeners");
        fireSigFloat32(paramFloa32);
    }
    public void onSigFloat64(List<Double> paramFloat64)
    {
        Log.i(TAG, "onSigFloat64, will pass notification to all listeners");
        fireSigFloat64(paramFloat64);
    }
    public void onSigString(List<String> paramString)
    {
        Log.i(TAG, "onSigString, will pass notification to all listeners");
        fireSigString(paramString);
    }

}