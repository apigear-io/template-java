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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.Arrays;


public class SimpleLocalIfService extends AbstractSimpleLocalIf {

    private final static String TAG = "SimpleLocalIfService";
    private static boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
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
        return CompletableFuture.supplyAsync(
                () -> {return intMethod(param); },
                executor);
    }    

    @Override
    public boolean _isReady() {
        return isServiceReady;
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