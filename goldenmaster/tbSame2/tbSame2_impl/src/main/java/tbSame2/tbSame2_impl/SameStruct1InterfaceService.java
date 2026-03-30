package tbSame2.tbSame2_impl;

import android.os.Messenger;
import android.util.Log;

import tbSame2.tbSame2_api.ISameStruct1Interface;
import tbSame2.tbSame2_api.AbstractSameStruct1Interface;
import tbSame2.tbSame2_api.ISameStruct1InterfaceEventListener;
import tbSame2.tbSame2_api.Struct1;


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


public class SameStruct1InterfaceService extends AbstractSameStruct1Interface {

    private final static String TAG = "SameStruct1InterfaceService";
    private volatile boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Struct1 m_prop1 = new Struct1();

    public SameStruct1InterfaceService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setProp1(Struct1 prop1)
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
    public Struct1 getProp1()
    {
        Log.i(TAG, "request getProp1 called,");
        return m_prop1;
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
    private void onProp1Changed(Struct1 newValue)
    {
         Log.i(TAG, "onProp1Changed, will pass notification to all listeners");
         fireProp1Changed(newValue);
    }
    public void onSig1(Struct1 param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }

}