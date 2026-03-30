package testbed2.testbed2jniclient;

import testbed2.testbed2_api.INestedStruct1Interface;
import testbed2.testbed2_api.AbstractNestedStruct1Interface;
import testbed2.testbed2_api.INestedStruct1InterfaceEventListener;
import testbed2.testbed2_api.RemoteOperationException;

import testbed2.testbed2_android_client.NestedStruct1InterfaceClient;
import testbed2.testbed2_android_messenger.Conversions;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_android_messenger.NestedStruct1Parcelable;
import android.content.Context;

import android.os.Bundle;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class NestedStruct1InterfaceJniClient extends AbstractNestedStruct1Interface implements INestedStruct1InterfaceEventListener
{

    private static final String TAG = "NestedStruct1InterfaceJniClient";

    private NestedStruct1InterfaceClient mMessengerClient = null;


    private static String ModuleName = "testbed2.testbed2jniservice.NestedStruct1InterfaceJniService";
    private String lastServicePackage ="";

    @Override
    public boolean _isReady()
    {
        return mMessengerClient != null ? mMessengerClient._isReady() : false;
    }
    // Interface method — List types
    @Override
    public void setProp1(NestedStruct1 prop1)
    {
        Log.i(TAG, "got request setProp1" + (prop1));
        mMessengerClient.setProp1(prop1);
    }
    @Override
    public NestedStruct1 getProp1()
    {
        Log.i(TAG, "got request getProp1");
        return mMessengerClient.getProp1();
    }


    
    // Interface method — List types
    @Override
    public void funcNoReturnValue(NestedStruct1 param1)
    {
        Log.v(TAG, "Blocking callfuncNoReturnValue - should not be used ");
         mMessengerClient.funcNoReturnValue(param1);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnFuncNoReturnValueResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcNoReturnValueAsync(String callId, NestedStruct1 param1){
        Log.v(TAG, "non blocking call funcNoReturnValue ");
        mMessengerClient.funcNoReturnValueAsync(param1).whenComplete((result, throwable) -> {
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
    public CompletableFuture<Void> funcNoReturnValueAsync(NestedStruct1 param1)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcNoReturnValueAsync(param1);
    }
    // Interface method — List types
    @Override
    public NestedStruct1 funcNoParams()
    {
        Log.v(TAG, "Blocking callfuncNoParams - should not be used ");
        return mMessengerClient.funcNoParams();
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
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
    public CompletableFuture<NestedStruct1> funcNoParamsAsync()
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcNoParamsAsync();
    }
    // Interface method — List types
    @Override
    public NestedStruct1 func1(NestedStruct1 param1)
    {
        Log.v(TAG, "Blocking callfunc1 - should not be used ");
        return mMessengerClient.func1(param1);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnFunc1Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void func1Async(String callId, NestedStruct1 param1){
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
    public CompletableFuture<NestedStruct1> func1Async(NestedStruct1 param1)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func1Async(param1);
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
            mMessengerClient = new NestedStruct1InterfaceClient(ctx, connectionID);
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
    public void onProp1Changed(NestedStruct1 newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp1Changed(newValue);
    }
    @Override
    public void onSig1(NestedStruct1 param1)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sig1 "+ " " + param1);
        nativeOnSig1(param1);
    }


    // Native declarations — array types for JNI compatibility
     private native void nativeOnProp1Changed(NestedStruct1 prop1);
    private native void nativeOnSig1(NestedStruct1 param1);
    private native void nativeOnFuncNoReturnValueResult(String callId);
    private native void nativeOnFuncNoParamsResult(NestedStruct1 result, String callId);
    private native void nativeOnFunc1Result(NestedStruct1 result, String callId);
    private native void nativeAsyncOperationFailed(String callId, String errorMessage, int errorCode);
    private native void nativeIsReady(boolean isReady);
}
