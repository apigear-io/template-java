package tbSame1.tbSame1_impl;

import android.os.Messenger;
import android.util.Log;

import tbSame1.tbSame1_api.ISameStruct2Interface;
import tbSame1.tbSame1_api.AbstractSameStruct2Interface;
import tbSame1.tbSame1_api.ISameStruct2InterfaceEventListener;
import tbSame1.tbSame1_api.Struct1;
import tbSame1.tbSame1_api.Struct2;


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


public class SameStruct2InterfaceService extends AbstractSameStruct2Interface {

    private final static String TAG = "SameStruct2InterfaceService";
    private volatile boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Struct2 m_prop1 = new Struct2();
    private Struct2 m_prop2 = new Struct2();

    public SameStruct2InterfaceService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setProp1(Struct2 prop1)
    {
        Log.i(TAG, "request setProp1 called ");
        if ( (m_prop1 != null && ! m_prop1.equals(prop1))
        || (m_prop1 == null && prop1 != null ))
        {
            m_prop1 = prop1;
            onProp1Changed(m_prop1);
        }

    }

    @Override
    public Struct2 getProp1()
    {
        Log.i(TAG, "request getProp1 called,");
        return m_prop1;
    }

  
    @Override
    public void setProp2(Struct2 prop2)
    {
        Log.i(TAG, "request setProp2 called ");
        if ( (m_prop2 != null && ! m_prop2.equals(prop2))
        || (m_prop2 == null && prop2 != null ))
        {
            m_prop2 = prop2;
            onProp2Changed(m_prop2);
        }

    }

    @Override
    public Struct2 getProp2()
    {
        Log.i(TAG, "request getProp2 called,");
        return m_prop2;
    }

  
    // methods

    @Override
    public Struct1 func1(Struct1 param1) {
        Log.i(TAG, "request method func1 called, returnig default");
        return new Struct1();
    }

    @Override
    public  CompletableFuture<Struct1> func1Async(Struct1 param1) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func1(param1); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Struct1> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public Struct1 func2(Struct1 param1, Struct2 param2) {
        Log.i(TAG, "request method func2 called, returnig default");
        return new Struct1();
    }

    @Override
    public  CompletableFuture<Struct1> func2Async(Struct1 param1, Struct2 param2) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return func2(param1, param2); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Struct1> f = new CompletableFuture<>();
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
    private void onProp1Changed(Struct2 newValue)
    {
         Log.i(TAG, "onProp1Changed, will pass notification to all listeners");
         fireProp1Changed(newValue);
    }
    private void onProp2Changed(Struct2 newValue)
    {
         Log.i(TAG, "onProp2Changed, will pass notification to all listeners");
         fireProp2Changed(newValue);
    }
    public void onSig1(Struct1 param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }
    public void onSig2(Struct1 param1, Struct2 param2)
    {
        Log.i(TAG, "onSig2, will pass notification to all listeners");
        fireSig2(param1, param2);
    }

}