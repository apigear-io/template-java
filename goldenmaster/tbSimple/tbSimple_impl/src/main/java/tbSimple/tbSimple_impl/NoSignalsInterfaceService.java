package tbSimple.tbSimple_impl;

import android.os.Messenger;
import android.util.Log;

import tbSimple.tbSimple_api.INoSignalsInterface;
import tbSimple.tbSimple_api.AbstractNoSignalsInterface;
import tbSimple.tbSimple_api.INoSignalsInterfaceEventListener;


import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.Arrays;


public class NoSignalsInterfaceService extends AbstractNoSignalsInterface {

    private final static String TAG = "NoSignalsInterfaceService";
    private static boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);
    private boolean m_propBool = false;
    private int m_propInt = 0;

    public NoSignalsInterfaceService()
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
    public void funcVoid() {
        Log.w(TAG, "request method funcVoid called, returnig default");
        return ;
    }

    @Override
    public  CompletableFuture<Void> funcVoidAsync() {
        return CompletableFuture.runAsync(
                () -> { funcVoid(); },
                executor);
    }

    @Override
    public boolean funcBool(boolean paramBool) {
        Log.w(TAG, "request method funcBool called, returnig default");
        return false;
    }

    @Override
    public  CompletableFuture<Boolean> funcBoolAsync(boolean paramBool) {
        return CompletableFuture.supplyAsync(
                () -> {return funcBool(paramBool); },
                executor);
    }    

    @Override
    public boolean _isReady() {
        return isServiceReady;
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

}