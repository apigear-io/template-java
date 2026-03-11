package tbEnum.tbEnumjniservice;

import android.util.Log;

import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_api.AbstractEnumInterface;
import tbEnum.tbEnum_api.IEnumInterfaceEventListener;
import tbEnum.tbEnum_api.Enum0;
import tbEnum.tbEnum_android_messenger.Enum0Parcelable;
import tbEnum.tbEnum_api.Enum1;
import tbEnum.tbEnum_android_messenger.Enum1Parcelable;
import tbEnum.tbEnum_api.Enum2;
import tbEnum.tbEnum_android_messenger.Enum2Parcelable;
import tbEnum.tbEnum_api.Enum3;
import tbEnum.tbEnum_android_messenger.Enum3Parcelable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class EnumInterfaceJniService extends AbstractEnumInterface {


    private final static String TAG = "EnumInterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingFutures
        = new ConcurrentHashMap<>();

    public EnumInterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setProp0(Enum0 prop0)
    {
        Log.i(TAG, "request setProp0 called, will call native ");
        nativeSetProp0(prop0);
    }

    @Override
    public Enum0 getProp0()
    {
        Log.i(TAG, "request getProp0 called, will call native ");
        return nativeGetProp0();
    }

  
    @Override
    public void setProp1(Enum1 prop1)
    {
        Log.i(TAG, "request setProp1 called, will call native ");
        nativeSetProp1(prop1);
    }

    @Override
    public Enum1 getProp1()
    {
        Log.i(TAG, "request getProp1 called, will call native ");
        return nativeGetProp1();
    }

  
    @Override
    public void setProp2(Enum2 prop2)
    {
        Log.i(TAG, "request setProp2 called, will call native ");
        nativeSetProp2(prop2);
    }

    @Override
    public Enum2 getProp2()
    {
        Log.i(TAG, "request getProp2 called, will call native ");
        return nativeGetProp2();
    }

  
    @Override
    public void setProp3(Enum3 prop3)
    {
        Log.i(TAG, "request setProp3 called, will call native ");
        nativeSetProp3(prop3);
    }

    @Override
    public Enum3 getProp3()
    {
        Log.i(TAG, "request getProp3 called, will call native ");
        return nativeGetProp3();
    }

  
    // methods

    @Override
    public Enum0 func0(Enum0 param0) {
        Log.i(TAG, "request method func0 called");
        try {
            return func0Async(param0).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "func0 sync call timed out");
            return Enum0.Value0;
        } catch (Exception e) {
            Log.w(TAG, "func0 sync call failed: " + e.getMessage());
            return Enum0.Value0;
        }
    }

    @Override
    public CompletableFuture<Enum0> func0Async(Enum0 param0) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFunc0Async(callId, param0);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for func0"));
        }
        return future;
    }

    @Override
    public Enum1 func1(Enum1 param1) {
        Log.i(TAG, "request method func1 called");
        try {
            return func1Async(param1).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "func1 sync call timed out");
            return Enum1.Value1;
        } catch (Exception e) {
            Log.w(TAG, "func1 sync call failed: " + e.getMessage());
            return Enum1.Value1;
        }
    }

    @Override
    public CompletableFuture<Enum1> func1Async(Enum1 param1) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFunc1Async(callId, param1);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for func1"));
        }
        return future;
    }

    @Override
    public Enum2 func2(Enum2 param2) {
        Log.i(TAG, "request method func2 called");
        try {
            return func2Async(param2).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "func2 sync call timed out");
            return Enum2.Value2;
        } catch (Exception e) {
            Log.w(TAG, "func2 sync call failed: " + e.getMessage());
            return Enum2.Value2;
        }
    }

    @Override
    public CompletableFuture<Enum2> func2Async(Enum2 param2) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFunc2Async(callId, param2);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for func2"));
        }
        return future;
    }

    @Override
    public Enum3 func3(Enum3 param3) {
        Log.i(TAG, "request method func3 called");
        try {
            return func3Async(param3).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "func3 sync call timed out");
            return Enum3.Value3;
        } catch (Exception e) {
            Log.w(TAG, "func3 sync call failed: " + e.getMessage());
            return Enum3.Value3;
        }
    }

    @Override
    public CompletableFuture<Enum3> func3Async(Enum3 param3) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFunc3Async(callId, param3);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for func3"));
        }
        return future;
    }

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    private native void nativeSetProp0(Enum0 prop0);
    private native Enum0 nativeGetProp0();
  
    private native void nativeSetProp1(Enum1 prop1);
    private native Enum1 nativeGetProp1();
  
    private native void nativeSetProp2(Enum2 prop2);
    private native Enum2 nativeGetProp2();
  
    private native void nativeSetProp3(Enum3 prop3);
    private native Enum3 nativeGetProp3();
  
    // methods (async, returns false if native service unavailable)
    private native boolean nativeFunc0Async(String callId, Enum0 param0);
    private native boolean nativeFunc1Async(String callId, Enum1 param1);
    private native boolean nativeFunc2Async(String callId, Enum2 param2);
    private native boolean nativeFunc3Async(String callId, Enum3 param3);

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
    public void onFunc0Result(Enum0 result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFunc1Result(Enum1 result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFunc2Result(Enum2 result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFunc3Result(Enum3 result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }

    //In theory event listener interface
    public void onProp0Changed(Enum0 newValue)
    {
         Log.i(TAG, "onProp0Changed, will pass notification to all listeners");
         fireProp0Changed(newValue);
    }
    public void onProp1Changed(Enum1 newValue)
    {
         Log.i(TAG, "onProp1Changed, will pass notification to all listeners");
         fireProp1Changed(newValue);
    }
    public void onProp2Changed(Enum2 newValue)
    {
         Log.i(TAG, "onProp2Changed, will pass notification to all listeners");
         fireProp2Changed(newValue);
    }
    public void onProp3Changed(Enum3 newValue)
    {
         Log.i(TAG, "onProp3Changed, will pass notification to all listeners");
         fireProp3Changed(newValue);
    }
    public void onSig0(Enum0 param0)
    {
        Log.i(TAG, "onSig0, will pass notification to all listeners");
        fireSig0(param0);
    }
    public void onSig1(Enum1 param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }
    public void onSig2(Enum2 param2)
    {
        Log.i(TAG, "onSig2, will pass notification to all listeners");
        fireSig2(param2);
    }
    public void onSig3(Enum3 param3)
    {
        Log.i(TAG, "onSig3, will pass notification to all listeners");
        fireSig3(param3);
    }

}
