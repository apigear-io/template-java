package tbSimple.tbSimple_impl;

import android.os.Messenger;
import android.util.Log;

import tbSimple.tbSimple_api.INoOperationsInterface;
import tbSimple.tbSimple_api.AbstractNoOperationsInterface;
import tbSimple.tbSimple_api.INoOperationsInterfaceEventListener;


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


public class NoOperationsInterfaceService extends AbstractNoOperationsInterface {

    private final static String TAG = "NoOperationsInterfaceService";
    private volatile boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private boolean m_propBool = false;
    private int m_propInt = 0;

    public NoOperationsInterfaceService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setPropBool(boolean propBool)
    {
        Log.i(TAG, "request setPropBool called ");
        if (m_propBool != propBool)
        {
            m_propBool = propBool;
            onPropBoolChanged(m_propBool);
        }

    }

    @Override
    public boolean getPropBool()
    {
        Log.i(TAG, "request getPropBool called,");
        return m_propBool;
    }

  
    @Override
    public void setPropInt(int propInt)
    {
        Log.i(TAG, "request setPropInt called ");
        if (m_propInt != propInt)
        {
            m_propInt = propInt;
            onPropIntChanged(m_propInt);
        }

    }

    @Override
    public int getPropInt()
    {
        Log.i(TAG, "request getPropInt called,");
        return m_propInt;
    }

  
    // methods

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
    private void onPropBoolChanged(boolean newValue)
    {
         Log.i(TAG, "onPropBoolChanged, will pass notification to all listeners");
         firePropBoolChanged(newValue);
    }
    private void onPropIntChanged(int newValue)
    {
         Log.i(TAG, "onPropIntChanged, will pass notification to all listeners");
         firePropIntChanged(newValue);
    }
    public void onSigVoid()
    {
        Log.i(TAG, "onSigVoid, will pass notification to all listeners");
        fireSigVoid();
    }
    public void onSigBool(boolean paramBool)
    {
        Log.i(TAG, "onSigBool, will pass notification to all listeners");
        fireSigBool(paramBool);
    }

}