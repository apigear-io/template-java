package tbSame1.tbSame1_impl;

import android.os.Messenger;
import android.util.Log;

import tbSame1.tbSame1_api.ISameStruct1Interface;
import tbSame1.tbSame1_api.AbstractSameStruct1Interface;
import tbSame1.tbSame1_api.ISameStruct1InterfaceEventListener;
import tbSame1.tbSame1_api.Struct1;


import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.Arrays;


public class SameStruct1InterfaceService extends AbstractSameStruct1Interface {

    private final static String TAG = "SameStruct1InterfaceService";
    private static boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
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
        return CompletableFuture.supplyAsync(
                () -> {return func1(param1); },
                executor);
    }    

    @Override
    public boolean _isReady() {
        return isServiceReady;
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