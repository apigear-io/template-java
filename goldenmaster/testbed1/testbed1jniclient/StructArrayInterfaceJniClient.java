package testbed1.testbed1jniclient;

import testbed1.testbed1_api.IStructArrayInterface;
import testbed1.testbed1_api.AbstractStructArrayInterface;
import testbed1.testbed1_api.IStructArrayInterfaceEventListener;
import testbed1.testbed1_api.RemoteOperationException;

import testbed1.testbed1_android_client.StructArrayInterfaceClient;
import testbed1.testbed1_android_messenger.Conversions;
import testbed1.testbed1_api.Enum0;
import testbed1.testbed1_android_messenger.Enum0Parcelable;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class StructArrayInterfaceJniClient extends AbstractStructArrayInterface implements IStructArrayInterfaceEventListener
{

    private static final String TAG = "StructArrayInterfaceJniClient";

    private StructArrayInterfaceClient mMessengerClient = null;


    private static String ModuleName = "testbed1.testbed1jniservice.StructArrayInterfaceJniService";
    private String lastServicePackage ="";

    @Override
    public boolean _isReady()
    {
        return mMessengerClient != null ? mMessengerClient._isReady() : false;
    }
    // Interface method — List types
    @Override
    public void setPropBool(List<StructBool> propBool)
    {
        Log.i(TAG, "got request setPropBool" + (propBool));
        mMessengerClient.setPropBool(propBool);
    }
    // JNI entry point — array types for C++ compatibility
    public void setPropBool(StructBool[] propBool)
    {
        Log.i(TAG, "got JNI request setPropBool");
        setPropBool(Conversions.toList(propBool));
    }
    @Override
    public List<StructBool> getPropBool()
    {
        Log.i(TAG, "got request getPropBool");
        return mMessengerClient.getPropBool();
    }


    
    // Interface method — List types
    @Override
    public void setPropInt(List<StructInt> propInt)
    {
        Log.i(TAG, "got request setPropInt" + (propInt));
        mMessengerClient.setPropInt(propInt);
    }
    // JNI entry point — array types for C++ compatibility
    public void setPropInt(StructInt[] propInt)
    {
        Log.i(TAG, "got JNI request setPropInt");
        setPropInt(Conversions.toList(propInt));
    }
    @Override
    public List<StructInt> getPropInt()
    {
        Log.i(TAG, "got request getPropInt");
        return mMessengerClient.getPropInt();
    }


    
    // Interface method — List types
    @Override
    public void setPropFloat(List<StructFloat> propFloat)
    {
        Log.i(TAG, "got request setPropFloat" + (propFloat));
        mMessengerClient.setPropFloat(propFloat);
    }
    // JNI entry point — array types for C++ compatibility
    public void setPropFloat(StructFloat[] propFloat)
    {
        Log.i(TAG, "got JNI request setPropFloat");
        setPropFloat(Conversions.toList(propFloat));
    }
    @Override
    public List<StructFloat> getPropFloat()
    {
        Log.i(TAG, "got request getPropFloat");
        return mMessengerClient.getPropFloat();
    }


    
    // Interface method — List types
    @Override
    public void setPropString(List<StructString> propString)
    {
        Log.i(TAG, "got request setPropString" + (propString));
        mMessengerClient.setPropString(propString);
    }
    // JNI entry point — array types for C++ compatibility
    public void setPropString(StructString[] propString)
    {
        Log.i(TAG, "got JNI request setPropString");
        setPropString(Conversions.toList(propString));
    }
    @Override
    public List<StructString> getPropString()
    {
        Log.i(TAG, "got request getPropString");
        return mMessengerClient.getPropString();
    }


    
    // Interface method — List types
    @Override
    public void setPropEnum(List<Enum0> propEnum)
    {
        Log.i(TAG, "got request setPropEnum" + (propEnum));
        mMessengerClient.setPropEnum(propEnum);
    }
    // JNI entry point — array types for C++ compatibility
    public void setPropEnum(Enum0[] propEnum)
    {
        Log.i(TAG, "got JNI request setPropEnum");
        setPropEnum(Conversions.toList(propEnum));
    }
    @Override
    public List<Enum0> getPropEnum()
    {
        Log.i(TAG, "got request getPropEnum");
        return mMessengerClient.getPropEnum();
    }


    
    // Interface method — List types
    @Override
    public List<StructBool> funcBool(List<StructBool> paramBool)
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
    public void funcBoolAsync(String callId, StructBool[] paramBool){
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
                nativeOnFuncBoolResult(Conversions.toArray(result, new StructBool[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<StructBool>> funcBoolAsync(List<StructBool> paramBool)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcBoolAsync(paramBool);
    }
    // Interface method — List types
    @Override
    public List<StructInt> funcInt(List<StructInt> paramInt)
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
    public void funcIntAsync(String callId, StructInt[] paramInt){
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
                nativeOnFuncIntResult(Conversions.toArray(result, new StructInt[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<StructInt>> funcIntAsync(List<StructInt> paramInt)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcIntAsync(paramInt);
    }
    // Interface method — List types
    @Override
    public List<StructFloat> funcFloat(List<StructFloat> paramFloat)
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
    public void funcFloatAsync(String callId, StructFloat[] paramFloat){
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
                nativeOnFuncFloatResult(Conversions.toArray(result, new StructFloat[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<StructFloat>> funcFloatAsync(List<StructFloat> paramFloat)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcFloatAsync(paramFloat);
    }
    // Interface method — List types
    @Override
    public List<StructString> funcString(List<StructString> paramString)
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
    public void funcStringAsync(String callId, StructString[] paramString){
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
                nativeOnFuncStringResult(Conversions.toArray(result, new StructString[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<StructString>> funcStringAsync(List<StructString> paramString)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcStringAsync(paramString);
    }
    // Interface method — List types
    @Override
    public List<Enum0> funcEnum(List<Enum0> paramEnum)
    {
        Log.v(TAG, "Blocking callfuncEnum - should not be used ");
        return mMessengerClient.funcEnum(paramEnum);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnFuncEnumResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcEnumAsync(String callId, Enum0[] paramEnum){
        Log.v(TAG, "non blocking call funcEnum ");
        mMessengerClient.funcEnumAsync(Conversions.toList(paramEnum)).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcEnum async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncEnumResult(Conversions.toArray(result, new Enum0[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<Enum0>> funcEnumAsync(List<Enum0> paramEnum)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcEnumAsync(paramEnum);
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
            mMessengerClient = new StructArrayInterfaceClient(ctx, connectionID);
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
    public void onPropBoolChanged(List<StructBool> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropBoolChanged(Conversions.toArray(newValue, new StructBool[0]));
    }
    @Override
    public void onPropIntChanged(List<StructInt> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropIntChanged(Conversions.toArray(newValue, new StructInt[0]));
    }
    @Override
    public void onPropFloatChanged(List<StructFloat> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropFloatChanged(Conversions.toArray(newValue, new StructFloat[0]));
    }
    @Override
    public void onPropStringChanged(List<StructString> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropStringChanged(Conversions.toArray(newValue, new StructString[0]));
    }
    @Override
    public void onPropEnumChanged(List<Enum0> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnPropEnumChanged(Conversions.toArray(newValue, new Enum0[0]));
    }
    @Override
    public void onSigBool(List<StructBool> paramBool)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigBool "+ " " + paramBool);
        nativeOnSigBool(Conversions.toArray(paramBool, new StructBool[0]));
    }
    @Override
    public void onSigInt(List<StructInt> paramInt)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigInt "+ " " + paramInt);
        nativeOnSigInt(Conversions.toArray(paramInt, new StructInt[0]));
    }
    @Override
    public void onSigFloat(List<StructFloat> paramFloat)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigFloat "+ " " + paramFloat);
        nativeOnSigFloat(Conversions.toArray(paramFloat, new StructFloat[0]));
    }
    @Override
    public void onSigString(List<StructString> paramString)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigString "+ " " + paramString);
        nativeOnSigString(Conversions.toArray(paramString, new StructString[0]));
    }
    @Override
    public void onSigEnum(List<Enum0> paramEnum)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigEnum "+ " " + paramEnum);
        nativeOnSigEnum(Conversions.toArray(paramEnum, new Enum0[0]));
    }


    // Native declarations — array types for JNI compatibility
     private native void nativeOnPropBoolChanged(StructBool[] propBool);
     private native void nativeOnPropIntChanged(StructInt[] propInt);
     private native void nativeOnPropFloatChanged(StructFloat[] propFloat);
     private native void nativeOnPropStringChanged(StructString[] propString);
     private native void nativeOnPropEnumChanged(Enum0[] propEnum);
    private native void nativeOnSigBool(StructBool[] paramBool);
    private native void nativeOnSigInt(StructInt[] paramInt);
    private native void nativeOnSigFloat(StructFloat[] paramFloat);
    private native void nativeOnSigString(StructString[] paramString);
    private native void nativeOnSigEnum(Enum0[] paramEnum);
    private native void nativeOnFuncBoolResult(StructBool[] result, String callId);
    private native void nativeOnFuncIntResult(StructInt[] result, String callId);
    private native void nativeOnFuncFloatResult(StructFloat[] result, String callId);
    private native void nativeOnFuncStringResult(StructString[] result, String callId);
    private native void nativeOnFuncEnumResult(Enum0[] result, String callId);
    private native void nativeAsyncOperationFailed(String callId, String errorMessage, int errorCode);
    private native void nativeIsReady(boolean isReady);
}
