package tbRefIfaces.tbRefIfacesjniservice;

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


public class SimpleLocalIfJniService extends AbstractSimpleLocalIf {


    private final static String TAG = "SimpleLocalIfJniService";
    private static boolean isServiceReady = false;
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

    public SimpleLocalIfJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setIntProperty(int intProperty)
    {
        Log.i(TAG, "request setIntProperty called, will call native ");
        nativeSetIntProperty(intProperty);
    }

    @Override
    public int getIntProperty()
    {
        Log.i(TAG, "request getIntProperty called, will call native ");
        return nativeGetIntProperty();
    }

  
    // methods

    @Override
    public int intMethod(int param) {
        Log.w(TAG, "request method intMethod called, will call native");
        return nativeIntMethod(param);
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

    // Called on Native Impl Service
    private native void nativeSetIntProperty(int intProperty);
    private native int nativeGetIntProperty();
  
    // methods
    private native int nativeIntMethod(int param);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    //In theory event listener interface
    public void onIntPropertyChanged(int newValue)
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
