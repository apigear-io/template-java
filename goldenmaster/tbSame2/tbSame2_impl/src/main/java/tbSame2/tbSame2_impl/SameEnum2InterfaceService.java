package tbSame2.tbSame2_impl;

import android.os.Messenger;
import android.util.Log;

import tbSame2.tbSame2_api.ISameEnum2Interface;
import tbSame2.tbSame2_api.AbstractSameEnum2Interface;
import tbSame2.tbSame2_api.ISameEnum2InterfaceEventListener;
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_api.Enum2;


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


public class SameEnum2InterfaceService extends AbstractSameEnum2Interface {

    private final static String TAG = "SameEnum2InterfaceService";
    private volatile boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Enum1 m_prop1 = Enum1.Value1;
    private Enum2 m_prop2 = Enum2.Value1;

    public SameEnum2InterfaceService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setProp1(Enum1 prop1)
    {
        Log.i(TAG, "request setProp1 called ");
        if (m_prop1 != prop1)
        {
            m_prop1 = prop1;
            onProp1Changed(m_prop1);
        }

    }

    @Override
    public Enum1 getProp1()
    {
        Log.i(TAG, "request getProp1 called,");
        return m_prop1;
    }

  
    @Override
    public void setProp2(Enum2 prop2)
    {
        Log.i(TAG, "request setProp2 called ");
        if (m_prop2 != prop2)
        {
            m_prop2 = prop2;
            onProp2Changed(m_prop2);
        }

    }

    @Override
    public Enum2 getProp2()
    {
        Log.i(TAG, "request getProp2 called,");
        return m_prop2;
    }

  
    // methods

    @Override
    public Enum1 func1(Enum1 param1) {
        Log.i(TAG, "request method func1 called, returnig default");
        return Enum1.Value1;
    }

    @Override
    public  CompletableFuture<Enum1> func1Async(Enum1 param1) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func1(param1); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Enum1> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public Enum1 func2(Enum1 param1, Enum2 param2) {
        Log.i(TAG, "request method func2 called, returnig default");
        return Enum1.Value1;
    }

    @Override
    public  CompletableFuture<Enum1> func2Async(Enum1 param1, Enum2 param2) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func2(param1, param2); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Enum1> f = new CompletableFuture<>();
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
    private void onProp1Changed(Enum1 newValue)
    {
         Log.i(TAG, "onProp1Changed, will pass notification to all listeners");
         fireProp1Changed(newValue);
    }
    private void onProp2Changed(Enum2 newValue)
    {
         Log.i(TAG, "onProp2Changed, will pass notification to all listeners");
         fireProp2Changed(newValue);
    }
    public void onSig1(Enum1 param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }
    public void onSig2(Enum1 param1, Enum2 param2)
    {
        Log.i(TAG, "onSig2, will pass notification to all listeners");
        fireSig2(param1, param2);
    }

}