package tbEnum.tbEnumjniclient;

import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_api.AbstractEnumInterface;
import tbEnum.tbEnum_api.IEnumInterfaceEventListener;
import tbEnum.tbEnum_api.RemoteOperationException;

import tbEnum.tbEnum_android_client.EnumInterfaceClient;
import apigear.android.lifecycle.BindingLifecycleRegistry;
import apigear.android.lifecycle.IBindingLifecycleCoordinator;
import tbEnum.tbEnum_api.Enum0;
import tbEnum.tbEnum_android_messenger.Enum0Parcelable;
import tbEnum.tbEnum_api.Enum1;
import tbEnum.tbEnum_android_messenger.Enum1Parcelable;
import tbEnum.tbEnum_api.Enum2;
import tbEnum.tbEnum_android_messenger.Enum2Parcelable;
import tbEnum.tbEnum_api.Enum3;
import tbEnum.tbEnum_android_messenger.Enum3Parcelable;
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class EnumInterfaceJniClient extends AbstractEnumInterface implements IEnumInterfaceEventListener
{

    private static final String TAG = "EnumInterfaceJniClient";

    private EnumInterfaceClient mMessengerClient = null;


    private static String ModuleName = "tbEnum.tbEnumjniservice.EnumInterfaceJniService";
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
    public void setProp0(Enum0 prop0)
    {
        Log.i(TAG, "got request from ue, setProp0" + (prop0));
        mMessengerClient.setProp0(prop0);
    }
    @Override
    public Enum0 getProp0()
    {
        Log.i(TAG, "got request from ue, getProp0");
        return mMessengerClient.getProp0();
    }
    
    @Override
    public void setProp1(Enum1 prop1)
    {
        Log.i(TAG, "got request from ue, setProp1" + (prop1));
        mMessengerClient.setProp1(prop1);
    }
    @Override
    public Enum1 getProp1()
    {
        Log.i(TAG, "got request from ue, getProp1");
        return mMessengerClient.getProp1();
    }
    
    @Override
    public void setProp2(Enum2 prop2)
    {
        Log.i(TAG, "got request from ue, setProp2" + (prop2));
        mMessengerClient.setProp2(prop2);
    }
    @Override
    public Enum2 getProp2()
    {
        Log.i(TAG, "got request from ue, getProp2");
        return mMessengerClient.getProp2();
    }
    
    @Override
    public void setProp3(Enum3 prop3)
    {
        Log.i(TAG, "got request from ue, setProp3" + (prop3));
        mMessengerClient.setProp3(prop3);
    }
    @Override
    public Enum3 getProp3()
    {
        Log.i(TAG, "got request from ue, getProp3");
        return mMessengerClient.getProp3();
    }
    
     @Override
     public Enum0 func0(Enum0 param0)
     {
        Log.v(TAG, "Blocking callfunc0 - should not be used ");
        return mMessengerClient.func0(param0);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnFunc0Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void func0Async(String callId, Enum0 param0){
        Log.v(TAG, "non blocking call func0 ");
        mMessengerClient.func0Async(param0).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "func0 async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFunc0Result(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<Enum0> func0Async(Enum0 param0)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func0Async(param0);
    }
     @Override
     public Enum1 func1(Enum1 param1)
     {
        Log.v(TAG, "Blocking callfunc1 - should not be used ");
        return mMessengerClient.func1(param1);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnFunc1Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void func1Async(String callId, Enum1 param1){
        Log.v(TAG, "non blocking call func1 ");
        mMessengerClient.func1Async(param1).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "func1 async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFunc1Result(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<Enum1> func1Async(Enum1 param1)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func1Async(param1);
    }
     @Override
     public Enum2 func2(Enum2 param2)
     {
        Log.v(TAG, "Blocking callfunc2 - should not be used ");
        return mMessengerClient.func2(param2);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnFunc2Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void func2Async(String callId, Enum2 param2){
        Log.v(TAG, "non blocking call func2 ");
        mMessengerClient.func2Async(param2).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "func2 async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFunc2Result(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<Enum2> func2Async(Enum2 param2)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func2Async(param2);
    }
     @Override
     public Enum3 func3(Enum3 param3)
     {
        Log.v(TAG, "Blocking callfunc3 - should not be used ");
        return mMessengerClient.func3(param3);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnFunc3Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void func3Async(String callId, Enum3 param3){
        Log.v(TAG, "non blocking call func3 ");
        mMessengerClient.func3Async(param3).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "func3 async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFunc3Result(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<Enum3> func3Async(Enum3 param3)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func3Async(param3);
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
            mMessengerClient = new EnumInterfaceClient(ctx, connectionID);
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
    public void onProp0Changed(Enum0 newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp0Changed(newValue);
    }
    @Override
    public void onProp1Changed(Enum1 newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp1Changed(newValue);
    }
    @Override
    public void onProp2Changed(Enum2 newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp2Changed(newValue);
    }
    @Override
    public void onProp3Changed(Enum3 newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp3Changed(newValue);
    }
    @Override
    public void onSig0(Enum0 param0)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sig0 "+ " " + param0);
        nativeOnSig0(param0);
    }
    @Override
    public void onSig1(Enum1 param1)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sig1 "+ " " + param1);
        nativeOnSig1(param1);
    }
    @Override
    public void onSig2(Enum2 param2)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sig2 "+ " " + param2);
        nativeOnSig2(param2);
    }
    @Override
    public void onSig3(Enum3 param3)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sig3 "+ " " + param3);
        nativeOnSig3(param3);
    }
     private native void nativeOnProp0Changed(Enum0 prop0);
     private native void nativeOnProp1Changed(Enum1 prop1);
     private native void nativeOnProp2Changed(Enum2 prop2);
     private native void nativeOnProp3Changed(Enum3 prop3);
    private native void nativeOnSig0(Enum0 param0);
    private native void nativeOnSig1(Enum1 param1);
    private native void nativeOnSig2(Enum2 param2);
    private native void nativeOnSig3(Enum3 param3);
    private native void nativeOnFunc0Result(Enum0 result, String callId);
    private native void nativeOnFunc1Result(Enum1 result, String callId);
    private native void nativeOnFunc2Result(Enum2 result, String callId);
    private native void nativeOnFunc3Result(Enum3 result, String callId);
    private native void nativeAsyncOperationFailed(String callId, String errorMessage, int errorCode);
    private native void nativeIsReady(boolean isReady);
}
