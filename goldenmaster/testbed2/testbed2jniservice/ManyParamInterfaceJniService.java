package testbed2.testbed2jniservice;

import android.util.Log;

import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_api.AbstractManyParamInterface;
import testbed2.testbed2_api.IManyParamInterfaceEventListener;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class ManyParamInterfaceJniService extends AbstractManyParamInterface {


    private final static String TAG = "ManyParamInterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingFutures
        = new ConcurrentHashMap<>();

    public ManyParamInterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setProp1(int prop1)
    {
        Log.i(TAG, "request setProp1 called, will call native ");
        nativeSetProp1(prop1);
    }

    @Override
    public int getProp1()
    {
        Log.i(TAG, "request getProp1 called, will call native ");
        return nativeGetProp1();
    }

  
    @Override
    public void setProp2(int prop2)
    {
        Log.i(TAG, "request setProp2 called, will call native ");
        nativeSetProp2(prop2);
    }

    @Override
    public int getProp2()
    {
        Log.i(TAG, "request getProp2 called, will call native ");
        return nativeGetProp2();
    }

  
    @Override
    public void setProp3(int prop3)
    {
        Log.i(TAG, "request setProp3 called, will call native ");
        nativeSetProp3(prop3);
    }

    @Override
    public int getProp3()
    {
        Log.i(TAG, "request getProp3 called, will call native ");
        return nativeGetProp3();
    }

  
    @Override
    public void setProp4(int prop4)
    {
        Log.i(TAG, "request setProp4 called, will call native ");
        nativeSetProp4(prop4);
    }

    @Override
    public int getProp4()
    {
        Log.i(TAG, "request getProp4 called, will call native ");
        return nativeGetProp4();
    }

  
    // methods

    @Override
    public int func1(int param1) {
        Log.i(TAG, "request method func1 called");
        try {
            return func1Async(param1).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "func1 sync call timed out");
            return 0;
        } catch (Exception e) {
            Log.w(TAG, "func1 sync call failed: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public CompletableFuture<Integer> func1Async(int param1) {
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
    public int func2(int param1, int param2) {
        Log.i(TAG, "request method func2 called");
        try {
            return func2Async(param1, param2).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "func2 sync call timed out");
            return 0;
        } catch (Exception e) {
            Log.w(TAG, "func2 sync call failed: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public CompletableFuture<Integer> func2Async(int param1, int param2) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFunc2Async(callId, param1, param2);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for func2"));
        }
        return future;
    }

    @Override
    public int func3(int param1, int param2, int param3) {
        Log.i(TAG, "request method func3 called");
        try {
            return func3Async(param1, param2, param3).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "func3 sync call timed out");
            return 0;
        } catch (Exception e) {
            Log.w(TAG, "func3 sync call failed: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public CompletableFuture<Integer> func3Async(int param1, int param2, int param3) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFunc3Async(callId, param1, param2, param3);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for func3"));
        }
        return future;
    }

    @Override
    public int func4(int param1, int param2, int param3, int param4) {
        Log.i(TAG, "request method func4 called");
        try {
            return func4Async(param1, param2, param3, param4).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "func4 sync call timed out");
            return 0;
        } catch (Exception e) {
            Log.w(TAG, "func4 sync call failed: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public CompletableFuture<Integer> func4Async(int param1, int param2, int param3, int param4) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFunc4Async(callId, param1, param2, param3, param4);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for func4"));
        }
        return future;
    }

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    private native void nativeSetProp1(int prop1);
    private native int nativeGetProp1();
  
    private native void nativeSetProp2(int prop2);
    private native int nativeGetProp2();
  
    private native void nativeSetProp3(int prop3);
    private native int nativeGetProp3();
  
    private native void nativeSetProp4(int prop4);
    private native int nativeGetProp4();
  
    // methods (async, returns false if native service unavailable)
    private native boolean nativeFunc1Async(String callId, int param1);
    private native boolean nativeFunc2Async(String callId, int param1, int param2);
    private native boolean nativeFunc3Async(String callId, int param1, int param2, int param3);
    private native boolean nativeFunc4Async(String callId, int param1, int param2, int param3, int param4);

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
    public void onFunc1Result(int result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFunc2Result(int result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFunc3Result(int result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFunc4Result(int result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }

    //In theory event listener interface
    public void onProp1Changed(int newValue)
    {
         Log.i(TAG, "onProp1Changed, will pass notification to all listeners");
         fireProp1Changed(newValue);
    }
    public void onProp2Changed(int newValue)
    {
         Log.i(TAG, "onProp2Changed, will pass notification to all listeners");
         fireProp2Changed(newValue);
    }
    public void onProp3Changed(int newValue)
    {
         Log.i(TAG, "onProp3Changed, will pass notification to all listeners");
         fireProp3Changed(newValue);
    }
    public void onProp4Changed(int newValue)
    {
         Log.i(TAG, "onProp4Changed, will pass notification to all listeners");
         fireProp4Changed(newValue);
    }
    public void onSig1(int param1)
    {
        Log.i(TAG, "onSig1, will pass notification to all listeners");
        fireSig1(param1);
    }
    public void onSig2(int param1, int param2)
    {
        Log.i(TAG, "onSig2, will pass notification to all listeners");
        fireSig2(param1, param2);
    }
    public void onSig3(int param1, int param2, int param3)
    {
        Log.i(TAG, "onSig3, will pass notification to all listeners");
        fireSig3(param1, param2, param3);
    }
    public void onSig4(int param1, int param2, int param3, int param4)
    {
        Log.i(TAG, "onSig4, will pass notification to all listeners");
        fireSig4(param1, param2, param3, param4);
    }

}
