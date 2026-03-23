package tbIfaceimport.tbIfaceimportjniservice;

import android.os.Messenger;
import android.util.Log;

import tbIfaceimport.tbIfaceimport_api.IEmptyIf;
import tbIfaceimport.tbIfaceimport_api.AbstractEmptyIf;
import tbIfaceimport.tbIfaceimport_api.IEmptyIfEventListener;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class EmptyIfJniService extends AbstractEmptyIf {


    private final static String TAG = "EmptyIfJniService";
    private static volatile boolean isServiceReady = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public EmptyIfJniService()
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
