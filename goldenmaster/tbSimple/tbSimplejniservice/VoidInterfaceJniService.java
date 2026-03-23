package tbSimple.tbSimplejniservice;

import android.os.Messenger;
import android.util.Log;

import tbSimple.tbSimple_api.IVoidInterface;
import tbSimple.tbSimple_api.AbstractVoidInterface;
import tbSimple.tbSimple_api.IVoidInterfaceEventListener;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class VoidInterfaceJniService extends AbstractVoidInterface {


    private final static String TAG = "VoidInterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public VoidInterfaceJniService()
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

    // Called on Native Impl Service
    // methods
    private native void nativeFuncVoid();

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    //In theory event listener interface
    public void onSigVoid()
    {
        Log.i(TAG, "onSigVoid, will pass notification to all listeners");
        fireSigVoid();
    }

}
