package tbSimple.tbSimplejniclient;

import tbSimple.tbSimple_api.ISimpleInterface;
import tbSimple.tbSimple_api.AbstractSimpleInterface;
import tbSimple.tbSimple_api.ISimpleInterfaceEventListener;
import tbSimple.tbSimple_api.RemoteOperationException;

import tbSimple.tbSimple_android_client.SimpleInterfaceClient;
import apigear.android.lifecycle.BindingLifecycleRegistry;
import apigear.android.lifecycle.IBindingLifecycleCoordinator;
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class SimpleInterfaceJniClient extends AbstractSimpleInterface implements ISimpleInterfaceEventListener
{

    private static final String TAG = "SimpleInterfaceJniClient";

    private SimpleInterfaceClient mMessengerClient = null;


    private static String ModuleName = "tbSimple.tbSimplejniservice.SimpleInterfaceJniService";
    private String lastServicePackage ="";

    // Stable Runnable reference so coordinator register/unregister see the same
    // instance. Initialized once per JniClient; method-reference resolution at
    // field-init time gives a single Runnable bound to this::unbind.
    private final Runnable mCleanup = this::unbind;

    @Override
    public boolean _isReady()
    {
        return mMessengerClient != null ? mMessengerClient._isReady() : false;
    }
    @Override
    public void setPropBool(boolean propBool)
    {
        Log.i(TAG, "got request from ue, setPropBool" + (propBool));
        mMessengerClient.setPropBool(propBool);
    }
    @Override
    public boolean getPropBool()
    {
        Log.i(TAG, "got request from ue, getPropBool");
        return mMessengerClient.getPropBool();
    }
    
    @Override
    public void setPropInt(int propInt)
    {
        Log.i(TAG, "got request from ue, setPropInt" + (propInt));
        mMessengerClient.setPropInt(propInt);
    }
    @Override
    public int getPropInt()
    {
        Log.i(TAG, "got request from ue, getPropInt");
        return mMessengerClient.getPropInt();
    }
    
    @Override
    public void setPropInt32(int propInt32)
    {
        Log.i(TAG, "got request from ue, setPropInt32" + (propInt32));
        mMessengerClient.setPropInt32(propInt32);
    }
    @Override
    public int getPropInt32()
    {
        Log.i(TAG, "got request from ue, getPropInt32");
        return mMessengerClient.getPropInt32();
    }
    
    @Override
    public void setPropInt64(long propInt64)
    {
        Log.i(TAG, "got request from ue, setPropInt64" + (propInt64));
        mMessengerClient.setPropInt64(propInt64);
    }
    @Override
    public long getPropInt64()
    {
        Log.i(TAG, "got request from ue, getPropInt64");
        return mMessengerClient.getPropInt64();
    }
    
    @Override
    public void setPropFloat(float propFloat)
    {
        Log.i(TAG, "got request from ue, setPropFloat" + (propFloat));
        mMessengerClient.setPropFloat(propFloat);
    }
    @Override
    public float getPropFloat()
    {
        Log.i(TAG, "got request from ue, getPropFloat");
        return mMessengerClient.getPropFloat();
    }
    
    @Override
    public void setPropFloat32(float propFloat32)
    {
        Log.i(TAG, "got request from ue, setPropFloat32" + (propFloat32));
        mMessengerClient.setPropFloat32(propFloat32);
    }
    @Override
    public float getPropFloat32()
    {
        Log.i(TAG, "got request from ue, getPropFloat32");
        return mMessengerClient.getPropFloat32();
    }
    
    @Override
    public void setPropFloat64(double propFloat64)
    {
        Log.i(TAG, "got request from ue, setPropFloat64" + (propFloat64));
        mMessengerClient.setPropFloat64(propFloat64);
    }
    @Override
    public double getPropFloat64()
    {
        Log.i(TAG, "got request from ue, getPropFloat64");
        return mMessengerClient.getPropFloat64();
    }
    
    @Override
    public void setPropString(String propString)
    {
        Log.i(TAG, "got request from ue, setPropString" + (propString));
        mMessengerClient.setPropString(propString);
    }
    @Override
    public String getPropString()
    {
        Log.i(TAG, "got request from ue, getPropString");
        return mMessengerClient.getPropString();
    }
    
     @Override
     public void funcNoReturnValue(boolean paramBool)
     {
        Log.v(TAG, "Blocking callfuncNoReturnValue - should not be used ");
         mMessengerClient.funcNoReturnValue(paramBool);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnFuncNoReturnValueResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcNoReturnValueAsync(String callId, boolean paramBool){
        Log.v(TAG, "non blocking call funcNoReturnValue ");
        mMessengerClient.funcNoReturnValueAsync(paramBool).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcNoReturnValue async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncNoReturnValueResult(callId);
            }
        });
    }

    @Override
    public CompletableFuture<Void> funcNoReturnValueAsync(boolean paramBool)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcNoReturnValueAsync(paramBool);
    }
     @Override
     public boolean funcNoParams()
     {
        Log.v(TAG, "Blocking callfuncNoParams - should not be used ");
        return mMessengerClient.funcNoParams();
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnFuncNoParamsResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcNoParamsAsync(String callId){
        Log.v(TAG, "non blocking call funcNoParams ");
        mMessengerClient.funcNoParamsAsync().whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcNoParams async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncNoParamsResult(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> funcNoParamsAsync()
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcNoParamsAsync();
    }
     @Override
     public boolean funcBool(boolean paramBool)
     {
        Log.v(TAG, "Blocking callfuncBool - should not be used ");
        return mMessengerClient.funcBool(paramBool);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnFuncBoolResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcBoolAsync(String callId, boolean paramBool){
        Log.v(TAG, "non blocking call funcBool ");
        mMessengerClient.funcBoolAsync(paramBool).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcBool async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncBoolResult(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> funcBoolAsync(boolean paramBool)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcBoolAsync(paramBool);
    }
     @Override
     public int funcInt(int paramInt)
     {
        Log.v(TAG, "Blocking callfuncInt - should not be used ");
        return mMessengerClient.funcInt(paramInt);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnFuncIntResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcIntAsync(String callId, int paramInt){
        Log.v(TAG, "non blocking call funcInt ");
        mMessengerClient.funcIntAsync(paramInt).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcInt async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncIntResult(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<Integer> funcIntAsync(int paramInt)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcIntAsync(paramInt);
    }
     @Override
     public int funcInt32(int paramInt32)
     {
        Log.v(TAG, "Blocking callfuncInt32 - should not be used ");
        return mMessengerClient.funcInt32(paramInt32);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnFuncInt32Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcInt32Async(String callId, int paramInt32){
        Log.v(TAG, "non blocking call funcInt32 ");
        mMessengerClient.funcInt32Async(paramInt32).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcInt32 async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncInt32Result(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<Integer> funcInt32Async(int paramInt32)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcInt32Async(paramInt32);
    }
     @Override
     public long funcInt64(long paramInt64)
     {
        Log.v(TAG, "Blocking callfuncInt64 - should not be used ");
        return mMessengerClient.funcInt64(paramInt64);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnFuncInt64Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcInt64Async(String callId, long paramInt64){
        Log.v(TAG, "non blocking call funcInt64 ");
        mMessengerClient.funcInt64Async(paramInt64).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcInt64 async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncInt64Result(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<Long> funcInt64Async(long paramInt64)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcInt64Async(paramInt64);
    }
     @Override
     public float funcFloat(float paramFloat)
     {
        Log.v(TAG, "Blocking callfuncFloat - should not be used ");
        return mMessengerClient.funcFloat(paramFloat);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnFuncFloatResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcFloatAsync(String callId, float paramFloat){
        Log.v(TAG, "non blocking call funcFloat ");
        mMessengerClient.funcFloatAsync(paramFloat).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcFloat async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncFloatResult(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<Float> funcFloatAsync(float paramFloat)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcFloatAsync(paramFloat);
    }
     @Override
     public float funcFloat32(float paramFloat32)
     {
        Log.v(TAG, "Blocking callfuncFloat32 - should not be used ");
        return mMessengerClient.funcFloat32(paramFloat32);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnFuncFloat32Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcFloat32Async(String callId, float paramFloat32){
        Log.v(TAG, "non blocking call funcFloat32 ");
        mMessengerClient.funcFloat32Async(paramFloat32).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcFloat32 async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncFloat32Result(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<Float> funcFloat32Async(float paramFloat32)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcFloat32Async(paramFloat32);
    }
     @Override
     public double funcFloat64(double paramFloat)
     {
        Log.v(TAG, "Blocking callfuncFloat64 - should not be used ");
        return mMessengerClient.funcFloat64(paramFloat);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnFuncFloat64Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcFloat64Async(String callId, double paramFloat){
        Log.v(TAG, "non blocking call funcFloat64 ");
        mMessengerClient.funcFloat64Async(paramFloat).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcFloat64 async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncFloat64Result(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<Double> funcFloat64Async(double paramFloat)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcFloat64Async(paramFloat);
    }
     @Override
     public String funcString(String paramString)
     {
        Log.v(TAG, "Blocking callfuncString - should not be used ");
        return mMessengerClient.funcString(paramString);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnFuncStringResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcStringAsync(String callId, String paramString){
        Log.v(TAG, "non blocking call funcString ");
        mMessengerClient.funcStringAsync(paramString).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcString async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncStringResult(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<String> funcStringAsync(String paramString)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcStringAsync(paramString);
    }

    public boolean bind(Context ctx, String packageName, String connectionID){
        Log.v(TAG, "natice client: bind " + packageName);
        boolean res = initServiceConnection(ctx, packageName, connectionID);
        if (res)
        {
            IBindingLifecycleCoordinator coordinator = BindingLifecycleRegistry.get();
            if (coordinator != null)
            {
                coordinator.registerCleanup(mCleanup);
            }
        }
        return res;
    }

    public void unbind(){
        Log.v(TAG, "native client: unbind " + lastServicePackage);
        IBindingLifecycleCoordinator coordinator = BindingLifecycleRegistry.get();
        if (coordinator != null)
        {
            coordinator.unregisterCleanup(mCleanup);
        }
        if (mMessengerClient != null)
        {
            mMessengerClient.unbindFromService();
        }
    }

    private boolean initServiceConnection(Context ctx, String servicePackage, String connectionID)
    {
        if (mMessengerClient == null)
        {
            mMessengerClient = new SimpleInterfaceClient(ctx, connectionID);
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

    //Event listener
    @Override
    public void onPropBoolChanged(boolean newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropBoolChanged(newValue);
    }
    @Override
    public void onPropIntChanged(int newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropIntChanged(newValue);
    }
    @Override
    public void onPropInt32Changed(int newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropInt32Changed(newValue);
    }
    @Override
    public void onPropInt64Changed(long newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropInt64Changed(newValue);
    }
    @Override
    public void onPropFloatChanged(float newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropFloatChanged(newValue);
    }
    @Override
    public void onPropFloat32Changed(float newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropFloat32Changed(newValue);
    }
    @Override
    public void onPropFloat64Changed(double newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropFloat64Changed(newValue);
    }
    @Override
    public void onPropStringChanged(String newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropStringChanged(newValue);
    }
    @Override
    public void onSigBool(boolean paramBool)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigBool "+ " " + paramBool);
        nativeOnSigBool(paramBool);
    }
    @Override
    public void onSigInt(int paramInt)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigInt "+ " " + paramInt);
        nativeOnSigInt(paramInt);
    }
    @Override
    public void onSigInt32(int paramInt32)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigInt32 "+ " " + paramInt32);
        nativeOnSigInt32(paramInt32);
    }
    @Override
    public void onSigInt64(long paramInt64)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigInt64 "+ " " + paramInt64);
        nativeOnSigInt64(paramInt64);
    }
    @Override
    public void onSigFloat(float paramFloat)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigFloat "+ " " + paramFloat);
        nativeOnSigFloat(paramFloat);
    }
    @Override
    public void onSigFloat32(float paramFloat32)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigFloat32 "+ " " + paramFloat32);
        nativeOnSigFloat32(paramFloat32);
    }
    @Override
    public void onSigFloat64(double paramFloat64)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigFloat64 "+ " " + paramFloat64);
        nativeOnSigFloat64(paramFloat64);
    }
    @Override
    public void onSigString(String paramString)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigString "+ " " + paramString);
        nativeOnSigString(paramString);
    }
     private native void nativeOnPropBoolChanged(boolean propBool);
     private native void nativeOnPropIntChanged(int propInt);
     private native void nativeOnPropInt32Changed(int propInt32);
     private native void nativeOnPropInt64Changed(long propInt64);
     private native void nativeOnPropFloatChanged(float propFloat);
     private native void nativeOnPropFloat32Changed(float propFloat32);
     private native void nativeOnPropFloat64Changed(double propFloat64);
     private native void nativeOnPropStringChanged(String propString);
    private native void nativeOnSigBool(boolean paramBool);
    private native void nativeOnSigInt(int paramInt);
    private native void nativeOnSigInt32(int paramInt32);
    private native void nativeOnSigInt64(long paramInt64);
    private native void nativeOnSigFloat(float paramFloat);
    private native void nativeOnSigFloat32(float paramFloat32);
    private native void nativeOnSigFloat64(double paramFloat64);
    private native void nativeOnSigString(String paramString);
    private native void nativeOnFuncNoReturnValueResult(String callId);
    private native void nativeOnFuncNoParamsResult(boolean result, String callId);
    private native void nativeOnFuncBoolResult(boolean result, String callId);
    private native void nativeOnFuncIntResult(int result, String callId);
    private native void nativeOnFuncInt32Result(int result, String callId);
    private native void nativeOnFuncInt64Result(long result, String callId);
    private native void nativeOnFuncFloatResult(float result, String callId);
    private native void nativeOnFuncFloat32Result(float result, String callId);
    private native void nativeOnFuncFloat64Result(double result, String callId);
    private native void nativeOnFuncStringResult(String result, String callId);
    private native void nativeAsyncOperationFailed(String callId, String errorMessage, int errorCode);
    private native void nativeIsReady(boolean isReady);
}
