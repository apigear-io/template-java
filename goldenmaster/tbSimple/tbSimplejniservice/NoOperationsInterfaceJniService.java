package tbSimple.tbSimplejniservice;

import android.util.Log;

import tbSimple.tbSimple_api.INoOperationsInterface;
import tbSimple.tbSimple_api.AbstractNoOperationsInterface;
import tbSimple.tbSimple_api.INoOperationsInterfaceEventListener;
import tbSimple.tbSimple_android_messenger.Conversions;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;


public class NoOperationsInterfaceJniService extends AbstractNoOperationsInterface {


    private final static String TAG = "NoOperationsInterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public NoOperationsInterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setPropBool(boolean propBool)
    {
        Log.i(TAG, "request setPropBool called, will call native ");
        nativeSetPropBool(propBool);
    }

    @Override
    public boolean getPropBool()
    {
        Log.i(TAG, "request getPropBool called, will call native ");
        return nativeGetPropBool();
    }

  
    @Override
    public void setPropInt(int propInt)
    {
        Log.i(TAG, "request setPropInt called, will call native ");
        nativeSetPropInt(propInt);
    }

    @Override
    public int getPropInt()
    {
        Log.i(TAG, "request getPropInt called, will call native ");
        return nativeGetPropInt();
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
    private native void nativeSetPropBool(boolean propBool);
    private native boolean nativeGetPropBool();
  
    private native void nativeSetPropInt(int propInt);
    private native int nativeGetPropInt();
  

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    // Callbacks from native — receive arrays, convert to List and fire events
    public void onPropBoolChanged(boolean newValue)
    {
         Log.i(TAG, "onPropBoolChanged, will pass notification to all listeners");
         firePropBoolChanged(newValue);
    }
    public void onPropIntChanged(int newValue)
    {
         Log.i(TAG, "onPropIntChanged, will pass notification to all listeners");
         firePropIntChanged(newValue);
    }
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
