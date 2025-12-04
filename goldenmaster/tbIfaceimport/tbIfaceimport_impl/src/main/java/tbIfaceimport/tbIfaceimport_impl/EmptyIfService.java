package tbIfaceimport.tbIfaceimport_impl;

import android.os.Messenger;
import android.util.Log;

import tbIfaceimport.tbIfaceimport_api.IEmptyIf;
import tbIfaceimport.tbIfaceimport_api.AbstractEmptyIf;
import tbIfaceimport.tbIfaceimport_api.IEmptyIfEventListener;


import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.Arrays;


public class EmptyIfService extends AbstractEmptyIf {

    private final static String TAG = "EmptyIfService";
    private static boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public EmptyIfService()
    {
        fire_readyStatusChanged(true);
    }
    // methods    

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    //In theory event listener interface

}