package tbSimple.tbSimplejniservice;

import android.os.Messenger;
import android.util.Log;

import tbSimple.tbSimple_api.INoPropertiesInterface;
import tbSimple.tbSimple_api.AbstractNoPropertiesInterface;
import tbSimple.tbSimple_api.INoPropertiesInterfaceEventListener;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class NoPropertiesInterfaceJniService extends AbstractNoPropertiesInterface {


    private final static String TAG = "NoPropertiesInterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

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
        return CompletableFuture.runAsync(
                () -> { funcVoid(); },
                executor);
    }

    @Override
    public boolean funcBool(boolean paramBool) {
        Log.i(TAG, "request method funcBool called, will call native");
        return nativeFuncBool(paramBool);
    }

    @Override
    public  CompletableFuture<Boolean> funcBoolAsync(boolean paramBool) {
        return CompletableFuture.supplyAsync(
                () -> {return funcBool(paramBool); },
                executor);
    }    

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    // methods
    private native void nativeFuncVoid();
    private native boolean nativeFuncBool(boolean paramBool);

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
    public void onSigBool(boolean paramBool)
    {
        Log.i(TAG, "onSigBool, will pass notification to all listeners");
        fireSigBool(paramBool);
    }

}
