package testbed2.testbed2jniclient;

import testbed2.testbed2_api.INestedStruct3Interface;
import testbed2.testbed2_api.AbstractNestedStruct3Interface;
import testbed2.testbed2_api.INestedStruct3InterfaceEventListener;
import testbed2.testbed2_api.RemoteOperationException;

import testbed2.testbed2_android_client.NestedStruct3InterfaceClient;
import testbed2.testbed2_android_messenger.Conversions;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_android_messenger.NestedStruct1Parcelable;
import testbed2.testbed2_api.NestedStruct2;
import testbed2.testbed2_android_messenger.NestedStruct2Parcelable;
import testbed2.testbed2_api.NestedStruct3;
import testbed2.testbed2_android_messenger.NestedStruct3Parcelable;
import android.content.Context;

import android.os.Bundle;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class NestedStruct3InterfaceJniClient extends AbstractNestedStruct3Interface implements INestedStruct3InterfaceEventListener
{

    private static final String TAG = "NestedStruct3InterfaceJniClient";

    private NestedStruct3InterfaceClient mMessengerClient = null;


    private static String ModuleName = "testbed2.testbed2jniservice.NestedStruct3InterfaceJniService";
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
    public void setProp2(NestedStruct2 prop2)
    {
        Log.i(TAG, "got request setProp2" + (prop2));
        mMessengerClient.setProp2(prop2);
    }
    @Override
    public NestedStruct2 getProp2()
    {
        Log.i(TAG, "got request getProp2");
        return mMessengerClient.getProp2();
    }


    
    // Interface method — List types
    @Override
    public void setProp3(NestedStruct3 prop3)
    {
        Log.i(TAG, "got request setProp3" + (prop3));
        mMessengerClient.setProp3(prop3);
    }
    @Override
    public NestedStruct3 getProp3()
    {
        Log.i(TAG, "got request getProp3");
        return mMessengerClient.getProp3();
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
    // Interface method — List types
    @Override
    public NestedStruct1 func2(NestedStruct1 param1, NestedStruct2 param2)
    {
        Log.v(TAG, "Blocking callfunc2 - should not be used ");
        return mMessengerClient.func2(param1, param2);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnFunc2Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void func2Async(String callId, NestedStruct1 param1, NestedStruct2 param2){
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
    public CompletableFuture<NestedStruct1> func2Async(NestedStruct1 param1, NestedStruct2 param2)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func2Async(param1, param2);
    }
    // Interface method — List types
    @Override
    public NestedStruct1 func3(NestedStruct1 param1, NestedStruct2 param2, NestedStruct3 param3)
    {
        Log.v(TAG, "Blocking callfunc3 - should not be used ");
        return mMessengerClient.func3(param1, param2, param3);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnFunc3Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void func3Async(String callId, NestedStruct1 param1, NestedStruct2 param2, NestedStruct3 param3){
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
    public CompletableFuture<NestedStruct1> func3Async(NestedStruct1 param1, NestedStruct2 param2, NestedStruct3 param3)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.func3Async(param1, param2, param3);
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
            mMessengerClient = new NestedStruct3InterfaceClient(ctx, connectionID);
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
    public void onProp2Changed(NestedStruct2 newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp2Changed(newValue);
    }
    @Override
    public void onProp3Changed(NestedStruct3 newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnProp3Changed(newValue);
    }
    @Override
    public void onSig1(NestedStruct1 param1)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sig1 "+ " " + param1);
        nativeOnSig1(param1);
    }
    @Override
    public void onSig2(NestedStruct1 param1, NestedStruct2 param2)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sig2 "+ " " + param1+ " " + param2);
        nativeOnSig2(param1, param2);
    }
    @Override
    public void onSig3(NestedStruct1 param1, NestedStruct2 param2, NestedStruct3 param3)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sig3 "+ " " + param1+ " " + param2+ " " + param3);
        nativeOnSig3(param1, param2, param3);
    }


    // Native declarations — array types for JNI compatibility
     private native void nativeOnProp1Changed(NestedStruct1 prop1);
     private native void nativeOnProp2Changed(NestedStruct2 prop2);
     private native void nativeOnProp3Changed(NestedStruct3 prop3);
    private native void nativeOnSig1(NestedStruct1 param1);
    private native void nativeOnSig2(NestedStruct1 param1, NestedStruct2 param2);
    private native void nativeOnSig3(NestedStruct1 param1, NestedStruct2 param2, NestedStruct3 param3);
    private native void nativeOnFunc1Result(NestedStruct1 result, String callId);
    private native void nativeOnFunc2Result(NestedStruct1 result, String callId);
    private native void nativeOnFunc3Result(NestedStruct1 result, String callId);
    private native void nativeAsyncOperationFailed(String callId, String errorMessage, int errorCode);
    private native void nativeIsReady(boolean isReady);
}
