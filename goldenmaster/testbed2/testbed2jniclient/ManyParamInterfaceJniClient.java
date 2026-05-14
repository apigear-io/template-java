package testbed2.testbed2jniclient;

import testbed2.testbed2_api.IManyParamInterface;
import testbed2.testbed2_api.AbstractManyParamInterface;
import testbed2.testbed2_api.IManyParamInterfaceEventListener;
import testbed2.testbed2_api.RemoteOperationException;

import testbed2.testbed2_android_client.ManyParamInterfaceClient;
import apigear.android.lifecycle.BindingLifecycleRegistry;
import apigear.android.lifecycle.IBindingLifecycleCoordinator;
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class ManyParamInterfaceJniClient extends AbstractManyParamInterface implements IManyParamInterfaceEventListener
{

    private static final String TAG = "ManyParamInterfaceJniClient";

    private ManyParamInterfaceClient mMessengerClient = null;


    private static String ModuleName = "testbed2.testbed2jniservice.ManyParamInterfaceJniService";
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
    public void setProp1(int prop1)
    {
        Log.i(TAG, "got request from ue, setProp1" + (prop1));
        mMessengerClient.setProp1(prop1);
    }
    @Override
    public int getProp1()
    {
        Log.i(TAG, "got request from ue, getProp1");
        return mMessengerClient.getProp1();
    }
    
    @Override
    public void setProp2(int prop2)
    {
        Log.i(TAG, "got request from ue, setProp2" + (prop2));
        mMessengerClient.setProp2(prop2);
    }
    @Override
    public int getProp2()
    {
        Log.i(TAG, "got request from ue, getProp2");
        return mMessengerClient.getProp2();
    }
    
    @Override
    public void setProp3(int prop3)
    {
        Log.i(TAG, "got request from ue, setProp3" + (prop3));
        mMessengerClient.setProp3(prop3);
    }
    @Override
    public int getProp3()
    {
        Log.i(TAG, "got request from ue, getProp3");
        return mMessengerClient.getProp3();
    }
    
    @Override
    public void setProp4(int prop4)
    {
        Log.i(TAG, "got request from ue, setProp4" + (prop4));
        mMessengerClient.setProp4(prop4);
    }
    @Override
    public int getProp4()
    {
        Log.i(TAG, "got request from ue, getProp4");
        return mMessengerClient.getProp4();
    }
    
     @Override
     public int func1(int param1)
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
    public void func1Async(String callId, int param1){
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
    public CompletableFuture<Integer> func1Async(int param1)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func1Async(param1);
    }
     @Override
     public int func2(int param1, int param2)
     {
        Log.v(TAG, "Blocking callfunc2 - should not be used ");
        return mMessengerClient.func2(param1, param2);
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
    public void func2Async(String callId, int param1, int param2){
        Log.v(TAG, "non blocking call func2 ");
        mMessengerClient.func2Async(param1, param2).whenComplete((result, throwable) -> {
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
    public CompletableFuture<Integer> func2Async(int param1, int param2)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func2Async(param1, param2);
    }
     @Override
     public int func3(int param1, int param2, int param3)
     {
        Log.v(TAG, "Blocking callfunc3 - should not be used ");
        return mMessengerClient.func3(param1, param2, param3);
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
    public void func3Async(String callId, int param1, int param2, int param3){
        Log.v(TAG, "non blocking call func3 ");
        mMessengerClient.func3Async(param1, param2, param3).whenComplete((result, throwable) -> {
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
    public CompletableFuture<Integer> func3Async(int param1, int param2, int param3)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func3Async(param1, param2, param3);
    }
     @Override
     public int func4(int param1, int param2, int param3, int param4)
     {
        Log.v(TAG, "Blocking callfunc4 - should not be used ");
        return mMessengerClient.func4(param1, param2, param3, param4);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnFunc4Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void func4Async(String callId, int param1, int param2, int param3, int param4){
        Log.v(TAG, "non blocking call func4 ");
        mMessengerClient.func4Async(param1, param2, param3, param4).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "func4 async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFunc4Result(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<Integer> func4Async(int param1, int param2, int param3, int param4)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func4Async(param1, param2, param3, param4);
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
            mMessengerClient = new ManyParamInterfaceClient(ctx, connectionID);
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
    public void onProp1Changed(int newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp1Changed(newValue);
    }
    @Override
    public void onProp2Changed(int newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp2Changed(newValue);
    }
    @Override
    public void onProp3Changed(int newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp3Changed(newValue);
    }
    @Override
    public void onProp4Changed(int newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp4Changed(newValue);
    }
    @Override
    public void onSig1(int param1)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sig1 "+ " " + param1);
        nativeOnSig1(param1);
    }
    @Override
    public void onSig2(int param1, int param2)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sig2 "+ " " + param1+ " " + param2);
        nativeOnSig2(param1, param2);
    }
    @Override
    public void onSig3(int param1, int param2, int param3)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sig3 "+ " " + param1+ " " + param2+ " " + param3);
        nativeOnSig3(param1, param2, param3);
    }
    @Override
    public void onSig4(int param1, int param2, int param3, int param4)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sig4 "+ " " + param1+ " " + param2+ " " + param3+ " " + param4);
        nativeOnSig4(param1, param2, param3, param4);
    }
     private native void nativeOnProp1Changed(int prop1);
     private native void nativeOnProp2Changed(int prop2);
     private native void nativeOnProp3Changed(int prop3);
     private native void nativeOnProp4Changed(int prop4);
    private native void nativeOnSig1(int param1);
    private native void nativeOnSig2(int param1, int param2);
    private native void nativeOnSig3(int param1, int param2, int param3);
    private native void nativeOnSig4(int param1, int param2, int param3, int param4);
    private native void nativeOnFunc1Result(int result, String callId);
    private native void nativeOnFunc2Result(int result, String callId);
    private native void nativeOnFunc3Result(int result, String callId);
    private native void nativeOnFunc4Result(int result, String callId);
    private native void nativeAsyncOperationFailed(String callId, String errorMessage, int errorCode);
    private native void nativeIsReady(boolean isReady);
}
