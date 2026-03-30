package tbSimple.tbSimplejniservice;

import android.util.Log;

import tbSimple.tbSimple_api.INoPropertiesInterface;
import tbSimple.tbSimple_api.AbstractNoPropertiesInterface;
import tbSimple.tbSimple_api.INoPropertiesInterfaceEventListener;
import tbSimple.tbSimple_android_messenger.Conversions;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;


public class NoPropertiesInterfaceJniService extends AbstractNoPropertiesInterface {


    private final static String TAG = "NoPropertiesInterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public NoPropertiesInterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    // methods

    @Override
    public void funcVoid() {
        Log.i(TAG, "request method funcVoid called, will call native");
        nativeFuncVoid();
    }

    @Override
    public  CompletableFuture<Void> funcVoidAsync() {
        try {
            return CompletableFuture.runAsync(
                    () -> { funcVoid(); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Void> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public boolean funcBool(boolean paramBool) {
        Log.i(TAG, "request method funcBool called, will call native");
        return nativeFuncBool(paramBool);
    }

    @Override
    public  CompletableFuture<Boolean> funcBoolAsync(boolean paramBool) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return funcBool(paramBool); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<Boolean> f = new CompletableFuture<>();
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
    private native void nativeFuncVoid();
    private native boolean nativeFuncBool(boolean paramBool);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    // Callbacks from native — receive arrays, convert to List and fire events
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
