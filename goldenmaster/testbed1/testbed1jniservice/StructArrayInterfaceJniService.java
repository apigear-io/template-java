package testbed1.testbed1jniservice;

import android.util.Log;

import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_api.AbstractStructArrayInterface;
import testbed1.testbed1_api.IStructArrayInterfaceEventListener;
import testbed1.testbed1_api.Enum0;
import testbed1.testbed1_android_messenger.Enum0Parcelable;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_android_messenger.StructBoolParcelable;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_android_messenger.StructFloatParcelable;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_android_messenger.StructIntParcelable;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_android_messenger.StructStringParcelable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class StructArrayInterfaceJniService extends AbstractStructArrayInterface {


    private final static String TAG = "StructArrayInterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingFutures
        = new ConcurrentHashMap<>();

    public StructArrayInterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setPropBool(StructBool[] propBool)
    {
        Log.i(TAG, "request setPropBool called, will call native ");
        nativeSetPropBool(propBool);
    }

    @Override
    public StructBool[] getPropBool()
    {
        Log.i(TAG, "request getPropBool called, will call native ");
        return nativeGetPropBool();
    }

  
    @Override
    public void setPropInt(StructInt[] propInt)
    {
        Log.i(TAG, "request setPropInt called, will call native ");
        nativeSetPropInt(propInt);
    }

    @Override
    public StructInt[] getPropInt()
    {
        Log.i(TAG, "request getPropInt called, will call native ");
        return nativeGetPropInt();
    }

  
    @Override
    public void setPropFloat(StructFloat[] propFloat)
    {
        Log.i(TAG, "request setPropFloat called, will call native ");
        nativeSetPropFloat(propFloat);
    }

    @Override
    public StructFloat[] getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called, will call native ");
        return nativeGetPropFloat();
    }

  
    @Override
    public void setPropString(StructString[] propString)
    {
        Log.i(TAG, "request setPropString called, will call native ");
        nativeSetPropString(propString);
    }

    @Override
    public StructString[] getPropString()
    {
        Log.i(TAG, "request getPropString called, will call native ");
        return nativeGetPropString();
    }

  
    @Override
    public void setPropEnum(Enum0[] propEnum)
    {
        Log.i(TAG, "request setPropEnum called, will call native ");
        nativeSetPropEnum(propEnum);
    }

    @Override
    public Enum0[] getPropEnum()
    {
        Log.i(TAG, "request getPropEnum called, will call native ");
        return nativeGetPropEnum();
    }

  
    // methods

    @Override
    public StructBool[] funcBool(StructBool[] paramBool) {
        Log.i(TAG, "request method funcBool called");
        try {
            return funcBoolAsync(paramBool).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcBool sync call timed out");
            return new StructBool[]{};
        } catch (Exception e) {
            Log.w(TAG, "funcBool sync call failed: " + e.getMessage());
            return new StructBool[]{};
        }
    }

    @Override
    public CompletableFuture<StructBool[]> funcBoolAsync(StructBool[] paramBool) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFuncBoolAsync(callId, paramBool);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for funcBool"));
        }
        return future;
    }

    @Override
    public StructInt[] funcInt(StructInt[] paramInt) {
        Log.i(TAG, "request method funcInt called");
        try {
            return funcIntAsync(paramInt).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcInt sync call timed out");
            return new StructInt[]{};
        } catch (Exception e) {
            Log.w(TAG, "funcInt sync call failed: " + e.getMessage());
            return new StructInt[]{};
        }
    }

    @Override
    public CompletableFuture<StructInt[]> funcIntAsync(StructInt[] paramInt) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFuncIntAsync(callId, paramInt);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for funcInt"));
        }
        return future;
    }

    @Override
    public StructFloat[] funcFloat(StructFloat[] paramFloat) {
        Log.i(TAG, "request method funcFloat called");
        try {
            return funcFloatAsync(paramFloat).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcFloat sync call timed out");
            return new StructFloat[]{};
        } catch (Exception e) {
            Log.w(TAG, "funcFloat sync call failed: " + e.getMessage());
            return new StructFloat[]{};
        }
    }

    @Override
    public CompletableFuture<StructFloat[]> funcFloatAsync(StructFloat[] paramFloat) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFuncFloatAsync(callId, paramFloat);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for funcFloat"));
        }
        return future;
    }

    @Override
    public StructString[] funcString(StructString[] paramString) {
        Log.i(TAG, "request method funcString called");
        try {
            return funcStringAsync(paramString).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcString sync call timed out");
            return new StructString[]{};
        } catch (Exception e) {
            Log.w(TAG, "funcString sync call failed: " + e.getMessage());
            return new StructString[]{};
        }
    }

    @Override
    public CompletableFuture<StructString[]> funcStringAsync(StructString[] paramString) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFuncStringAsync(callId, paramString);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for funcString"));
        }
        return future;
    }

    @Override
    public Enum0[] funcEnum(Enum0[] paramEnum) {
        Log.i(TAG, "request method funcEnum called");
        try {
            return funcEnumAsync(paramEnum).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcEnum sync call timed out");
            return new Enum0[]{};
        } catch (Exception e) {
            Log.w(TAG, "funcEnum sync call failed: " + e.getMessage());
            return new Enum0[]{};
        }
    }

    @Override
    public CompletableFuture<Enum0[]> funcEnumAsync(Enum0[] paramEnum) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFuncEnumAsync(callId, paramEnum);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for funcEnum"));
        }
        return future;
    }

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    private native void nativeSetPropBool(StructBool[] propBool);
    private native StructBool[] nativeGetPropBool();
  
    private native void nativeSetPropInt(StructInt[] propInt);
    private native StructInt[] nativeGetPropInt();
  
    private native void nativeSetPropFloat(StructFloat[] propFloat);
    private native StructFloat[] nativeGetPropFloat();
  
    private native void nativeSetPropString(StructString[] propString);
    private native StructString[] nativeGetPropString();
  
    private native void nativeSetPropEnum(Enum0[] propEnum);
    private native Enum0[] nativeGetPropEnum();
  
    // methods (async, returns false if native service unavailable)
    private native boolean nativeFuncBoolAsync(String callId, StructBool[] paramBool);
    private native boolean nativeFuncIntAsync(String callId, StructInt[] paramInt);
    private native boolean nativeFuncFloatAsync(String callId, StructFloat[] paramFloat);
    private native boolean nativeFuncStringAsync(String callId, StructString[] paramString);
    private native boolean nativeFuncEnumAsync(String callId, Enum0[] paramEnum);

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
    public void onFuncBoolResult(StructBool[] result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFuncIntResult(StructInt[] result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFuncFloatResult(StructFloat[] result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFuncStringResult(StructString[] result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFuncEnumResult(Enum0[] result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }

    //In theory event listener interface
    public void onPropBoolChanged(StructBool[] newValue)
    {
         Log.i(TAG, "onPropBoolChanged, will pass notification to all listeners");
         firePropBoolChanged(newValue);
    }
    public void onPropIntChanged(StructInt[] newValue)
    {
         Log.i(TAG, "onPropIntChanged, will pass notification to all listeners");
         firePropIntChanged(newValue);
    }
    public void onPropFloatChanged(StructFloat[] newValue)
    {
         Log.i(TAG, "onPropFloatChanged, will pass notification to all listeners");
         firePropFloatChanged(newValue);
    }
    public void onPropStringChanged(StructString[] newValue)
    {
         Log.i(TAG, "onPropStringChanged, will pass notification to all listeners");
         firePropStringChanged(newValue);
    }
    public void onPropEnumChanged(Enum0[] newValue)
    {
         Log.i(TAG, "onPropEnumChanged, will pass notification to all listeners");
         firePropEnumChanged(newValue);
    }
    public void onSigBool(StructBool[] paramBool)
    {
        Log.i(TAG, "onSigBool, will pass notification to all listeners");
        fireSigBool(paramBool);
    }
    public void onSigInt(StructInt[] paramInt)
    {
        Log.i(TAG, "onSigInt, will pass notification to all listeners");
        fireSigInt(paramInt);
    }
    public void onSigFloat(StructFloat[] paramFloat)
    {
        Log.i(TAG, "onSigFloat, will pass notification to all listeners");
        fireSigFloat(paramFloat);
    }
    public void onSigString(StructString[] paramString)
    {
        Log.i(TAG, "onSigString, will pass notification to all listeners");
        fireSigString(paramString);
    }
    public void onSigEnum(Enum0[] paramEnum)
    {
        Log.i(TAG, "onSigEnum, will pass notification to all listeners");
        fireSigEnum(paramEnum);
    }

}
