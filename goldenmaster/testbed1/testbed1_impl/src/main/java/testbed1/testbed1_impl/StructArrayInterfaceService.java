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


import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.Arrays;


public class StructArrayInterfaceService extends AbstractStructArrayInterface {

    private final static String TAG = "StructArrayInterfaceService";
    private static boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);
    private StructBool[] m_propBool = new StructBool[]{};
    private StructInt[] m_propInt = new StructInt[]{};
    private StructFloat[] m_propFloat = new StructFloat[]{};
    private StructString[] m_propString = new StructString[]{};
    private Enum0[] m_propEnum = new Enum0[]{};

    public StructArrayInterfaceService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setPropBool(StructBool[] propBool)
    {
        Log.i(TAG, "request setPropBool called ");
        if (! Arrays.equals(m_propBool, propBool))
        {
            m_propBool = propBool;
            onPropBoolChanged(m_propBool);
        }

    }

    @Override
    public StructBool[] getPropBool()
    {
        Log.i(TAG, "request getPropBool called,");
        return m_propBool;
    }

  
    @Override
    public void setPropInt(StructInt[] propInt)
    {
        Log.i(TAG, "request setPropInt called ");
        if (! Arrays.equals(m_propInt, propInt))
        {
            m_propInt = propInt;
            onPropIntChanged(m_propInt);
        }

    }

    @Override
    public StructInt[] getPropInt()
    {
        Log.i(TAG, "request getPropInt called,");
        return m_propInt;
    }

  
    @Override
    public void setPropFloat(StructFloat[] propFloat)
    {
        Log.i(TAG, "request setPropFloat called ");
        if (! Arrays.equals(m_propFloat, propFloat))
        {
            m_propFloat = propFloat;
            onPropFloatChanged(m_propFloat);
        }

    }

    @Override
    public StructFloat[] getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called,");
        return m_propFloat;
    }

  
    @Override
    public void setPropString(StructString[] propString)
    {
        Log.i(TAG, "request setPropString called ");
        if (! Arrays.equals(m_propString, propString))
        {
            m_propString = propString;
            onPropStringChanged(m_propString);
        }

    }

    @Override
    public StructString[] getPropString()
    {
        Log.i(TAG, "request getPropString called,");
        return m_propString;
    }

  
    @Override
    public void setPropEnum(Enum0[] propEnum)
    {
        Log.i(TAG, "request setPropEnum called ");
        if (! Arrays.equals(m_propEnum, propEnum))
        {
            m_propEnum = propEnum;
            onPropEnumChanged(m_propEnum);
        }

    }

    @Override
    public Enum0[] getPropEnum()
    {
        Log.i(TAG, "request getPropEnum called,");
        return m_propEnum;
    }

  
    // methods

    @Override
    public StructBool[] funcBool(StructBool[] paramBool) {
        Log.w(TAG, "request method funcBool called, returnig default");
        return new StructBool[]{};
    }

    @Override
    public  CompletableFuture<StructBool[]> funcBoolAsync(StructBool[] paramBool) {
        return CompletableFuture.supplyAsync(
                () -> {return funcBool(paramBool); },
                executor);
    }

    @Override
    public StructInt[] funcInt(StructInt[] paramInt) {
        Log.w(TAG, "request method funcInt called, returnig default");
        return new StructInt[]{};
    }

    @Override
    public  CompletableFuture<StructInt[]> funcIntAsync(StructInt[] paramInt) {
        return CompletableFuture.supplyAsync(
                () -> {return funcInt(paramInt); },
                executor);
    }

    @Override
    public StructFloat[] funcFloat(StructFloat[] paramFloat) {
        Log.w(TAG, "request method funcFloat called, returnig default");
        return new StructFloat[]{};
    }

    @Override
    public  CompletableFuture<StructFloat[]> funcFloatAsync(StructFloat[] paramFloat) {
        return CompletableFuture.supplyAsync(
                () -> {return funcFloat(paramFloat); },
                executor);
    }

    @Override
    public StructString[] funcString(StructString[] paramString) {
        Log.w(TAG, "request method funcString called, returnig default");
        return new StructString[]{};
    }

    @Override
    public  CompletableFuture<StructString[]> funcStringAsync(StructString[] paramString) {
        return CompletableFuture.supplyAsync(
                () -> {return funcString(paramString); },
                executor);
    }

    @Override
    public Enum0[] funcEnum(Enum0[] paramEnum) {
        Log.w(TAG, "request method funcEnum called, returnig default");
        return new Enum0[]{};
    }

    @Override
    public  CompletableFuture<Enum0[]> funcEnumAsync(Enum0[] paramEnum) {
        return CompletableFuture.supplyAsync(
                () -> {return funcEnum(paramEnum); },
                executor);
    }    

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    //In theory event listener interface
    private void onPropBoolChanged(StructBool[] newValue)
    {
         Log.i(TAG, "onPropBoolChanged, will pass notification to all listeners");
         firePropBoolChanged(newValue);
    }
    private void onPropIntChanged(StructInt[] newValue)
    {
         Log.i(TAG, "onPropIntChanged, will pass notification to all listeners");
         firePropIntChanged(newValue);
    }
    private void onPropFloatChanged(StructFloat[] newValue)
    {
         Log.i(TAG, "onPropFloatChanged, will pass notification to all listeners");
         firePropFloatChanged(newValue);
    }
    private void onPropStringChanged(StructString[] newValue)
    {
         Log.i(TAG, "onPropStringChanged, will pass notification to all listeners");
         firePropStringChanged(newValue);
    }
    private void onPropEnumChanged(Enum0[] newValue)
    {
         Log.i(TAG, "onPropEnumChanged, will pass notification to all listeners");
         firePropEnumChanged(newValue);
    }
    public void onSigBool(StructBool[] paramBool)
    {
        Log.i(TAG, "onSigBool, will pass notification to all listeners");
        fireSigBool(paramBool);
    }
    public void onSigInt(StructInt[] paramInt)
    {
        Log.i(TAG, "onSigInt, will pass notification to all listeners");
        fireSigInt(paramInt);
    }
    public void onSigFloat(StructFloat[] paramFloat)
    {
        Log.i(TAG, "onSigFloat, will pass notification to all listeners");
        fireSigFloat(paramFloat);
    }
    public void onSigString(StructString[] paramString)
    {
        Log.i(TAG, "onSigString, will pass notification to all listeners");
        fireSigString(paramString);
    }
    public void onSigEnum(Enum0[] paramEnum)
    {
        Log.i(TAG, "onSigEnum, will pass notification to all listeners");
        fireSigEnum(paramEnum);
    }

}