package testbed1.testbed1_impl;

import android.os.Messenger;
import android.util.Log;

import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_api.AbstractStructArrayInterface;
import testbed1.testbed1_api.IStructArrayInterfaceEventListener;
import testbed1.testbed1_api.Enum0;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_api.StructString;


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


public class StructArrayInterfaceService extends AbstractStructArrayInterface {

    private final static String TAG = "StructArrayInterfaceService";
    private volatile boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private List<StructBool> m_propBool = new ArrayList<>();
    private List<StructInt> m_propInt = new ArrayList<>();
    private List<StructFloat> m_propFloat = new ArrayList<>();
    private List<StructString> m_propString = new ArrayList<>();
    private List<Enum0> m_propEnum = new ArrayList<>();

    public StructArrayInterfaceService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setPropBool(List<StructBool> propBool)
    {
        Log.i(TAG, "request setPropBool called ");
        if (!m_propBool.equals(propBool))
        {
            m_propBool = new ArrayList<>(propBool);
            onPropBoolChanged(new ArrayList<>(m_propBool));
        }

    }

    @Override
    public List<StructBool> getPropBool()
    {
        Log.i(TAG, "request getPropBool called,");
        return new ArrayList<>(m_propBool);
    }

  
    @Override
    public void setPropInt(List<StructInt> propInt)
    {
        Log.i(TAG, "request setPropInt called ");
        if (!m_propInt.equals(propInt))
        {
            m_propInt = new ArrayList<>(propInt);
            onPropIntChanged(new ArrayList<>(m_propInt));
        }

    }

    @Override
    public List<StructInt> getPropInt()
    {
        Log.i(TAG, "request getPropInt called,");
        return new ArrayList<>(m_propInt);
    }

  
    @Override
    public void setPropFloat(List<StructFloat> propFloat)
    {
        Log.i(TAG, "request setPropFloat called ");
        if (!m_propFloat.equals(propFloat))
        {
            m_propFloat = new ArrayList<>(propFloat);
            onPropFloatChanged(new ArrayList<>(m_propFloat));
        }

    }

    @Override
    public List<StructFloat> getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called,");
        return new ArrayList<>(m_propFloat);
    }

  
    @Override
    public void setPropString(List<StructString> propString)
    {
        Log.i(TAG, "request setPropString called ");
        if (!m_propString.equals(propString))
        {
            m_propString = new ArrayList<>(propString);
            onPropStringChanged(new ArrayList<>(m_propString));
        }

    }

    @Override
    public List<StructString> getPropString()
    {
        Log.i(TAG, "request getPropString called,");
        return new ArrayList<>(m_propString);
    }

  
    @Override
    public void setPropEnum(List<Enum0> propEnum)
    {
        Log.i(TAG, "request setPropEnum called ");
        if (!m_propEnum.equals(propEnum))
        {
            m_propEnum = new ArrayList<>(propEnum);
            onPropEnumChanged(new ArrayList<>(m_propEnum));
        }

    }

    @Override
    public List<Enum0> getPropEnum()
    {
        Log.i(TAG, "request getPropEnum called,");
        return new ArrayList<>(m_propEnum);
    }

  
    // methods

    @Override
    public List<StructBool> funcBool(List<StructBool> paramBool) {
        Log.i(TAG, "request method funcBool called, returnig default");
        return new ArrayList<>();
    }

    @Override
    public  CompletableFuture<List<StructBool>> funcBoolAsync(List<StructBool> paramBool) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcBool(paramBool); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<StructBool>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<StructInt> funcInt(List<StructInt> paramInt) {
        Log.i(TAG, "request method funcInt called, returnig default");
        return new ArrayList<>();
    }

    @Override
    public  CompletableFuture<List<StructInt>> funcIntAsync(List<StructInt> paramInt) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcInt(paramInt); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<StructInt>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<StructFloat> funcFloat(List<StructFloat> paramFloat) {
        Log.i(TAG, "request method funcFloat called, returnig default");
        return new ArrayList<>();
    }

    @Override
    public  CompletableFuture<List<StructFloat>> funcFloatAsync(List<StructFloat> paramFloat) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcFloat(paramFloat); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<StructFloat>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<StructString> funcString(List<StructString> paramString) {
        Log.i(TAG, "request method funcString called, returnig default");
        return new ArrayList<>();
    }

    @Override
    public  CompletableFuture<List<StructString>> funcStringAsync(List<StructString> paramString) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcString(paramString); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<StructString>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<Enum0> funcEnum(List<Enum0> paramEnum) {
        Log.i(TAG, "request method funcEnum called, returnig default");
        return new ArrayList<>();
    }

    @Override
    public  CompletableFuture<List<Enum0>> funcEnumAsync(List<Enum0> paramEnum) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcEnum(paramEnum); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<Enum0>> f = new CompletableFuture<>();
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
    private void onPropBoolChanged(List<StructBool> newValue)
    {
         Log.i(TAG, "onPropBoolChanged, will pass notification to all listeners");
         firePropBoolChanged(newValue);
    }
    private void onPropIntChanged(List<StructInt> newValue)
    {
         Log.i(TAG, "onPropIntChanged, will pass notification to all listeners");
         firePropIntChanged(newValue);
    }
    private void onPropFloatChanged(List<StructFloat> newValue)
    {
         Log.i(TAG, "onPropFloatChanged, will pass notification to all listeners");
         firePropFloatChanged(newValue);
    }
    private void onPropStringChanged(List<StructString> newValue)
    {
         Log.i(TAG, "onPropStringChanged, will pass notification to all listeners");
         firePropStringChanged(newValue);
    }
    private void onPropEnumChanged(List<Enum0> newValue)
    {
         Log.i(TAG, "onPropEnumChanged, will pass notification to all listeners");
         firePropEnumChanged(newValue);
    }
    public void onSigBool(List<StructBool> paramBool)
    {
        Log.i(TAG, "onSigBool, will pass notification to all listeners");
        fireSigBool(paramBool);
    }
    public void onSigInt(List<StructInt> paramInt)
    {
        Log.i(TAG, "onSigInt, will pass notification to all listeners");
        fireSigInt(paramInt);
    }
    public void onSigFloat(List<StructFloat> paramFloat)
    {
        Log.i(TAG, "onSigFloat, will pass notification to all listeners");
        fireSigFloat(paramFloat);
    }
    public void onSigString(List<StructString> paramString)
    {
        Log.i(TAG, "onSigString, will pass notification to all listeners");
        fireSigString(paramString);
    }
    public void onSigEnum(List<Enum0> paramEnum)
    {
        Log.i(TAG, "onSigEnum, will pass notification to all listeners");
        fireSigEnum(paramEnum);
    }

}