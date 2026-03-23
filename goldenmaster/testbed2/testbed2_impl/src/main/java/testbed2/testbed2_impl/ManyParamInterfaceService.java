package testbed2.testbed2_impl;

import android.os.Messenger;
import android.util.Log;

import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_api.AbstractManyParamInterface;
import testbed2.testbed2_api.IManyParamInterfaceEventListener;


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


public class ManyParamInterfaceService extends AbstractManyParamInterface {

    private final static String TAG = "ManyParamInterfaceService";
    private static boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private int m_prop1 = 0;
    private int m_prop2 = 0;
    private int m_prop3 = 0;
    private int m_prop4 = 0;

    public ManyParamInterfaceService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setProp1(int prop1)
    {
        Log.i(TAG, "request setProp1 called ");
        if (m_prop1 != prop1)
        {
            m_prop1 = prop1;
            onProp1Changed(m_prop1);
        }

    }

    @Override
    public int getProp1()
    {
        Log.i(TAG, "request getProp1 called,");
        return m_prop1;
    }

  
    @Override
    public void setProp2(int prop2)
    {
        Log.i(TAG, "request setProp2 called ");
        if (m_prop2 != prop2)
        {
            m_prop2 = prop2;
            onProp2Changed(m_prop2);
        }

    }

    @Override
    public int getProp2()
    {
        Log.i(TAG, "request getProp2 called,");
        return m_prop2;
    }

  
    @Override
    public void setProp3(int prop3)
    {
        Log.i(TAG, "request setProp3 called ");
        if (m_prop3 != prop3)
        {
            m_prop3 = prop3;
            onProp3Changed(m_prop3);
        }

    }

    @Override
    public int getProp3()
    {
        Log.i(TAG, "request getProp3 called,");
        return m_prop3;
    }

  
    @Override
    public void setProp4(int prop4)
    {
        Log.i(TAG, "request setProp4 called ");
        if (m_prop4 != prop4)
        {
            m_prop4 = prop4;
            onProp4Changed(m_prop4);
        }

    }

    @Override
    public int getProp4()
    {
        Log.i(TAG, "request getProp4 called,");
        return m_prop4;
    }

  
    // methods

    @Override
    public int func1(int param1) {
        Log.i(TAG, "request method func1 called, returnig default");
        return 0;
    }

    @Override
    public  CompletableFuture<Integer> func1Async(int param1) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func1(param1); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Integer> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public int func2(int param1, int param2) {
        Log.i(TAG, "request method func2 called, returnig default");
        return 0;
    }

    @Override
    public  CompletableFuture<Integer> func2Async(int param1, int param2) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func2(param1, param2); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Integer> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public int func3(int param1, int param2, int param3) {
        Log.i(TAG, "request method func3 called, returnig default");
        return 0;
    }

    @Override
    public  CompletableFuture<Integer> func3Async(int param1, int param2, int param3) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func3(param1, param2, param3); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Integer> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public int func4(int param1, int param2, int param3, int param4) {
        Log.i(TAG, "request method func4 called, returnig default");
        return 0;
    }

    @Override
    public  CompletableFuture<Integer> func4Async(int param1, int param2, int param3, int param4) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func4(param1, param2, param3, param4); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Integer> f = new CompletableFuture<>();
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
    private void onProp1Changed(int newValue)
    {
         Log.i(TAG, "onProp1Changed, will pass notification to all listeners");
         fireProp1Changed(newValue);
    }
    private void onProp2Changed(int newValue)
    {
         Log.i(TAG, "onProp2Changed, will pass notification to all listeners");
         fireProp2Changed(newValue);
    }
    private void onProp3Changed(int newValue)
    {
         Log.i(TAG, "onProp3Changed, will pass notification to all listeners");
         fireProp3Changed(newValue);
    }
    private void onProp4Changed(int newValue)
    {
         Log.i(TAG, "onProp4Changed, will pass notification to all listeners");
         fireProp4Changed(newValue);
    }
    public void onSig1(int param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }
    public void onSig2(int param1, int param2)
    {
        Log.i(TAG, "onSig2, will pass notification to all listeners");
        fireSig2(param1, param2);
    }
    public void onSig3(int param1, int param2, int param3)
    {
        Log.i(TAG, "onSig3, will pass notification to all listeners");
        fireSig3(param1, param2, param3);
    }
    public void onSig4(int param1, int param2, int param3, int param4)
    {
        Log.i(TAG, "onSig4, will pass notification to all listeners");
        fireSig4(param1, param2, param3, param4);
    }

}