package tbSimple.tbSimplejniclient;

import tbSimple.tbSimple_api.ISimpleArrayInterface;
import tbSimple.tbSimple_api.AbstractSimpleArrayInterface;
import tbSimple.tbSimple_api.ISimpleArrayInterfaceEventListener;
import tbSimple.tbSimple_api.RemoteOperationException;

import tbSimple.tbSimple_android_client.SimpleArrayInterfaceClient;
import tbSimple.tbSimple_android_messenger.Conversions;
import android.content.Context;

import android.os.Bundle;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class SimpleArrayInterfaceJniClient extends AbstractSimpleArrayInterface implements ISimpleArrayInterfaceEventListener
{

    private static final String TAG = "SimpleArrayInterfaceJniClient";

    private SimpleArrayInterfaceClient mMessengerClient = null;


    private static String ModuleName = "tbSimple.tbSimplejniservice.SimpleArrayInterfaceJniService";
    private String lastServicePackage ="";

    @Override
    public boolean _isReady()
    {
        return mMessengerClient != null ? mMessengerClient._isReady() : false;
    }
    // Interface method — List types
    @Override
    public void setPropBool(List<Boolean> propBool)
    {
        Log.i(TAG, "got request setPropBool" + (propBool));
        mMessengerClient.setPropBool(propBool);
    }
    // JNI entry point — array types for C++ compatibility
    public void setPropBool(boolean[] propBool)
    {
        Log.i(TAG, "got JNI request setPropBool");
        setPropBool(Conversions.toList(propBool));
    }
    @Override
    public List<Boolean> getPropBool()
    {
        Log.i(TAG, "got request getPropBool");
        return mMessengerClient.getPropBool();
    }


    
    // Interface method — List types
    @Override
    public void setPropInt(List<Integer> propInt)
    {
        Log.i(TAG, "got request setPropInt" + (propInt));
        mMessengerClient.setPropInt(propInt);
    }
    // JNI entry point — array types for C++ compatibility
    public void setPropInt(int[] propInt)
    {
        Log.i(TAG, "got JNI request setPropInt");
        setPropInt(Conversions.toList(propInt));
    }
    @Override
    public List<Integer> getPropInt()
    {
        Log.i(TAG, "got request getPropInt");
        return mMessengerClient.getPropInt();
    }


    
    // Interface method — List types
    @Override
    public void setPropInt32(List<Integer> propInt32)
    {
        Log.i(TAG, "got request setPropInt32" + (propInt32));
        mMessengerClient.setPropInt32(propInt32);
    }
    // JNI entry point — array types for C++ compatibility
    public void setPropInt32(int[] propInt32)
    {
        Log.i(TAG, "got JNI request setPropInt32");
        setPropInt32(Conversions.toList(propInt32));
    }
    @Override
    public List<Integer> getPropInt32()
    {
        Log.i(TAG, "got request getPropInt32");
        return mMessengerClient.getPropInt32();
    }


    
    // Interface method — List types
    @Override
    public void setPropInt64(List<Long> propInt64)
    {
        Log.i(TAG, "got request setPropInt64" + (propInt64));
        mMessengerClient.setPropInt64(propInt64);
    }
    // JNI entry point — array types for C++ compatibility
    public void setPropInt64(long[] propInt64)
    {
        Log.i(TAG, "got JNI request setPropInt64");
        setPropInt64(Conversions.toList(propInt64));
    }
    @Override
    public List<Long> getPropInt64()
    {
        Log.i(TAG, "got request getPropInt64");
        return mMessengerClient.getPropInt64();
    }


    
    // Interface method — List types
    @Override
    public void setPropFloat(List<Float> propFloat)
    {
        Log.i(TAG, "got request setPropFloat" + (propFloat));
        mMessengerClient.setPropFloat(propFloat);
    }
    // JNI entry point — array types for C++ compatibility
    public void setPropFloat(float[] propFloat)
    {
        Log.i(TAG, "got JNI request setPropFloat");
        setPropFloat(Conversions.toList(propFloat));
    }
    @Override
    public List<Float> getPropFloat()
    {
        Log.i(TAG, "got request getPropFloat");
        return mMessengerClient.getPropFloat();
    }


    
    // Interface method — List types
    @Override
    public void setPropFloat32(List<Float> propFloat32)
    {
        Log.i(TAG, "got request setPropFloat32" + (propFloat32));
        mMessengerClient.setPropFloat32(propFloat32);
    }
    // JNI entry point — array types for C++ compatibility
    public void setPropFloat32(float[] propFloat32)
    {
        Log.i(TAG, "got JNI request setPropFloat32");
        setPropFloat32(Conversions.toList(propFloat32));
    }
    @Override
    public List<Float> getPropFloat32()
    {
        Log.i(TAG, "got request getPropFloat32");
        return mMessengerClient.getPropFloat32();
    }


    
    // Interface method — List types
    @Override
    public void setPropFloat64(List<Double> propFloat64)
    {
        Log.i(TAG, "got request setPropFloat64" + (propFloat64));
        mMessengerClient.setPropFloat64(propFloat64);
    }
    // JNI entry point — array types for C++ compatibility
    public void setPropFloat64(double[] propFloat64)
    {
        Log.i(TAG, "got JNI request setPropFloat64");
        setPropFloat64(Conversions.toList(propFloat64));
    }
    @Override
    public List<Double> getPropFloat64()
    {
        Log.i(TAG, "got request getPropFloat64");
        return mMessengerClient.getPropFloat64();
    }


    
    // Interface method — List types
    @Override
    public void setPropString(List<String> propString)
    {
        Log.i(TAG, "got request setPropString" + (propString));
        mMessengerClient.setPropString(propString);
    }
    // JNI entry point — array types for C++ compatibility
    public void setPropString(String[] propString)
    {
        Log.i(TAG, "got JNI request setPropString");
        setPropString(Conversions.toList(propString));
    }
    @Override
    public List<String> getPropString()
    {
        Log.i(TAG, "got request getPropString");
        return mMessengerClient.getPropString();
    }


    
    // Interface method — List types
    @Override
    public void setPropReadOnlyString(String propReadOnlyString)
    {
        Log.i(TAG, "got request setPropReadOnlyString" + (propReadOnlyString));
        mMessengerClient.setPropReadOnlyString(propReadOnlyString);
    }
    @Override
    public String getPropReadOnlyString()
    {
        Log.i(TAG, "got request getPropReadOnlyString");
        return mMessengerClient.getPropReadOnlyString();
    }


    
    // Interface method — List types
    @Override
    public List<Boolean> funcBool(List<Boolean> paramBool)
    {
        Log.v(TAG, "Blocking callfuncBool - should not be used ");
        return mMessengerClient.funcBool(paramBool);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnFuncBoolResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcBoolAsync(String callId, boolean[] paramBool){
        Log.v(TAG, "non blocking call funcBool ");
        mMessengerClient.funcBoolAsync(Conversions.toList(paramBool)).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcBool async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncBoolResult(Conversions.toArray(result, new boolean[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<Boolean>> funcBoolAsync(List<Boolean> paramBool)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcBoolAsync(paramBool);
    }
    // Interface method — List types
    @Override
    public List<Integer> funcInt(List<Integer> paramInt)
    {
        Log.v(TAG, "Blocking callfuncInt - should not be used ");
        return mMessengerClient.funcInt(paramInt);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnFuncIntResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcIntAsync(String callId, int[] paramInt){
        Log.v(TAG, "non blocking call funcInt ");
        mMessengerClient.funcIntAsync(Conversions.toList(paramInt)).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcInt async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncIntResult(Conversions.toArray(result, new int[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<Integer>> funcIntAsync(List<Integer> paramInt)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcIntAsync(paramInt);
    }
    // Interface method — List types
    @Override
    public List<Integer> funcInt32(List<Integer> paramInt32)
    {
        Log.v(TAG, "Blocking callfuncInt32 - should not be used ");
        return mMessengerClient.funcInt32(paramInt32);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnFuncInt32Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcInt32Async(String callId, int[] paramInt32){
        Log.v(TAG, "non blocking call funcInt32 ");
        mMessengerClient.funcInt32Async(Conversions.toList(paramInt32)).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcInt32 async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncInt32Result(Conversions.toArray(result, new int[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<Integer>> funcInt32Async(List<Integer> paramInt32)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcInt32Async(paramInt32);
    }
    // Interface method — List types
    @Override
    public List<Long> funcInt64(List<Long> paramInt64)
    {
        Log.v(TAG, "Blocking callfuncInt64 - should not be used ");
        return mMessengerClient.funcInt64(paramInt64);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnFuncInt64Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcInt64Async(String callId, long[] paramInt64){
        Log.v(TAG, "non blocking call funcInt64 ");
        mMessengerClient.funcInt64Async(Conversions.toList(paramInt64)).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcInt64 async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncInt64Result(Conversions.toArray(result, new long[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<Long>> funcInt64Async(List<Long> paramInt64)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcInt64Async(paramInt64);
    }
    // Interface method — List types
    @Override
    public List<Float> funcFloat(List<Float> paramFloat)
    {
        Log.v(TAG, "Blocking callfuncFloat - should not be used ");
        return mMessengerClient.funcFloat(paramFloat);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnFuncFloatResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcFloatAsync(String callId, float[] paramFloat){
        Log.v(TAG, "non blocking call funcFloat ");
        mMessengerClient.funcFloatAsync(Conversions.toList(paramFloat)).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcFloat async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncFloatResult(Conversions.toArray(result, new float[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<Float>> funcFloatAsync(List<Float> paramFloat)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcFloatAsync(paramFloat);
    }
    // Interface method — List types
    @Override
    public List<Float> funcFloat32(List<Float> paramFloat32)
    {
        Log.v(TAG, "Blocking callfuncFloat32 - should not be used ");
        return mMessengerClient.funcFloat32(paramFloat32);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnFuncFloat32Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcFloat32Async(String callId, float[] paramFloat32){
        Log.v(TAG, "non blocking call funcFloat32 ");
        mMessengerClient.funcFloat32Async(Conversions.toList(paramFloat32)).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcFloat32 async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncFloat32Result(Conversions.toArray(result, new float[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<Float>> funcFloat32Async(List<Float> paramFloat32)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcFloat32Async(paramFloat32);
    }
    // Interface method — List types
    @Override
    public List<Double> funcFloat64(List<Double> paramFloat)
    {
        Log.v(TAG, "Blocking callfuncFloat64 - should not be used ");
        return mMessengerClient.funcFloat64(paramFloat);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnFuncFloat64Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcFloat64Async(String callId, double[] paramFloat){
        Log.v(TAG, "non blocking call funcFloat64 ");
        mMessengerClient.funcFloat64Async(Conversions.toList(paramFloat)).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcFloat64 async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncFloat64Result(Conversions.toArray(result, new double[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<Double>> funcFloat64Async(List<Double> paramFloat)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcFloat64Async(paramFloat);
    }
    // Interface method — List types
    @Override
    public List<String> funcString(List<String> paramString)
    {
        Log.v(TAG, "Blocking callfuncString - should not be used ");
        return mMessengerClient.funcString(paramString);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnFuncStringResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcStringAsync(String callId, String[] paramString){
        Log.v(TAG, "non blocking call funcString ");
        mMessengerClient.funcStringAsync(Conversions.toList(paramString)).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcString async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncStringResult(Conversions.toArray(result, new String[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<String>> funcStringAsync(List<String> paramString)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcStringAsync(paramString);
    }

    public boolean bind(Context ctx, String packageName, String connectionID){
        Log.v(TAG, "natice client: bind " + packageName);
        return initServiceConnection(ctx, packageName, connectionID);
    }

    public void unbind(){
        Log.v(TAG, "native client: unbind " + lastServicePackage);
        if (mMessengerClient != null)
        {
            mMessengerClient.unbindFromService();
        }
    }

    private boolean initServiceConnection(Context ctx, String servicePackage, String connectionID)
    {
        if (mMessengerClient == null)
        {
            mMessengerClient = new SimpleArrayInterfaceClient(ctx, connectionID);
            Log.i(TAG, "client created ");
            mMessengerClient.addEventListener(this);
        }
        if (!lastServicePackage.equals(servicePackage) &&  mMessengerClient.isBoundToService()) {
            unbind();
        }
        lastServicePackage = servicePackage;
        boolean res = mMessengerClient.bindToService(lastServicePackage);
        Log.v(TAG, "Bind " + res+": to "+lastServicePackage);
        return res;
    }

    @Override
    public void on_readyStatusChanged(boolean isReady) {
        Log.i(TAG, "Will call native Connection state changed "+isReady);
        nativeIsReady(isReady);
    }

    // Event listener — receives List from messenger client, converts to array for native
    @Override
    public void onPropBoolChanged(List<Boolean> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropBoolChanged(Conversions.toArray(newValue, new boolean[0]));
    }
    @Override
    public void onPropIntChanged(List<Integer> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropIntChanged(Conversions.toArray(newValue, new int[0]));
    }
    @Override
    public void onPropInt32Changed(List<Integer> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropInt32Changed(Conversions.toArray(newValue, new int[0]));
    }
    @Override
    public void onPropInt64Changed(List<Long> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropInt64Changed(Conversions.toArray(newValue, new long[0]));
    }
    @Override
    public void onPropFloatChanged(List<Float> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropFloatChanged(Conversions.toArray(newValue, new float[0]));
    }
    @Override
    public void onPropFloat32Changed(List<Float> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropFloat32Changed(Conversions.toArray(newValue, new float[0]));
    }
    @Override
    public void onPropFloat64Changed(List<Double> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropFloat64Changed(Conversions.toArray(newValue, new double[0]));
    }
    @Override
    public void onPropStringChanged(List<String> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropStringChanged(Conversions.toArray(newValue, new String[0]));
    }
    @Override
    public void onPropReadOnlyStringChanged(String newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropReadOnlyStringChanged(newValue);
    }
    @Override
    public void onSigBool(List<Boolean> paramBool)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigBool "+ " " + paramBool);
        nativeOnSigBool(Conversions.toArray(paramBool, new boolean[0]));
    }
    @Override
    public void onSigInt(List<Integer> paramInt)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigInt "+ " " + paramInt);
        nativeOnSigInt(Conversions.toArray(paramInt, new int[0]));
    }
    @Override
    public void onSigInt32(List<Integer> paramInt32)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigInt32 "+ " " + paramInt32);
        nativeOnSigInt32(Conversions.toArray(paramInt32, new int[0]));
    }
    @Override
    public void onSigInt64(List<Long> paramInt64)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigInt64 "+ " " + paramInt64);
        nativeOnSigInt64(Conversions.toArray(paramInt64, new long[0]));
    }
    @Override
    public void onSigFloat(List<Float> paramFloat)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigFloat "+ " " + paramFloat);
        nativeOnSigFloat(Conversions.toArray(paramFloat, new float[0]));
    }
    @Override
    public void onSigFloat32(List<Float> paramFloa32)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigFloat32 "+ " " + paramFloa32);
        nativeOnSigFloat32(Conversions.toArray(paramFloa32, new float[0]));
    }
    @Override
    public void onSigFloat64(List<Double> paramFloat64)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigFloat64 "+ " " + paramFloat64);
        nativeOnSigFloat64(Conversions.toArray(paramFloat64, new double[0]));
    }
    @Override
    public void onSigString(List<String> paramString)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigString "+ " " + paramString);
        nativeOnSigString(Conversions.toArray(paramString, new String[0]));
    }


    // Native declarations — array types for JNI compatibility
     private native void nativeOnPropBoolChanged(boolean[] propBool);
     private native void nativeOnPropIntChanged(int[] propInt);
     private native void nativeOnPropInt32Changed(int[] propInt32);
     private native void nativeOnPropInt64Changed(long[] propInt64);
     private native void nativeOnPropFloatChanged(float[] propFloat);
     private native void nativeOnPropFloat32Changed(float[] propFloat32);
     private native void nativeOnPropFloat64Changed(double[] propFloat64);
     private native void nativeOnPropStringChanged(String[] propString);
     private native void nativeOnPropReadOnlyStringChanged(String propReadOnlyString);
    private native void nativeOnSigBool(boolean[] paramBool);
    private native void nativeOnSigInt(int[] paramInt);
    private native void nativeOnSigInt32(int[] paramInt32);
    private native void nativeOnSigInt64(long[] paramInt64);
    private native void nativeOnSigFloat(float[] paramFloat);
    private native void nativeOnSigFloat32(float[] paramFloa32);
    private native void nativeOnSigFloat64(double[] paramFloat64);
    private native void nativeOnSigString(String[] paramString);
    private native void nativeOnFuncBoolResult(boolean[] result, String callId);
    private native void nativeOnFuncIntResult(int[] result, String callId);
    private native void nativeOnFuncInt32Result(int[] result, String callId);
    private native void nativeOnFuncInt64Result(long[] result, String callId);
    private native void nativeOnFuncFloatResult(float[] result, String callId);
    private native void nativeOnFuncFloat32Result(float[] result, String callId);
    private native void nativeOnFuncFloat64Result(double[] result, String callId);
    private native void nativeOnFuncStringResult(String[] result, String callId);
    private native void nativeAsyncOperationFailed(String callId, String errorMessage, int errorCode);
    private native void nativeIsReady(boolean isReady);
}
