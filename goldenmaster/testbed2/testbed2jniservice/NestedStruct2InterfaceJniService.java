package testbed2.testbed2jniservice;

import android.os.Messenger;
import android.util.Log;

import testbed2.testbed2_api.INestedStruct2Interface;
import testbed2.testbed2_api.AbstractNestedStruct2Interface;
import testbed2.testbed2_api.INestedStruct2InterfaceEventListener;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_android_messenger.NestedStruct1Parcelable;
import testbed2.testbed2_api.NestedStruct2;
import testbed2.testbed2_android_messenger.NestedStruct2Parcelable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class NestedStruct2InterfaceJniService extends AbstractNestedStruct2Interface {


    private final static String TAG = "NestedStruct2InterfaceJniService";
    private static boolean isServiceReady = false;
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

    public NestedStruct2InterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setProp1(NestedStruct1 prop1)
    {
        Log.i(TAG, "request setProp1 called, will call native ");
        nativeSetProp1(prop1);
    }

    @Override
    public NestedStruct1 getProp1()
    {
        Log.i(TAG, "request getProp1 called, will call native ");
        return nativeGetProp1();
    }

  
    @Override
    public void setProp2(NestedStruct2 prop2)
    {
        Log.i(TAG, "request setProp2 called, will call native ");
        nativeSetProp2(prop2);
    }

    @Override
    public NestedStruct2 getProp2()
    {
        Log.i(TAG, "request getProp2 called, will call native ");
        return nativeGetProp2();
    }

  
    // methods

    @Override
    public NestedStruct1 func1(NestedStruct1 param1) {
        Log.i(TAG, "request method func1 called, will call native");
        return nativeFunc1(param1);
    }

    @Override
    public  CompletableFuture<NestedStruct1> func1Async(NestedStruct1 param1) {
        return CompletableFuture.supplyAsync(
                () -> {return func1(param1); },
                executor);
    }

    @Override
    public NestedStruct1 func2(NestedStruct1 param1, NestedStruct2 param2) {
        Log.i(TAG, "request method func2 called, will call native");
        return nativeFunc2(param1, param2);
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

    // Called on Native Impl Service
    private native void nativeSetProp1(NestedStruct1 prop1);
    private native NestedStruct1 nativeGetProp1();
  
    private native void nativeSetProp2(NestedStruct2 prop2);
    private native NestedStruct2 nativeGetProp2();
  
    // methods
    private native NestedStruct1 nativeFunc1(NestedStruct1 param1);
    private native NestedStruct1 nativeFunc2(NestedStruct1 param1, NestedStruct2 param2);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    //In theory event listener interface
    public void onProp1Changed(NestedStruct1 newValue)
    {
         Log.i(TAG, "onProp1Changed, will pass notification to all listeners");
         fireProp1Changed(newValue);
    }
    public void onProp2Changed(NestedStruct2 newValue)
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
