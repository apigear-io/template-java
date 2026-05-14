package tbNames.tbNamesjniclient;

import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_api.AbstractNamEs;
import tbNames.tbNames_api.INamEsEventListener;
import tbNames.tbNames_api.RemoteOperationException;

import tbNames.tbNames_android_client.NamEsClient;
import apigear.android.lifecycle.BindingLifecycleRegistry;
import apigear.android.lifecycle.IBindingLifecycleCoordinator;
import tbNames.tbNames_api.EnumWithUnderScores;
import tbNames.tbNames_android_messenger.EnumWithUnderScoresParcelable;
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class NamEsJniClient extends AbstractNamEs implements INamEsEventListener
{

    private static final String TAG = "NamEsJniClient";

    private NamEsClient mMessengerClient = null;


    private static String ModuleName = "tbNames.tbNamesjniservice.NamEsJniService";
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
    public void setSwitch(boolean Switch)
    {
        Log.i(TAG, "got request from ue, setSwitch" + (Switch));
        mMessengerClient.setSwitch(Switch);
    }
    @Override
    public boolean getSwitch()
    {
        Log.i(TAG, "got request from ue, getSwitch");
        return mMessengerClient.getSwitch();
    }
    
    @Override
    public void setSomeProperty(int SOME_PROPERTY)
    {
        Log.i(TAG, "got request from ue, setSomeProperty" + (SOME_PROPERTY));
        mMessengerClient.setSomeProperty(SOME_PROPERTY);
    }
    @Override
    public int getSomeProperty()
    {
        Log.i(TAG, "got request from ue, getSomeProperty");
        return mMessengerClient.getSomeProperty();
    }
    
    @Override
    public void setSomePoperty2(int Some_Poperty2)
    {
        Log.i(TAG, "got request from ue, setSomePoperty2" + (Some_Poperty2));
        mMessengerClient.setSomePoperty2(Some_Poperty2);
    }
    @Override
    public int getSomePoperty2()
    {
        Log.i(TAG, "got request from ue, getSomePoperty2");
        return mMessengerClient.getSomePoperty2();
    }
    
    @Override
    public void setEnumProperty(EnumWithUnderScores enum_property)
    {
        Log.i(TAG, "got request from ue, setEnumProperty" + (enum_property));
        mMessengerClient.setEnumProperty(enum_property);
    }
    @Override
    public EnumWithUnderScores getEnumProperty()
    {
        Log.i(TAG, "got request from ue, getEnumProperty");
        return mMessengerClient.getEnumProperty();
    }
    
     @Override
     public void someFunction(boolean SOME_PARAM)
     {
        Log.v(TAG, "Blocking callsomeFunction - should not be used ");
         mMessengerClient.someFunction(SOME_PARAM);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnSomeFunctionResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void someFunctionAsync(String callId, boolean SOME_PARAM){
        Log.v(TAG, "non blocking call someFunction ");
        mMessengerClient.someFunctionAsync(SOME_PARAM).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "SOME_FUNCTION async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnSomeFunctionResult(callId);
            }
        });
    }

    @Override
    public CompletableFuture<Void> someFunctionAsync(boolean SOME_PARAM)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.someFunctionAsync(SOME_PARAM);
    }
     @Override
     public void someFunction2(boolean Some_Param)
     {
        Log.v(TAG, "Blocking callsomeFunction2 - should not be used ");
         mMessengerClient.someFunction2(Some_Param);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnSomeFunction2Result with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void someFunction2Async(String callId, boolean Some_Param){
        Log.v(TAG, "non blocking call someFunction2 ");
        mMessengerClient.someFunction2Async(Some_Param).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "Some_Function2 async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnSomeFunction2Result(callId);
            }
        });
    }

    @Override
    public CompletableFuture<Void> someFunction2Async(boolean Some_Param)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.someFunction2Async(Some_Param);
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
            mMessengerClient = new NamEsClient(ctx, connectionID);
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
    public void onSwitchChanged(boolean newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnSwitchChanged(newValue);
    }
    @Override
    public void onSomePropertyChanged(int newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnSomePropertyChanged(newValue);
    }
    @Override
    public void onSomePoperty2Changed(int newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnSomePoperty2Changed(newValue);
    }
    @Override
    public void onEnumPropertyChanged(EnumWithUnderScores newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnEnumPropertyChanged(newValue);
    }
    @Override
    public void onSomeSignal(boolean SOME_PARAM)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal SOME_SIGNAL "+ " " + SOME_PARAM);
        nativeOnSomeSignal(SOME_PARAM);
    }
    @Override
    public void onSomeSignal2(boolean Some_Param)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal Some_Signal2 "+ " " + Some_Param);
        nativeOnSomeSignal2(Some_Param);
    }
     private native void nativeOnSwitchChanged(boolean Switch);
     private native void nativeOnSomePropertyChanged(int SOME_PROPERTY);
     private native void nativeOnSomePoperty2Changed(int Some_Poperty2);
     private native void nativeOnEnumPropertyChanged(EnumWithUnderScores enum_property);
    private native void nativeOnSomeSignal(boolean SOME_PARAM);
    private native void nativeOnSomeSignal2(boolean Some_Param);
    private native void nativeOnSomeFunctionResult(String callId);
    private native void nativeOnSomeFunction2Result(String callId);
    private native void nativeAsyncOperationFailed(String callId, String errorMessage, int errorCode);
    private native void nativeIsReady(boolean isReady);
}
