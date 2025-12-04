package tbSimple.tbSimple_impl;

import android.os.Messenger;
import android.util.Log;

import tbSimple.tbSimple_api.ISimpleArrayInterface;
import tbSimple.tbSimple_api.AbstractSimpleArrayInterface;
import tbSimple.tbSimple_api.ISimpleArrayInterfaceEventListener;


import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.Arrays;


public class SimpleArrayInterfaceService extends AbstractSimpleArrayInterface {

    private final static String TAG = "SimpleArrayInterfaceService";
    private static boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private boolean[] m_propBool = new boolean[]{};
    private int[] m_propInt = new int[]{};
    private int[] m_propInt32 = new int[]{};
    private long[] m_propInt64 = new long[]{};
    private float[] m_propFloat = new float[]{};
    private float[] m_propFloat32 = new float[]{};
    private double[] m_propFloat64 = new double[]{};
    private String[] m_propString = new String[]{};
    private String m_propReadOnlyString = new String();

    public SimpleArrayInterfaceService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setPropBool(boolean[] propBool)
    {
        Log.i(TAG, "request setPropBool called ");
        if (! Arrays.equals(m_propBool, propBool))
        {
            m_propBool = propBool;
            onPropBoolChanged(m_propBool);
        }

    }

    @Override
    public boolean[] getPropBool()
    {
        Log.i(TAG, "request getPropBool called,");
        return m_propBool;
    }

  
    @Override
    public void setPropInt(int[] propInt)
    {
        Log.i(TAG, "request setPropInt called ");
        if (! Arrays.equals(m_propInt, propInt))
        {
            m_propInt = propInt;
            onPropIntChanged(m_propInt);
        }

    }

    @Override
    public int[] getPropInt()
    {
        Log.i(TAG, "request getPropInt called,");
        return m_propInt;
    }

  
    @Override
    public void setPropInt32(int[] propInt32)
    {
        Log.i(TAG, "request setPropInt32 called ");
        if (! Arrays.equals(m_propInt32, propInt32))
        {
            m_propInt32 = propInt32;
            onPropInt32Changed(m_propInt32);
        }

    }

    @Override
    public int[] getPropInt32()
    {
        Log.i(TAG, "request getPropInt32 called,");
        return m_propInt32;
    }

  
    @Override
    public void setPropInt64(long[] propInt64)
    {
        Log.i(TAG, "request setPropInt64 called ");
        if (! Arrays.equals(m_propInt64, propInt64))
        {
            m_propInt64 = propInt64;
            onPropInt64Changed(m_propInt64);
        }

    }

    @Override
    public long[] getPropInt64()
    {
        Log.i(TAG, "request getPropInt64 called,");
        return m_propInt64;
    }

  
    @Override
    public void setPropFloat(float[] propFloat)
    {
        Log.i(TAG, "request setPropFloat called ");
        if (! Arrays.equals(m_propFloat, propFloat))
        {
            m_propFloat = propFloat;
            onPropFloatChanged(m_propFloat);
        }

    }

    @Override
    public float[] getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called,");
        return m_propFloat;
    }

  
    @Override
    public void setPropFloat32(float[] propFloat32)
    {
        Log.i(TAG, "request setPropFloat32 called ");
        if (! Arrays.equals(m_propFloat32, propFloat32))
        {
            m_propFloat32 = propFloat32;
            onPropFloat32Changed(m_propFloat32);
        }

    }

    @Override
    public float[] getPropFloat32()
    {
        Log.i(TAG, "request getPropFloat32 called,");
        return m_propFloat32;
    }

  
    @Override
    public void setPropFloat64(double[] propFloat64)
    {
        Log.i(TAG, "request setPropFloat64 called ");
        if (! Arrays.equals(m_propFloat64, propFloat64))
        {
            m_propFloat64 = propFloat64;
            onPropFloat64Changed(m_propFloat64);
        }

    }

    @Override
    public double[] getPropFloat64()
    {
        Log.i(TAG, "request getPropFloat64 called,");
        return m_propFloat64;
    }

  
    @Override
    public void setPropString(String[] propString)
    {
        Log.i(TAG, "request setPropString called ");
        if (! Arrays.equals(m_propString, propString))
        {
            m_propString = propString;
            onPropStringChanged(m_propString);
        }

    }

    @Override
    public String[] getPropString()
    {
        Log.i(TAG, "request getPropString called,");
        return m_propString;
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
    public boolean[] funcBool(boolean[] paramBool) {
        Log.i(TAG, "request method funcBool called, returnig default");
        return new boolean[]{};
    }

    @Override
    public  CompletableFuture<boolean[]> funcBoolAsync(boolean[] paramBool) {
        return CompletableFuture.supplyAsync(
                () -> {return funcBool(paramBool); },
                executor);
    }

    @Override
    public int[] funcInt(int[] paramInt) {
        Log.i(TAG, "request method funcInt called, returnig default");
        return new int[]{};
    }

    @Override
    public  CompletableFuture<int[]> funcIntAsync(int[] paramInt) {
        return CompletableFuture.supplyAsync(
                () -> {return funcInt(paramInt); },
                executor);
    }

    @Override
    public int[] funcInt32(int[] paramInt32) {
        Log.i(TAG, "request method funcInt32 called, returnig default");
        return new int[]{};
    }

    @Override
    public  CompletableFuture<int[]> funcInt32Async(int[] paramInt32) {
        return CompletableFuture.supplyAsync(
                () -> {return funcInt32(paramInt32); },
                executor);
    }

    @Override
    public long[] funcInt64(long[] paramInt64) {
        Log.i(TAG, "request method funcInt64 called, returnig default");
        return new long[]{};
    }

    @Override
    public  CompletableFuture<long[]> funcInt64Async(long[] paramInt64) {
        return CompletableFuture.supplyAsync(
                () -> {return funcInt64(paramInt64); },
                executor);
    }

    @Override
    public float[] funcFloat(float[] paramFloat) {
        Log.i(TAG, "request method funcFloat called, returnig default");
        return new float[]{};
    }

    @Override
    public  CompletableFuture<float[]> funcFloatAsync(float[] paramFloat) {
        return CompletableFuture.supplyAsync(
                () -> {return funcFloat(paramFloat); },
                executor);
    }

    @Override
    public float[] funcFloat32(float[] paramFloat32) {
        Log.i(TAG, "request method funcFloat32 called, returnig default");
        return new float[]{};
    }

    @Override
    public  CompletableFuture<float[]> funcFloat32Async(float[] paramFloat32) {
        return CompletableFuture.supplyAsync(
                () -> {return funcFloat32(paramFloat32); },
                executor);
    }

    @Override
    public double[] funcFloat64(double[] paramFloat) {
        Log.i(TAG, "request method funcFloat64 called, returnig default");
        return new double[]{};
    }

    @Override
    public  CompletableFuture<double[]> funcFloat64Async(double[] paramFloat) {
        return CompletableFuture.supplyAsync(
                () -> {return funcFloat64(paramFloat); },
                executor);
    }

    @Override
    public String[] funcString(String[] paramString) {
        Log.i(TAG, "request method funcString called, returnig default");
        return new String[]{};
    }

    @Override
    public  CompletableFuture<String[]> funcStringAsync(String[] paramString) {
        return CompletableFuture.supplyAsync(
                () -> {return funcString(paramString); },
                executor);
    }    

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    //In theory event listener interface
    private void onPropBoolChanged(boolean[] newValue)
    {
         Log.i(TAG, "onPropBoolChanged, will pass notification to all listeners");
         firePropBoolChanged(newValue);
    }
    private void onPropIntChanged(int[] newValue)
    {
         Log.i(TAG, "onPropIntChanged, will pass notification to all listeners");
         firePropIntChanged(newValue);
    }
    private void onPropInt32Changed(int[] newValue)
    {
         Log.i(TAG, "onPropInt32Changed, will pass notification to all listeners");
         firePropInt32Changed(newValue);
    }
    private void onPropInt64Changed(long[] newValue)
    {
         Log.i(TAG, "onPropInt64Changed, will pass notification to all listeners");
         firePropInt64Changed(newValue);
    }
    private void onPropFloatChanged(float[] newValue)
    {
         Log.i(TAG, "onPropFloatChanged, will pass notification to all listeners");
         firePropFloatChanged(newValue);
    }
    private void onPropFloat32Changed(float[] newValue)
    {
         Log.i(TAG, "onPropFloat32Changed, will pass notification to all listeners");
         firePropFloat32Changed(newValue);
    }
    private void onPropFloat64Changed(double[] newValue)
    {
         Log.i(TAG, "onPropFloat64Changed, will pass notification to all listeners");
         firePropFloat64Changed(newValue);
    }
    private void onPropStringChanged(String[] newValue)
    {
         Log.i(TAG, "onPropStringChanged, will pass notification to all listeners");
         firePropStringChanged(newValue);
    }
    private void onPropReadOnlyStringChanged(String newValue)
    {
         Log.i(TAG, "onPropReadOnlyStringChanged, will pass notification to all listeners");
         firePropReadOnlyStringChanged(newValue);
    }
    public void onSigBool(boolean[] paramBool)
    {
        Log.i(TAG, "onSigBool, will pass notification to all listeners");
        fireSigBool(paramBool);
    }
    public void onSigInt(int[] paramInt)
    {
        Log.i(TAG, "onSigInt, will pass notification to all listeners");
        fireSigInt(paramInt);
    }
    public void onSigInt32(int[] paramInt32)
    {
        Log.i(TAG, "onSigInt32, will pass notification to all listeners");
        fireSigInt32(paramInt32);
    }
    public void onSigInt64(long[] paramInt64)
    {
        Log.i(TAG, "onSigInt64, will pass notification to all listeners");
        fireSigInt64(paramInt64);
    }
    public void onSigFloat(float[] paramFloat)
    {
        Log.i(TAG, "onSigFloat, will pass notification to all listeners");
        fireSigFloat(paramFloat);
    }
    public void onSigFloat32(float[] paramFloa32)
    {
        Log.i(TAG, "onSigFloat32, will pass notification to all listeners");
        fireSigFloat32(paramFloa32);
    }
    public void onSigFloat64(double[] paramFloat64)
    {
        Log.i(TAG, "onSigFloat64, will pass notification to all listeners");
        fireSigFloat64(paramFloat64);
    }
    public void onSigString(String[] paramString)
    {
        Log.i(TAG, "onSigString, will pass notification to all listeners");
        fireSigString(paramString);
    }

}