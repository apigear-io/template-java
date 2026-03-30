package tbRefIfaces.tbRefIfacesjniclient;

import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_api.AbstractParentIf;
import tbRefIfaces.tbRefIfaces_api.IParentIfEventListener;
import tbRefIfaces.tbRefIfaces_api.RemoteOperationException;

import tbRefIfaces.tbRefIfaces_android_client.ParentIfClient;
import tbRefIfaces.tbRefIfaces_android_messenger.Conversions;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_android_messenger.SimpleLocalIfParcelable;
import android.content.Context;

import android.os.Bundle;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class ParentIfJniClient extends AbstractParentIf implements IParentIfEventListener
{

    private static final String TAG = "ParentIfJniClient";

    private ParentIfClient mMessengerClient = null;


    private static String ModuleName = "tbRefIfaces.tbRefIfacesjniservice.ParentIfJniService";
    private String lastServicePackage ="";

    @Override
    public boolean _isReady()
    {
        return mMessengerClient != null ? mMessengerClient._isReady() : false;
    }
    // Interface method — List types
    @Override
    public void setLocalIf(ISimpleLocalIf localIf)
    {
        Log.i(TAG, "got request setLocalIf" + (localIf));
        mMessengerClient.setLocalIf(localIf);
    }
    @Override
    public ISimpleLocalIf getLocalIf()
    {
        Log.i(TAG, "got request getLocalIf");
        return mMessengerClient.getLocalIf();
    }


    
    // Interface method — List types
    @Override
    public void setLocalIfList(List<ISimpleLocalIf> localIfList)
    {
        Log.i(TAG, "got request setLocalIfList" + (localIfList));
        mMessengerClient.setLocalIfList(localIfList);
    }
    // JNI entry point — array types for C++ compatibility
    public void setLocalIfList(ISimpleLocalIf[] localIfList)
    {
        Log.i(TAG, "got JNI request setLocalIfList");
        setLocalIfList(Conversions.toList(localIfList));
    }
    @Override
    public List<ISimpleLocalIf> getLocalIfList()
    {
        Log.i(TAG, "got request getLocalIfList");
        return mMessengerClient.getLocalIfList();
    }


    
    // Interface method — List types
    @Override
    public void setImportedIf(tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf)
    {
        Log.i(TAG, "got request setImportedIf" + (importedIf));
        mMessengerClient.setImportedIf(importedIf);
    }
    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf getImportedIf()
    {
        Log.i(TAG, "got request getImportedIf");
        return mMessengerClient.getImportedIf();
    }


    
    // Interface method — List types
    @Override
    public void setImportedIfList(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> importedIfList)
    {
        Log.i(TAG, "got request setImportedIfList" + (importedIfList));
        mMessengerClient.setImportedIfList(importedIfList);
    }
    // JNI entry point — array types for C++ compatibility
    public void setImportedIfList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfList)
    {
        Log.i(TAG, "got JNI request setImportedIfList");
        setImportedIfList(Conversions.toList(importedIfList));
    }
    @Override
    public List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> getImportedIfList()
    {
        Log.i(TAG, "got request getImportedIfList");
        return mMessengerClient.getImportedIfList();
    }


    
    // Interface method — List types
    @Override
    public ISimpleLocalIf localIfMethod(ISimpleLocalIf param)
    {
        Log.v(TAG, "Blocking calllocalIfMethod - should not be used ");
        return mMessengerClient.localIfMethod(param);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnLocalIfMethodResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void localIfMethodAsync(String callId, ISimpleLocalIf param){
        Log.v(TAG, "non blocking call localIfMethod ");
        mMessengerClient.localIfMethodAsync(param).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "localIfMethod async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnLocalIfMethodResult(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<ISimpleLocalIf> localIfMethodAsync(ISimpleLocalIf param)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.localIfMethodAsync(param);
    }
    // Interface method — List types
    @Override
    public List<ISimpleLocalIf> localIfMethodList(List<ISimpleLocalIf> param)
    {
        Log.v(TAG, "Blocking calllocalIfMethodList - should not be used ");
        return mMessengerClient.localIfMethodList(param);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnLocalIfMethodListResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void localIfMethodListAsync(String callId, ISimpleLocalIf[] param){
        Log.v(TAG, "non blocking call localIfMethodList ");
        mMessengerClient.localIfMethodListAsync(Conversions.toList(param)).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "localIfMethodList async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnLocalIfMethodListResult(Conversions.toArray(result, new ISimpleLocalIf[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<ISimpleLocalIf>> localIfMethodListAsync(List<ISimpleLocalIf> param)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.localIfMethodListAsync(param);
    }
    // Interface method — List types
    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIfMethod(tbIfaceimport.tbIfaceimport_api.IEmptyIf param)
    {
        Log.v(TAG, "Blocking callimportedIfMethod - should not be used ");
        return mMessengerClient.importedIfMethod(param);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnImportedIfMethodResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void importedIfMethodAsync(String callId, tbIfaceimport.tbIfaceimport_api.IEmptyIf param){
        Log.v(TAG, "non blocking call importedIfMethod ");
        mMessengerClient.importedIfMethodAsync(param).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "importedIfMethod async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnImportedIfMethodResult(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf> importedIfMethodAsync(tbIfaceimport.tbIfaceimport_api.IEmptyIf param)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.importedIfMethodAsync(param);
    }
    // Interface method — List types
    @Override
    public List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> importedIfMethodList(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> param)
    {
        Log.v(TAG, "Blocking callimportedIfMethodList - should not be used ");
        return mMessengerClient.importedIfMethodList(param);
    }

    /**
    * JNI async entry point — uses array types for C++ compatibility.
    *
    * On success, calls nativeOnImportedIfMethodListResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void importedIfMethodListAsync(String callId, tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param){
        Log.v(TAG, "non blocking call importedIfMethodList ");
        mMessengerClient.importedIfMethodListAsync(Conversions.toList(param)).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "importedIfMethodList async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnImportedIfMethodListResult(Conversions.toArray(result, new tbIfaceimport.tbIfaceimport_api.IEmptyIf[0]), callId);
            }
        });
    }

    @Override
    public CompletableFuture<List<tbIfaceimport.tbIfaceimport_api.IEmptyIf>> importedIfMethodListAsync(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> param)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.importedIfMethodListAsync(param);
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
            mMessengerClient = new ParentIfClient(ctx, connectionID);
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
    public void onLocalIfChanged(ISimpleLocalIf newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnLocalIfChanged(newValue);
    }
    @Override
    public void onLocalIfListChanged(List<ISimpleLocalIf> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnLocalIfListChanged(Conversions.toArray(newValue, new ISimpleLocalIf[0]));
    }
    @Override
    public void onImportedIfChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnImportedIfChanged(newValue);
    }
    @Override
    public void onImportedIfListChanged(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnImportedIfListChanged(Conversions.toArray(newValue, new tbIfaceimport.tbIfaceimport_api.IEmptyIf[0]));
    }
    @Override
    public void onLocalIfSignal(ISimpleLocalIf param)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal localIfSignal "+ " " + param);
        nativeOnLocalIfSignal(param);
    }
    @Override
    public void onLocalIfSignalList(List<ISimpleLocalIf> param)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal localIfSignalList "+ " " + param);
        nativeOnLocalIfSignalList(Conversions.toArray(param, new ISimpleLocalIf[0]));
    }
    @Override
    public void onImportedIfSignal(tbIfaceimport.tbIfaceimport_api.IEmptyIf param)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal importedIfSignal "+ " " + param);
        nativeOnImportedIfSignal(param);
    }
    @Override
    public void onImportedIfSignalList(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> param)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal importedIfSignalList "+ " " + param);
        nativeOnImportedIfSignalList(Conversions.toArray(param, new tbIfaceimport.tbIfaceimport_api.IEmptyIf[0]));
    }


    // Native declarations — array types for JNI compatibility
     private native void nativeOnLocalIfChanged(ISimpleLocalIf localIf);
     private native void nativeOnLocalIfListChanged(ISimpleLocalIf[] localIfList);
     private native void nativeOnImportedIfChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf);
     private native void nativeOnImportedIfListChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfList);
    private native void nativeOnLocalIfSignal(ISimpleLocalIf param);
    private native void nativeOnLocalIfSignalList(ISimpleLocalIf[] param);
    private native void nativeOnImportedIfSignal(tbIfaceimport.tbIfaceimport_api.IEmptyIf param);
    private native void nativeOnImportedIfSignalList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param);
    private native void nativeOnLocalIfMethodResult(ISimpleLocalIf result, String callId);
    private native void nativeOnLocalIfMethodListResult(ISimpleLocalIf[] result, String callId);
    private native void nativeOnImportedIfMethodResult(tbIfaceimport.tbIfaceimport_api.IEmptyIf result, String callId);
    private native void nativeOnImportedIfMethodListResult(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] result, String callId);
    private native void nativeAsyncOperationFailed(String callId, String errorMessage, int errorCode);
    private native void nativeIsReady(boolean isReady);
}
