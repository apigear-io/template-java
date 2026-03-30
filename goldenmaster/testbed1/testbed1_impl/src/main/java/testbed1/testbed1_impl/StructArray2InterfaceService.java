package testbed1.testbed1_impl;

import android.os.Messenger;
import android.util.Log;

import testbed1.testbed1_api.IStructArray2Interface;
import testbed1.testbed1_api.AbstractStructArray2Interface;
import testbed1.testbed1_api.IStructArray2InterfaceEventListener;
import testbed1.testbed1_api.Enum0;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_api.StructBoolWithArray;
import testbed1.testbed1_api.StructEnumWithArray;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_api.StructFloatWithArray;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_api.StructIntWithArray;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_api.StructStringWithArray;


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
import java.util.Arrays;


public class StructArray2InterfaceService extends AbstractStructArray2Interface {

    private final static String TAG = "StructArray2InterfaceService";
    private volatile boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private StructBoolWithArray m_propBool = new StructBoolWithArray();
    private StructIntWithArray m_propInt = new StructIntWithArray();
    private StructFloatWithArray m_propFloat = new StructFloatWithArray();
    private StructStringWithArray m_propString = new StructStringWithArray();
    private StructEnumWithArray m_propEnum = new StructEnumWithArray();

    public StructArray2InterfaceService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setPropBool(StructBoolWithArray propBool)
    {
        Log.i(TAG, "request setPropBool called ");
        if ( (m_propBool != null && ! m_propBool.equals(propBool))
        || (m_propBool == null && propBool != null ))
        {
            m_propBool = propBool;
            onPropBoolChanged(m_propBool);
        }

    }

    @Override
    public StructBoolWithArray getPropBool()
    {
        Log.i(TAG, "request getPropBool called,");
        return m_propBool;
    }

  
    @Override
    public void setPropInt(StructIntWithArray propInt)
    {
        Log.i(TAG, "request setPropInt called ");
        if ( (m_propInt != null && ! m_propInt.equals(propInt))
        || (m_propInt == null && propInt != null ))
        {
            m_propInt = propInt;
            onPropIntChanged(m_propInt);
        }

    }

    @Override
    public StructIntWithArray getPropInt()
    {
        Log.i(TAG, "request getPropInt called,");
        return m_propInt;
    }

  
    @Override
    public void setPropFloat(StructFloatWithArray propFloat)
    {
        Log.i(TAG, "request setPropFloat called ");
        if ( (m_propFloat != null && ! m_propFloat.equals(propFloat))
        || (m_propFloat == null && propFloat != null ))
        {
            m_propFloat = propFloat;
            onPropFloatChanged(m_propFloat);
        }

    }

    @Override
    public StructFloatWithArray getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called,");
        return m_propFloat;
    }

  
    @Override
    public void setPropString(StructStringWithArray propString)
    {
        Log.i(TAG, "request setPropString called ");
        if ( (m_propString != null && ! m_propString.equals(propString))
        || (m_propString == null && propString != null ))
        {
            m_propString = propString;
            onPropStringChanged(m_propString);
        }

    }

    @Override
    public StructStringWithArray getPropString()
    {
        Log.i(TAG, "request getPropString called,");
        return m_propString;
    }

  
    @Override
    public void setPropEnum(StructEnumWithArray propEnum)
    {
        Log.i(TAG, "request setPropEnum called ");
        if ( (m_propEnum != null && ! m_propEnum.equals(propEnum))
        || (m_propEnum == null && propEnum != null ))
        {
            m_propEnum = propEnum;
            onPropEnumChanged(m_propEnum);
        }

    }

    @Override
    public StructEnumWithArray getPropEnum()
    {
        Log.i(TAG, "request getPropEnum called,");
        return m_propEnum;
    }

  
    // methods

    @Override
    public StructBool[] funcBool(StructBoolWithArray paramBool) {
        Log.i(TAG, "request method funcBool called, returnig default");
        return new StructBool[]{};
    }

    @Override
    public  CompletableFuture<StructBool[]> funcBoolAsync(StructBoolWithArray paramBool) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcBool(paramBool); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<StructBool[]> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public StructInt[] funcInt(StructIntWithArray paramInt) {
        Log.i(TAG, "request method funcInt called, returnig default");
        return new StructInt[]{};
    }

    @Override
    public  CompletableFuture<StructInt[]> funcIntAsync(StructIntWithArray paramInt) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcInt(paramInt); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<StructInt[]> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public StructFloat[] funcFloat(StructFloatWithArray paramFloat) {
        Log.i(TAG, "request method funcFloat called, returnig default");
        return new StructFloat[]{};
    }

    @Override
    public  CompletableFuture<StructFloat[]> funcFloatAsync(StructFloatWithArray paramFloat) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcFloat(paramFloat); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<StructFloat[]> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public StructString[] funcString(StructStringWithArray paramString) {
        Log.i(TAG, "request method funcString called, returnig default");
        return new StructString[]{};
    }

    @Override
    public  CompletableFuture<StructString[]> funcStringAsync(StructStringWithArray paramString) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcString(paramString); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<StructString[]> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public Enum0[] funcEnum(StructEnumWithArray paramEnum) {
        Log.i(TAG, "request method funcEnum called, returnig default");
        return new Enum0[]{};
    }

    @Override
    public  CompletableFuture<Enum0[]> funcEnumAsync(StructEnumWithArray paramEnum) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcEnum(paramEnum); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Enum0[]> f = new CompletableFuture<>();
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
    private void onPropBoolChanged(StructBoolWithArray newValue)
    {
         Log.i(TAG, "onPropBoolChanged, will pass notification to all listeners");
         firePropBoolChanged(newValue);
    }
    private void onPropIntChanged(StructIntWithArray newValue)
    {
         Log.i(TAG, "onPropIntChanged, will pass notification to all listeners");
         firePropIntChanged(newValue);
    }
    private void onPropFloatChanged(StructFloatWithArray newValue)
    {
         Log.i(TAG, "onPropFloatChanged, will pass notification to all listeners");
         firePropFloatChanged(newValue);
    }
    private void onPropStringChanged(StructStringWithArray newValue)
    {
         Log.i(TAG, "onPropStringChanged, will pass notification to all listeners");
         firePropStringChanged(newValue);
    }
    private void onPropEnumChanged(StructEnumWithArray newValue)
    {
         Log.i(TAG, "onPropEnumChanged, will pass notification to all listeners");
         firePropEnumChanged(newValue);
    }
    public void onSigBool(StructBoolWithArray paramBool)
    {
        Log.i(TAG, "onSigBool, will pass notification to all listeners");
        fireSigBool(paramBool);
    }
    public void onSigInt(StructIntWithArray paramInt)
    {
        Log.i(TAG, "onSigInt, will pass notification to all listeners");
        fireSigInt(paramInt);
    }
    public void onSigFloat(StructFloatWithArray paramFloat)
    {
        Log.i(TAG, "onSigFloat, will pass notification to all listeners");
        fireSigFloat(paramFloat);
    }
    public void onSigString(StructStringWithArray paramString)
    {
        Log.i(TAG, "onSigString, will pass notification to all listeners");
        fireSigString(paramString);
    }

}