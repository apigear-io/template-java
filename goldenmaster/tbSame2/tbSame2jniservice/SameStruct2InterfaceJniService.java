package tbSame2.tbSame2jniservice;

import android.os.Messenger;
import android.util.Log;

import tbSame2.tbSame2_api.ISameStruct2Interface;
import tbSame2.tbSame2_api.AbstractSameStruct2Interface;
import tbSame2.tbSame2_api.ISameStruct2InterfaceEventListener;
import tbSame2.tbSame2_api.Struct1;
import tbSame2.tbSame2_android_messenger.Struct1Parcelable;
import tbSame2.tbSame2_api.Struct2;
import tbSame2.tbSame2_android_messenger.Struct2Parcelable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class SameStruct2InterfaceJniService extends AbstractSameStruct2Interface {


    private final static String TAG = "SameStruct2InterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SameStruct2InterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setProp1(Struct2 prop1)
    {
        Log.i(TAG, "request setProp1 called, will call native ");
        nativeSetProp1(prop1);
    }

    @Override
    public Struct2 getProp1()
    {
        Log.i(TAG, "request getProp1 called, will call native ");
        return nativeGetProp1();
    }

  
    @Override
    public void setProp2(Struct2 prop2)
    {
        Log.i(TAG, "request setProp2 called, will call native ");
        nativeSetProp2(prop2);
    }

    @Override
    public Struct2 getProp2()
    {
        Log.i(TAG, "request getProp2 called, will call native ");
        return nativeGetProp2();
    }

  
    // methods

    @Override
    public Struct1 func1(Struct1 param1) {
        Log.i(TAG, "request method func1 called, will call native");
        return nativeFunc1(param1);
    }

    @Override
    public  CompletableFuture<Struct1> func1Async(Struct1 param1) {
        return CompletableFuture.supplyAsync(
                () -> {return func1(param1); },
                executor);
    }

    @Override
    public Struct1 func2(Struct1 param1, Struct2 param2) {
        Log.i(TAG, "request method func2 called, will call native");
        return nativeFunc2(param1, param2);
    }

    @Override
    public  CompletableFuture<Struct1> func2Async(Struct1 param1, Struct2 param2) {
        return CompletableFuture.supplyAsync(
                () -> {return func2(param1, param2); },
                executor);
    }    

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    private native void nativeSetProp1(Struct2 prop1);
    private native Struct2 nativeGetProp1();
  
    private native void nativeSetProp2(Struct2 prop2);
    private native Struct2 nativeGetProp2();
  
    // methods
    private native Struct1 nativeFunc1(Struct1 param1);
    private native Struct1 nativeFunc2(Struct1 param1, Struct2 param2);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    //In theory event listener interface
    public void onProp1Changed(Struct2 newValue)
    {
         Log.i(TAG, "onProp1Changed, will pass notification to all listeners");
         fireProp1Changed(newValue);
    }
    public void onProp2Changed(Struct2 newValue)
    {
         Log.i(TAG, "onProp2Changed, will pass notification to all listeners");
         fireProp2Changed(newValue);
    }
    public void onSig1(Struct1 param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }
    public void onSig2(Struct1 param1, Struct2 param2)
    {
        Log.i(TAG, "onSig2, will pass notification to all listeners");
        fireSig2(param1, param2);
    }

}
