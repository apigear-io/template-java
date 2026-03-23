package tbSame1.tbSame1_impl;

import android.os.Messenger;
import android.util.Log;

import tbSame1.tbSame1_api.ISameEnum1Interface;
import tbSame1.tbSame1_api.AbstractSameEnum1Interface;
import tbSame1.tbSame1_api.ISameEnum1InterfaceEventListener;
import tbSame1.tbSame1_api.Enum1;


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


public class SameEnum1InterfaceService extends AbstractSameEnum1Interface {

    private final static String TAG = "SameEnum1InterfaceService";
    private static boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Enum1 m_prop1 = Enum1.Value1;

    public SameEnum1InterfaceService()
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
    public void onSig1(Enum1 param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }

}