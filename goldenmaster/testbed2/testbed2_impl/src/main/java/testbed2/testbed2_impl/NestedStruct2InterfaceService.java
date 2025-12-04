package testbed2.testbed2_impl;

import android.os.Messenger;
import android.util.Log;

import testbed2.testbed2_api.INestedStruct2Interface;
import testbed2.testbed2_api.AbstractNestedStruct2Interface;
import testbed2.testbed2_api.INestedStruct2InterfaceEventListener;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_api.NestedStruct2;


import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.Arrays;


public class NestedStruct2InterfaceService extends AbstractNestedStruct2Interface {

    private final static String TAG = "NestedStruct2InterfaceService";
    private static boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private NestedStruct1 m_prop1 = new NestedStruct1();
    private NestedStruct2 m_prop2 = new NestedStruct2();

    public NestedStruct2InterfaceService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setProp1(NestedStruct1 prop1)
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
    public NestedStruct1 getProp1()
    {
        Log.i(TAG, "request getProp1 called,");
        return m_prop1;
    }

  
    @Override
    public void setProp2(NestedStruct2 prop2)
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
    public NestedStruct2 getProp2()
    {
        Log.i(TAG, "request getProp2 called,");
        return m_prop2;
    }

  
    // methods

    @Override
    public NestedStruct1 func1(NestedStruct1 param1) {
        Log.i(TAG, "request method func1 called, returnig default");
        return new NestedStruct1();
    }

    @Override
    public  CompletableFuture<NestedStruct1> func1Async(NestedStruct1 param1) {
        return CompletableFuture.supplyAsync(
                () -> {return func1(param1); },
                executor);
    }

    @Override
    public NestedStruct1 func2(NestedStruct1 param1, NestedStruct2 param2) {
        Log.i(TAG, "request method func2 called, returnig default");
        return new NestedStruct1();
    }

    @Override
    public  CompletableFuture<NestedStruct1> func2Async(NestedStruct1 param1, NestedStruct2 param2) {
        return CompletableFuture.supplyAsync(
                () -> {return func2(param1, param2); },
                executor);
    }    

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    //In theory event listener interface
    private void onProp1Changed(NestedStruct1 newValue)
    {
         Log.i(TAG, "onProp1Changed, will pass notification to all listeners");
         fireProp1Changed(newValue);
    }
    private void onProp2Changed(NestedStruct2 newValue)
    {
         Log.i(TAG, "onProp2Changed, will pass notification to all listeners");
         fireProp2Changed(newValue);
    }
    public void onSig1(NestedStruct1 param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }
    public void onSig2(NestedStruct1 param1, NestedStruct2 param2)
    {
        Log.i(TAG, "onSig2, will pass notification to all listeners");
        fireSig2(param1, param2);
    }

}