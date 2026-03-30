package tbRefIfaces.tbRefIfacesjniclient;

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_api.AbstractSimpleLocalIf;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIfEventListener;
import tbRefIfaces.tbRefIfaces_api.RemoteOperationException;

import tbRefIfaces.tbRefIfaces_android_client.SimpleLocalIfClient;
import tbRefIfaces.tbRefIfaces_android_messenger.Conversions;
import android.content.Context;

import android.os.Bundle;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class SimpleLocalIfJniClient extends AbstractSimpleLocalIf implements ISimpleLocalIfEventListener
{

    private static final String TAG = "SimpleLocalIfJniClient";

    private SimpleLocalIfClient mMessengerClient = null;


    private static String ModuleName = "tbRefIfaces.tbRefIfacesjniservice.SimpleLocalIfJniService";
    private String lastServicePackage ="";

    @Override
    public boolean _isReady()
    {
        return mMessengerClient != null ? mMessengerClient._isReady() : false;
    }
    // Interface method — List types
    @Override
    public void setIntProperty(int intProperty)
    {
        Log.i(TAG, "got request setIntProperty" + (intProperty));
        mMessengerClient.setIntProperty(intProperty);
    }
    @Override
    public int getIntProperty()
    {
        Log.i(TAG, "got request getIntProperty");
        return mMessengerClient.getIntProperty();
    }


    
    // Interface method — List types
    @Override
    public int intMethod(int param)
    {
        Log.v(TAG, "Blocking callintMethod - should not be used ");
        return mMessengerClient.intMethod(param);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnIntMethodResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void intMethodAsync(String callId, int param){
        Log.v(TAG, "non blocking call intMethod ");
        mMessengerClient.intMethodAsync(param).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "intMethod async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnIntMethodResult(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<Integer> intMethodAsync(int param)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.intMethodAsync(param);
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
            mMessengerClient = new SimpleLocalIfClient(ctx, connectionID);
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
    public void onIntPropertyChanged(int newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnIntPropertyChanged(newValue);
    }
    @Override
    public void onIntSignal(int param)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal intSignal "+ " " + param);
        nativeOnIntSignal(param);
    }


    // Native declarations — array types for JNI compatibility
     private native void nativeOnIntPropertyChanged(int intProperty);
    private native void nativeOnIntSignal(int param);
    private native void nativeOnIntMethodResult(int result, String callId);
    private native void nativeAsyncOperationFailed(String callId, String errorMessage, int errorCode);
    private native void nativeIsReady(boolean isReady);
}
