package tbSimple.tbSimplejniservice;

import android.os.Messenger;
import android.util.Log;

import tbSimple.tbSimple_api.IEmptyInterface;
import tbSimple.tbSimple_api.AbstractEmptyInterface;
import tbSimple.tbSimple_api.IEmptyInterfaceEventListener;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class EmptyInterfaceJniService extends AbstractEmptyInterface {


    private final static String TAG = "EmptyInterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public EmptyInterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    // methods    

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

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    //In theory event listener interface

}
