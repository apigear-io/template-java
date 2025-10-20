package tbSimple.tbSimplejniservice;

import android.os.Messenger;
import android.util.Log;

import tbSimple.tbSimple_api.IEmptyInterface;
import tbSimple.tbSimple_api.AbstractEmptyInterface;
import tbSimple.tbSimple_api.IEmptyInterfaceEventListener;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class EmptyInterfaceJniService extends AbstractEmptyInterface {


    private final static String TAG = "EmptyInterfaceJniService";
    private static boolean isServiceReady = false;
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

    public EmptyInterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    // methods    

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    // methods

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    //In theory event listener interface

}
