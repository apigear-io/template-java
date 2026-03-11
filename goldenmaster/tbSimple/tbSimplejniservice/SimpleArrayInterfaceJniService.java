package tbSimple.tbSimplejniservice;

import android.util.Log;

import tbSimple.tbSimple_api.ISimpleArrayInterface;
import tbSimple.tbSimple_api.AbstractSimpleArrayInterface;
import tbSimple.tbSimple_api.ISimpleArrayInterfaceEventListener;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class SimpleArrayInterfaceJniService extends AbstractSimpleArrayInterface {


    private final static String TAG = "SimpleArrayInterfaceJniService";
    private static volatile boolean isServiceReady = false;
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingFutures
        = new ConcurrentHashMap<>();

    public SimpleArrayInterfaceJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setPropBool(boolean[] propBool)
    {
        Log.i(TAG, "request setPropBool called, will call native ");
        nativeSetPropBool(propBool);
    }

    @Override
    public boolean[] getPropBool()
    {
        Log.i(TAG, "request getPropBool called, will call native ");
        return nativeGetPropBool();
    }

  
    @Override
    public void setPropInt(int[] propInt)
    {
        Log.i(TAG, "request setPropInt called, will call native ");
        nativeSetPropInt(propInt);
    }

    @Override
    public int[] getPropInt()
    {
        Log.i(TAG, "request getPropInt called, will call native ");
        return nativeGetPropInt();
    }

  
    @Override
    public void setPropInt32(int[] propInt32)
    {
        Log.i(TAG, "request setPropInt32 called, will call native ");
        nativeSetPropInt32(propInt32);
    }

    @Override
    public int[] getPropInt32()
    {
        Log.i(TAG, "request getPropInt32 called, will call native ");
        return nativeGetPropInt32();
    }

  
    @Override
    public void setPropInt64(long[] propInt64)
    {
        Log.i(TAG, "request setPropInt64 called, will call native ");
        nativeSetPropInt64(propInt64);
    }

    @Override
    public long[] getPropInt64()
    {
        Log.i(TAG, "request getPropInt64 called, will call native ");
        return nativeGetPropInt64();
    }

  
    @Override
    public void setPropFloat(float[] propFloat)
    {
        Log.i(TAG, "request setPropFloat called, will call native ");
        nativeSetPropFloat(propFloat);
    }

    @Override
    public float[] getPropFloat()
    {
        Log.i(TAG, "request getPropFloat called, will call native ");
        return nativeGetPropFloat();
    }

  
    @Override
    public void setPropFloat32(float[] propFloat32)
    {
        Log.i(TAG, "request setPropFloat32 called, will call native ");
        nativeSetPropFloat32(propFloat32);
    }

    @Override
    public float[] getPropFloat32()
    {
        Log.i(TAG, "request getPropFloat32 called, will call native ");
        return nativeGetPropFloat32();
    }

  
    @Override
    public void setPropFloat64(double[] propFloat64)
    {
        Log.i(TAG, "request setPropFloat64 called, will call native ");
        nativeSetPropFloat64(propFloat64);
    }

    @Override
    public double[] getPropFloat64()
    {
        Log.i(TAG, "request getPropFloat64 called, will call native ");
        return nativeGetPropFloat64();
    }

  
    @Override
    public void setPropString(String[] propString)
    {
        Log.i(TAG, "request setPropString called, will call native ");
        nativeSetPropString(propString);
    }

    @Override
    public String[] getPropString()
    {
        Log.i(TAG, "request getPropString called, will call native ");
        return nativeGetPropString();
    }

  
    @Override
    public void setPropReadOnlyString(String propReadOnlyString)
    {
        Log.i(TAG, "request setPropReadOnlyString called, will call native ");
        nativeSetPropReadOnlyString(propReadOnlyString);
    }

    @Override
    public String getPropReadOnlyString()
    {
        Log.i(TAG, "request getPropReadOnlyString called, will call native ");
        return nativeGetPropReadOnlyString();
    }

  
    // methods

    @Override
    public boolean[] funcBool(boolean[] paramBool) {
        Log.i(TAG, "request method funcBool called");
        try {
            return funcBoolAsync(paramBool).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcBool sync call timed out");
            return new boolean[]{};
        } catch (Exception e) {
            Log.w(TAG, "funcBool sync call failed: " + e.getMessage());
            return new boolean[]{};
        }
    }

    @Override
    public CompletableFuture<boolean[]> funcBoolAsync(boolean[] paramBool) {
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
    public int[] funcInt(int[] paramInt) {
        Log.i(TAG, "request method funcInt called");
        try {
            return funcIntAsync(paramInt).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcInt sync call timed out");
            return new int[]{};
        } catch (Exception e) {
            Log.w(TAG, "funcInt sync call failed: " + e.getMessage());
            return new int[]{};
        }
    }

    @Override
    public CompletableFuture<int[]> funcIntAsync(int[] paramInt) {
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
    public int[] funcInt32(int[] paramInt32) {
        Log.i(TAG, "request method funcInt32 called");
        try {
            return funcInt32Async(paramInt32).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcInt32 sync call timed out");
            return new int[]{};
        } catch (Exception e) {
            Log.w(TAG, "funcInt32 sync call failed: " + e.getMessage());
            return new int[]{};
        }
    }

    @Override
    public CompletableFuture<int[]> funcInt32Async(int[] paramInt32) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFuncInt32Async(callId, paramInt32);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for funcInt32"));
        }
        return future;
    }

    @Override
    public long[] funcInt64(long[] paramInt64) {
        Log.i(TAG, "request method funcInt64 called");
        try {
            return funcInt64Async(paramInt64).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcInt64 sync call timed out");
            return new long[]{};
        } catch (Exception e) {
            Log.w(TAG, "funcInt64 sync call failed: " + e.getMessage());
            return new long[]{};
        }
    }

    @Override
    public CompletableFuture<long[]> funcInt64Async(long[] paramInt64) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFuncInt64Async(callId, paramInt64);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for funcInt64"));
        }
        return future;
    }

    @Override
    public float[] funcFloat(float[] paramFloat) {
        Log.i(TAG, "request method funcFloat called");
        try {
            return funcFloatAsync(paramFloat).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcFloat sync call timed out");
            return new float[]{};
        } catch (Exception e) {
            Log.w(TAG, "funcFloat sync call failed: " + e.getMessage());
            return new float[]{};
        }
    }

    @Override
    public CompletableFuture<float[]> funcFloatAsync(float[] paramFloat) {
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
    public float[] funcFloat32(float[] paramFloat32) {
        Log.i(TAG, "request method funcFloat32 called");
        try {
            return funcFloat32Async(paramFloat32).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcFloat32 sync call timed out");
            return new float[]{};
        } catch (Exception e) {
            Log.w(TAG, "funcFloat32 sync call failed: " + e.getMessage());
            return new float[]{};
        }
    }

    @Override
    public CompletableFuture<float[]> funcFloat32Async(float[] paramFloat32) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFuncFloat32Async(callId, paramFloat32);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for funcFloat32"));
        }
        return future;
    }

    @Override
    public double[] funcFloat64(double[] paramFloat) {
        Log.i(TAG, "request method funcFloat64 called");
        try {
            return funcFloat64Async(paramFloat).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcFloat64 sync call timed out");
            return new double[]{};
        } catch (Exception e) {
            Log.w(TAG, "funcFloat64 sync call failed: " + e.getMessage());
            return new double[]{};
        }
    }

    @Override
    public CompletableFuture<double[]> funcFloat64Async(double[] paramFloat) {
        String callId = UUID.randomUUID().toString().replace("-", "");
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingFutures.put(callId, future);
        boolean enqueued = nativeFuncFloat64Async(callId, paramFloat);
        if (!enqueued) {
            pendingFutures.remove(callId);
            future.completeExceptionally(
                new IllegalStateException("Native service unavailable for funcFloat64"));
        }
        return future;
    }

    @Override
    public String[] funcString(String[] paramString) {
        Log.i(TAG, "request method funcString called");
        try {
            return funcStringAsync(paramString).get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "funcString sync call timed out");
            return new String[]{};
        } catch (Exception e) {
            Log.w(TAG, "funcString sync call failed: " + e.getMessage());
            return new String[]{};
        }
    }

    @Override
    public CompletableFuture<String[]> funcStringAsync(String[] paramString) {
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
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    private native void nativeSetPropBool(boolean[] propBool);
    private native boolean[] nativeGetPropBool();
  
    private native void nativeSetPropInt(int[] propInt);
    private native int[] nativeGetPropInt();
  
    private native void nativeSetPropInt32(int[] propInt32);
    private native int[] nativeGetPropInt32();
  
    private native void nativeSetPropInt64(long[] propInt64);
    private native long[] nativeGetPropInt64();
  
    private native void nativeSetPropFloat(float[] propFloat);
    private native float[] nativeGetPropFloat();
  
    private native void nativeSetPropFloat32(float[] propFloat32);
    private native float[] nativeGetPropFloat32();
  
    private native void nativeSetPropFloat64(double[] propFloat64);
    private native double[] nativeGetPropFloat64();
  
    private native void nativeSetPropString(String[] propString);
    private native String[] nativeGetPropString();
  
    private native void nativeSetPropReadOnlyString(String propReadOnlyString);
    private native String nativeGetPropReadOnlyString();
  
    // methods (async, returns false if native service unavailable)
    private native boolean nativeFuncBoolAsync(String callId, boolean[] paramBool);
    private native boolean nativeFuncIntAsync(String callId, int[] paramInt);
    private native boolean nativeFuncInt32Async(String callId, int[] paramInt32);
    private native boolean nativeFuncInt64Async(String callId, long[] paramInt64);
    private native boolean nativeFuncFloatAsync(String callId, float[] paramFloat);
    private native boolean nativeFuncFloat32Async(String callId, float[] paramFloat32);
    private native boolean nativeFuncFloat64Async(String callId, double[] paramFloat);
    private native boolean nativeFuncStringAsync(String callId, String[] paramString);

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
    public void onFuncBoolResult(boolean[] result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFuncIntResult(int[] result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFuncInt32Result(int[] result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFuncInt64Result(long[] result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFuncFloatResult(float[] result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFuncFloat32Result(float[] result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFuncFloat64Result(double[] result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }
    public void onFuncStringResult(String[] result, String callId) {
        CompletableFuture<?> future = pendingFutures.remove(callId);
        if (future != null) {
            @SuppressWarnings("unchecked")
            CompletableFuture<Object> typedFuture = (CompletableFuture<Object>) future;
            typedFuture.complete(result);
        }
    }

    //In theory event listener interface
    public void onPropBoolChanged(boolean[] newValue)
    {
         Log.i(TAG, "onPropBoolChanged, will pass notification to all listeners");
         firePropBoolChanged(newValue);
    }
    public void onPropIntChanged(int[] newValue)
    {
         Log.i(TAG, "onPropIntChanged, will pass notification to all listeners");
         firePropIntChanged(newValue);
    }
    public void onPropInt32Changed(int[] newValue)
    {
         Log.i(TAG, "onPropInt32Changed, will pass notification to all listeners");
         firePropInt32Changed(newValue);
    }
    public void onPropInt64Changed(long[] newValue)
    {
         Log.i(TAG, "onPropInt64Changed, will pass notification to all listeners");
         firePropInt64Changed(newValue);
    }
    public void onPropFloatChanged(float[] newValue)
    {
         Log.i(TAG, "onPropFloatChanged, will pass notification to all listeners");
         firePropFloatChanged(newValue);
    }
    public void onPropFloat32Changed(float[] newValue)
    {
         Log.i(TAG, "onPropFloat32Changed, will pass notification to all listeners");
         firePropFloat32Changed(newValue);
    }
    public void onPropFloat64Changed(double[] newValue)
    {
         Log.i(TAG, "onPropFloat64Changed, will pass notification to all listeners");
         firePropFloat64Changed(newValue);
    }
    public void onPropStringChanged(String[] newValue)
    {
         Log.i(TAG, "onPropStringChanged, will pass notification to all listeners");
         firePropStringChanged(newValue);
    }
    public void onPropReadOnlyStringChanged(String newValue)
    {
         Log.i(TAG, "onPropReadOnlyStringChanged, will pass notification to all listeners");
         firePropReadOnlyStringChanged(newValue);
    }
    public void onSigBool(boolean[] paramBool)
    {
        Log.i(TAG, "onSigBool, will pass notification to all listeners");
        fireSigBool(paramBool);
    }
    public void onSigInt(int[] paramInt)
    {
        Log.i(TAG, "onSigInt, will pass notification to all listeners");
        fireSigInt(paramInt);
    }
    public void onSigInt32(int[] paramInt32)
    {
        Log.i(TAG, "onSigInt32, will pass notification to all listeners");
        fireSigInt32(paramInt32);
    }
    public void onSigInt64(long[] paramInt64)
    {
        Log.i(TAG, "onSigInt64, will pass notification to all listeners");
        fireSigInt64(paramInt64);
    }
    public void onSigFloat(float[] paramFloat)
    {
        Log.i(TAG, "onSigFloat, will pass notification to all listeners");
        fireSigFloat(paramFloat);
    }
    public void onSigFloat32(float[] paramFloa32)
    {
        Log.i(TAG, "onSigFloat32, will pass notification to all listeners");
        fireSigFloat32(paramFloa32);
    }
    public void onSigFloat64(double[] paramFloat64)
    {
        Log.i(TAG, "onSigFloat64, will pass notification to all listeners");
        fireSigFloat64(paramFloat64);
    }
    public void onSigString(String[] paramString)
    {
        Log.i(TAG, "onSigString, will pass notification to all listeners");
        fireSigString(paramString);
    }

}
