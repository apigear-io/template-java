package tbRefIfaces.tbRefIfaces_impl;

import android.os.Messenger;
import android.util.Log;

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_api.AbstractSimpleLocalIf;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIfEventListener;


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


public class SimpleLocalIfService extends AbstractSimpleLocalIf {

    private final static String TAG = "SimpleLocalIfService";
    private static boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private int m_intProperty = 0;

    public SimpleLocalIfService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setIntProperty(int intProperty)
    {
        Log.i(TAG, "request setIntProperty called ");
        if (m_intProperty != intProperty)
        {
            m_intProperty = intProperty;
            onIntPropertyChanged(m_intProperty);
        }

    }

    @Override
    public int getIntProperty()
    {
        Log.i(TAG, "request getIntProperty called,");
        return m_intProperty;
    }

  
    // methods

    @Override
    public int intMethod(int param) {
        Log.i(TAG, "request method intMethod called, returnig default");
        return 0;
    }

    @Override
    public  CompletableFuture<Integer> intMethodAsync(int param) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return intMethod(param); },
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
    private void onIntPropertyChanged(int newValue)
    {
         Log.i(TAG, "onIntPropertyChanged, will pass notification to all listeners");
         fireIntPropertyChanged(newValue);
    }
    public void onIntSignal(int param)
    {
        Log.i(TAG, "onIntSignal, will pass notification to all listeners");
        fireIntSignal(param);
    }

}