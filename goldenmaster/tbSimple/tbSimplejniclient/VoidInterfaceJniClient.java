package tbSimple.tbSimplejniclient;

import tbSimple.tbSimple_api.IVoidInterface;
import tbSimple.tbSimple_api.AbstractVoidInterface;
import tbSimple.tbSimple_api.IVoidInterfaceEventListener;
import tbSimple.tbSimple_api.RemoteOperationException;

import tbSimple.tbSimple_android_client.VoidInterfaceClient;
import tbSimple.tbSimple_android_messenger.Conversions;
import android.content.Context;

import android.os.Bundle;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class VoidInterfaceJniClient extends AbstractVoidInterface implements IVoidInterfaceEventListener
{

    private static final String TAG = "VoidInterfaceJniClient";

    private VoidInterfaceClient mMessengerClient = null;


    private static String ModuleName = "tbSimple.tbSimplejniservice.VoidInterfaceJniService";
    private String lastServicePackage ="";

    @Override
    public boolean _isReady()
    {
        return mMessengerClient != null ? mMessengerClient._isReady() : false;
    }
    // Interface method — List types
    @Override
    public void funcVoid()
    {
        Log.v(TAG, "Blocking callfuncVoid - should not be used ");
         mMessengerClient.funcVoid();
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnFuncVoidResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void funcVoidAsync(String callId){
        Log.v(TAG, "non blocking call funcVoid ");
        mMessengerClient.funcVoidAsync().whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "funcVoid async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnFuncVoidResult(callId);
            }
        });
    }

    @Override
    public CompletableFuture<Void> funcVoidAsync()
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.funcVoidAsync();
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
            mMessengerClient = new VoidInterfaceClient(ctx, connectionID);
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
    public void onSigVoid()
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal sigVoid ");
        nativeOnSigVoid();
    }


    // Native declarations — array types for JNI compatibility
    private native void nativeOnSigVoid();
    private native void nativeOnFuncVoidResult(String callId);
    private native void nativeAsyncOperationFailed(String callId, String errorMessage, int errorCode);
    private native void nativeIsReady(boolean isReady);
}
