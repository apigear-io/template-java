package tbEnum.tbEnum_impl;

import android.os.Messenger;
import android.util.Log;

import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_api.AbstractEnumInterface;
import tbEnum.tbEnum_api.IEnumInterfaceEventListener;
import tbEnum.tbEnum_api.Enum0;
import tbEnum.tbEnum_api.Enum1;
import tbEnum.tbEnum_api.Enum2;
import tbEnum.tbEnum_api.Enum3;


import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.Arrays;


public class EnumInterfaceService extends AbstractEnumInterface {

    private final static String TAG = "EnumInterfaceService";
    private static boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);
    private Enum0 m_prop0 = Enum0.Value0;
    private Enum1 m_prop1 = Enum1.Value1;
    private Enum2 m_prop2 = Enum2.Value2;
    private Enum3 m_prop3 = Enum3.Value3;

    public EnumInterfaceService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setProp0(Enum0 prop0)
    {
        Log.i(TAG, "request setProp0 called ");
        if (m_prop0 != prop0)
        {
            m_prop0 = prop0;
            onProp0Changed(m_prop0);
        }

    }

    @Override
    public Enum0 getProp0()
    {
        Log.i(TAG, "request getProp0 called,");
        return m_prop0;
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

  
    @Override
    public void setProp3(Enum3 prop3)
    {
        Log.i(TAG, "request setProp3 called ");
        if (m_prop3 != prop3)
        {
            m_prop3 = prop3;
            onProp3Changed(m_prop3);
        }

    }

    @Override
    public Enum3 getProp3()
    {
        Log.i(TAG, "request getProp3 called,");
        return m_prop3;
    }

  
    // methods

    @Override
    public Enum0 func0(Enum0 param0) {
        Log.i(TAG, "request method func0 called, returnig default");
        return Enum0.Value0;
    }

    @Override
    public  CompletableFuture<Enum0> func0Async(Enum0 param0) {
        return CompletableFuture.supplyAsync(
                () -> {return func0(param0); },
                executor);
    }

    @Override
    public Enum1 func1(Enum1 param1) {
        Log.i(TAG, "request method func1 called, returnig default");
        return Enum1.Value1;
    }

    @Override
    public  CompletableFuture<Enum1> func1Async(Enum1 param1) {
        return CompletableFuture.supplyAsync(
                () -> {return func1(param1); },
                executor);
    }

    @Override
    public Enum2 func2(Enum2 param2) {
        Log.i(TAG, "request method func2 called, returnig default");
        return Enum2.Value2;
    }

    @Override
    public  CompletableFuture<Enum2> func2Async(Enum2 param2) {
        return CompletableFuture.supplyAsync(
                () -> {return func2(param2); },
                executor);
    }

    @Override
    public Enum3 func3(Enum3 param3) {
        Log.i(TAG, "request method func3 called, returnig default");
        return Enum3.Value3;
    }

    @Override
    public  CompletableFuture<Enum3> func3Async(Enum3 param3) {
        return CompletableFuture.supplyAsync(
                () -> {return func3(param3); },
                executor);
    }    

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    //In theory event listener interface
    private void onProp0Changed(Enum0 newValue)
    {
         Log.i(TAG, "onProp0Changed, will pass notification to all listeners");
         fireProp0Changed(newValue);
    }
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
    private void onProp3Changed(Enum3 newValue)
    {
         Log.i(TAG, "onProp3Changed, will pass notification to all listeners");
         fireProp3Changed(newValue);
    }
    public void onSig0(Enum0 param0)
    {
        Log.i(TAG, "onSig0, will pass notification to all listeners");
        fireSig0(param0);
    }
    public void onSig1(Enum1 param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }
    public void onSig2(Enum2 param2)
    {
        Log.i(TAG, "onSig2, will pass notification to all listeners");
        fireSig2(param2);
    }
    public void onSig3(Enum3 param3)
    {
        Log.i(TAG, "onSig3, will pass notification to all listeners");
        fireSig3(param3);
    }

}