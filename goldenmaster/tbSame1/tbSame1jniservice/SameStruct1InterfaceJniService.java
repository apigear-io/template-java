package tbSame1.tbSame1jniservice;

import android.os.Messenger;
import android.util.Log;

import tbSame1.tbSame1_api.ISameStruct1Interface;
import tbSame1.tbSame1_api.AbstractSameStruct1Interface;
import tbSame1.tbSame1_api.ISameStruct1InterfaceEventListener;
import tbSame1.tbSame1_api.Struct1;
import tbSame1.tbSame1_android_messenger.Struct1Parcelable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class SameStruct1InterfaceJniService extends AbstractSameStruct1Interface {


    private final static String TAG = "SameStruct1InterfaceJniService";
    private static boolean isServiceReady = false;
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

    public SameStruct1InterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setProp1(Struct1 prop1)
    {
        Log.i(TAG, "request setProp1 called, will call native ");
        nativeSetProp1(prop1);
    }

    @Override
    public Struct1 getProp1()
    {
        Log.i(TAG, "request getProp1 called, will call native ");
        return nativeGetProp1();
    }

  
    // methods

    @Override
    public Struct1 func1(Struct1 param1) {
        Log.w(TAG, "request method func1 called, will call native");
        return nativeFunc1(param1);
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

    // Called on Native Impl Service
    private native void nativeSetProp1(Struct1 prop1);
    private native Struct1 nativeGetProp1();
  
    // methods
    private native Struct1 nativeFunc1(Struct1 param1);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    //In theory event listener interface
    public void onProp1Changed(Struct1 newValue)
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
