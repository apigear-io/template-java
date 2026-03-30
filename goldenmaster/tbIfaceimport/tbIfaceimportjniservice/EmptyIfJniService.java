package tbIfaceimport.tbIfaceimportjniservice;

import android.util.Log;

import tbIfaceimport.tbIfaceimport_api.IEmptyIf;
import tbIfaceimport.tbIfaceimport_api.AbstractEmptyIf;
import tbIfaceimport.tbIfaceimport_api.IEmptyIfEventListener;
import tbIfaceimport.tbIfaceimport_android_messenger.Conversions;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;


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

    // Native methods — use array types for JNI compatibility

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    // Callbacks from native — receive arrays, convert to List and fire events

}
