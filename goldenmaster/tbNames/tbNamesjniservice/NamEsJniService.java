package tbNames.tbNamesjniservice;

import android.util.Log;

import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_api.AbstractNamEs;
import tbNames.tbNames_api.INamEsEventListener;
import tbNames.tbNames_api.EnumWithUnderScores;
import tbNames.tbNames_android_messenger.EnumWithUnderScoresParcelable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class NamEsJniService extends AbstractNamEs {


    private final static String TAG = "NamEsJniService";
    private static volatile boolean isServiceReady = false;
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingFutures
        = new ConcurrentHashMap<>();

    public NamEsJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setSwitch(boolean Switch)
    {
        Log.i(TAG, "request setSwitch called, will call native ");
        nativeSetSwitch(Switch);
    }

    @Override
    public boolean getSwitch()
    {
        Log.i(TAG, "request getSwitch called, will call native ");
        return nativeGetSwitch();
    }

  
    @Override
    public void setSomeProperty(int SOME_PROPERTY)
    {
        Log.i(TAG, "request setSomeProperty called, will call native ");
        nativeSetSomeProperty(SOME_PROPERTY);
    }

    @Override
    public int getSomeProperty()
    {
        Log.i(TAG, "request getSomeProperty called, will call native ");
        return nativeGetSomeProperty();
    }

  
    @Override
    public void setSomePoperty2(int Some_Poperty2)
    {
        Log.i(TAG, "request setSomePoperty2 called, will call native ");
        nativeSetSomePoperty2(Some_Poperty2);
    }

    @Override
    public int getSomePoperty2()
    {
        Log.i(TAG, "request getSomePoperty2 called, will call native ");
        return nativeGetSomePoperty2();
    }

  
    @Override
    public void setEnumProperty(EnumWithUnderScores enum_property)
    {
        Log.i(TAG, "request setEnumProperty called, will call native ");
        nativeSetEnumProperty(enum_property);
    }

    @Override
    public EnumWithUnderScores getEnumProperty()
    {
        Log.i(TAG, "request getEnumProperty called, will call native ");
        return nativeGetEnumProperty();
    }

  
    // methods

    @Override
    public void someFunction(boolean SOME_PARAM) {
        Log.i(TAG, "request method someFunction called");
        try {
            someFunctionAsync(SOME_PARAM).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "someFunction sync call timed out");
        } catch (Exception e) {
            Log.w(TAG, "someFunction sync call failed: " + e.getMessage());
        }
    }

    @Override
    public CompletableFuture<Void> someFunctionAsync(boolean SOME_PARAM) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Void> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeSomeFunctionAsync(callId, SOME_PARAM);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for someFunction"));
        }
        return future;
    }

    @Override
    public void someFunction2(boolean Some_Param) {
        Log.i(TAG, "request method someFunction2 called");
        try {
            someFunction2Async(Some_Param).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "someFunction2 sync call timed out");
        } catch (Exception e) {
            Log.w(TAG, "someFunction2 sync call failed: " + e.getMessage());
        }
    }

    @Override
    public CompletableFuture<Void> someFunction2Async(boolean Some_Param) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Void> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeSomeFunction2Async(callId, Some_Param);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for someFunction2"));
        }
        return future;
    }

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    private native void nativeSetSwitch(boolean Switch);
    private native boolean nativeGetSwitch();
  
    private native void nativeSetSomeProperty(int SOME_PROPERTY);
    private native int nativeGetSomeProperty();
  
    private native void nativeSetSomePoperty2(int Some_Poperty2);
    private native int nativeGetSomePoperty2();
  
    private native void nativeSetEnumProperty(EnumWithUnderScores enum_property);
    private native EnumWithUnderScores nativeGetEnumProperty();
  
    // methods (async, returns false if native service unavailable)
    private native boolean nativeSomeFunctionAsync(String callId, boolean SOME_PARAM);
    private native boolean nativeSomeFunction2Async(String callId, boolean Some_Param);

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
    public void onSomeFunctionResult(String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Void> typedFuture = (CompletableFuture<Void>) future;
            typedFuture.complete(null);
        }
    }
    public void onSomeFunction2Result(String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Void> typedFuture = (CompletableFuture<Void>) future;
            typedFuture.complete(null);
        }
    }

    //In theory event listener interface
    public void onSwitchChanged(boolean newValue)
    {
         Log.i(TAG, "onSwitchChanged, will pass notification to all listeners");
         fireSwitchChanged(newValue);
    }
    public void onSomePropertyChanged(int newValue)
    {
         Log.i(TAG, "onSomePropertyChanged, will pass notification to all listeners");
         fireSomePropertyChanged(newValue);
    }
    public void onSomePoperty2Changed(int newValue)
    {
         Log.i(TAG, "onSomePoperty2Changed, will pass notification to all listeners");
         fireSomePoperty2Changed(newValue);
    }
    public void onEnumPropertyChanged(EnumWithUnderScores newValue)
    {
         Log.i(TAG, "onEnumPropertyChanged, will pass notification to all listeners");
         fireEnumPropertyChanged(newValue);
    }
    public void onSomeSignal(boolean SOME_PARAM)
    {
        Log.i(TAG, "onSomeSignal, will pass notification to all listeners");
        fireSomeSignal(SOME_PARAM);
    }
    public void onSomeSignal2(boolean Some_Param)
    {
        Log.i(TAG, "onSomeSignal2, will pass notification to all listeners");
        fireSomeSignal2(Some_Param);
    }

}
