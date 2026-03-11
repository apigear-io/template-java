package tbIfaceimport.tbIfaceimportjniservice;

import android.util.Log;

import tbIfaceimport.tbIfaceimport_api.IEmptyIf;
import tbIfaceimport.tbIfaceimport_api.AbstractEmptyIf;
import tbIfaceimport.tbIfaceimport_api.IEmptyIfEventListener;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class EmptyIfJniService extends AbstractEmptyIf {


    private final static String TAG = "EmptyIfJniService";
    private static volatile boolean isServiceReady = false;
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingFutures
        = new ConcurrentHashMap<>();

    public EmptyIfJniService()
    {
        fire_readyStatusChanged(true);
    }
    // methods

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    // methods (async, returns false if native service unavailable)

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
        if (!value) {
            cancelAllPending();
        }
    }

    public void cancelAllPending() {
        for (String callId : pendingFutures.keySet()) {
            CompletableFuture<?> future = pendingFutures.remove(callId);
            if (future != null) {
                future.completeExceptionally(
                    new IllegalStateException("Service disconnected"));
            }
        }
    }

    // Operation result callbacks (called by native C++ continuations)

    //In theory event listener interface

}
