package testbed1.testbed1jniclient;

import testbed1.testbed1_api.IStructInterface;
import testbed1.testbed1_api.AbstractStructInterface;
import testbed1.testbed1_api.IStructInterfaceEventListener;
import testbed1.testbed1_api.RemoteOperationException;

import testbed1.testbed1_android_client.StructInterfaceClient;
import apigear.android.lifecycle.BindingLifecycleRegistry;
import apigear.android.lifecycle.IBindingLifecycleCoordinator;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_android_messenger.StructBoolParcelable;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_android_messenger.StructFloatParcelable;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_android_messenger.StructIntParcelable;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_android_messenger.StructStringParcelable;
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class StructInterfaceJniClient extends AbstractStructInterface implements IStructInterfaceEventListener
{

    private static final String TAG = "StructInterfaceJniClient";

    private StructInterfaceClient mMessengerClient = null;


    private static String ModuleName = "testbed1.testbed1jniservice.StructInterfaceJniService";
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
    public void setPropBool(StructBool propBool)
    {
        Log.i(TAG, "got request from ue, setPropBool" + (propBool));
        mMessengerClient.setPropBool(propBool);
    }
    @Override
    public StructBool getPropBool()
    {
        Log.i(TAG, "got request from ue, getPropBool");
        return mMessengerClient.getPropBool();
    }
    
    @Override
    public void setPropInt(StructInt propInt)
    {
        Log.i(TAG, "got request from ue, setPropInt" + (propInt));
        mMessengerClient.setPropInt(propInt);
    }
    @Override
    public StructInt getPropInt()
    {
        Log.i(TAG, "got request from ue, getPropInt");
        return mMessengerClient.getPropInt();
    }
    
    @Override
    public void setPropFloat(StructFloat propFloat)
    {
        Log.i(TAG, "got request from ue, setPropFloat" + (propFloat));
        mMessengerClient.setPropFloat(propFloat);
    }
    @Override
    public StructFloat getPropFloat()
    {
        Log.i(TAG, "got request from ue, getPropFloat");
        return mMessengerClient.getPropFloat();
    }
    
    @Override
    public void setPropString(StructString propString)
    {
        Log.i(TAG, "got request from ue, setPropString" + (propString));
        mMessengerClient.setPropString(propString);
    }
    @Override
    public StructString getPropString()
    {
        Log.i(TAG, "got request from ue, getPropString");
        return mMessengerClient.getPropString();
    }
    
     @Override
     public StructBool funcBool(StructBool paramBool)
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
    public void funcBoolAsync(String callId, StructBool paramBool){
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
    public CompletableFuture<StructBool> funcBoolAsync(StructBool paramBool)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcBoolAsync(paramBool);
    }
     @Override
     public StructInt funcInt(StructInt paramInt)
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
    public void funcIntAsync(String callId, StructInt paramInt){
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
    public CompletableFuture<StructInt> funcIntAsync(StructInt paramInt)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcIntAsync(paramInt);
    }
     @Override
     public StructFloat funcFloat(StructFloat paramFloat)
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
    public void funcFloatAsync(String callId, StructFloat paramFloat){
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
    public CompletableFuture<StructFloat> funcFloatAsync(StructFloat paramFloat)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcFloatAsync(paramFloat);
    }
     @Override
     public StructString funcString(StructString paramString)
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
    public void funcStringAsync(String callId, StructString paramString){
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
    public CompletableFuture<StructString> funcStringAsync(StructString paramString)
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
            mMessengerClient = new StructInterfaceClient(ctx, connectionID);
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
    public void onPropBoolChanged(StructBool newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropBoolChanged(newValue);
    }
    @Override
    public void onPropIntChanged(StructInt newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropIntChanged(newValue);
    }
    @Override
    public void onPropFloatChanged(StructFloat newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropFloatChanged(newValue);
    }
    @Override
    public void onPropStringChanged(StructString newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropStringChanged(newValue);
    }
    @Override
    public void onSigBool(StructBool paramBool)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigBool "+ " " + paramBool);
        nativeOnSigBool(paramBool);
    }
    @Override
    public void onSigInt(StructInt paramInt)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigInt "+ " " + paramInt);
        nativeOnSigInt(paramInt);
    }
    @Override
    public void onSigFloat(StructFloat paramFloat)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigFloat "+ " " + paramFloat);
        nativeOnSigFloat(paramFloat);
    }
    @Override
    public void onSigString(StructString paramString)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigString "+ " " + paramString);
        nativeOnSigString(paramString);
    }
     private native void nativeOnPropBoolChanged(StructBool propBool);
     private native void nativeOnPropIntChanged(StructInt propInt);
     private native void nativeOnPropFloatChanged(StructFloat propFloat);
     private native void nativeOnPropStringChanged(StructString propString);
    private native void nativeOnSigBool(StructBool paramBool);
    private native void nativeOnSigInt(StructInt paramInt);
    private native void nativeOnSigFloat(StructFloat paramFloat);
    private native void nativeOnSigString(StructString paramString);
    private native void nativeOnFuncBoolResult(StructBool result, String callId);
    private native void nativeOnFuncIntResult(StructInt result, String callId);
    private native void nativeOnFuncFloatResult(StructFloat result, String callId);
    private native void nativeOnFuncStringResult(StructString result, String callId);
    private native void nativeAsyncOperationFailed(String callId, String errorMessage, int errorCode);
    private native void nativeIsReady(boolean isReady);
}
