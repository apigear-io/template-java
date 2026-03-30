package tbRefIfaces.tbRefIfacesjniservice;

import android.util.Log;

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_api.AbstractSimpleLocalIf;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIfEventListener;
import tbRefIfaces.tbRefIfaces_android_messenger.Conversions;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;


public class SimpleLocalIfJniService extends AbstractSimpleLocalIf {


    private final static String TAG = "SimpleLocalIfJniService";
    private static volatile boolean isServiceReady = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

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
        Log.i(TAG, "request method intMethod called, will call native");
        return nativeIntMethod(param);
    }

    @Override
    public  CompletableFuture<Integer> intMethodAsync(int param) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return intMethod(param); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Integer> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

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

    // Native methods — use array types for JNI compatibility
    private native void nativeSetIntProperty(int intProperty);
    private native int nativeGetIntProperty();
  
    private native int nativeIntMethod(int param);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    // Callbacks from native — receive arrays, convert to List and fire events
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
