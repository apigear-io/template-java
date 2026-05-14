package tbRefIfaces.tbRefIfacesjniclient;

import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_api.AbstractParentIf;
import tbRefIfaces.tbRefIfaces_api.IParentIfEventListener;
import tbRefIfaces.tbRefIfaces_api.RemoteOperationException;

import tbRefIfaces.tbRefIfaces_android_client.ParentIfClient;
import apigear.android.lifecycle.BindingLifecycleRegistry;
import apigear.android.lifecycle.IBindingLifecycleCoordinator;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_android_messenger.SimpleLocalIfParcelable;
import android.content.Context;

import android.os.Bundle;
import java.util.concurrent.CompletableFuture;
import android.util.Log;



public class ParentIfJniClient extends AbstractParentIf implements IParentIfEventListener
{

    private static final String TAG = "ParentIfJniClient";

    private ParentIfClient mMessengerClient = null;


    private static String ModuleName = "tbRefIfaces.tbRefIfacesjniservice.ParentIfJniService";
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
    public void setLocalIf(ISimpleLocalIf localIf)
    {
        Log.i(TAG, "got request from ue, setLocalIf" + (localIf));
        mMessengerClient.setLocalIf(localIf);
    }
    @Override
    public ISimpleLocalIf getLocalIf()
    {
        Log.i(TAG, "got request from ue, getLocalIf");
        return mMessengerClient.getLocalIf();
    }
    
    @Override
    public void setLocalIfList(ISimpleLocalIf[] localIfList)
    {
        Log.i(TAG, "got request from ue, setLocalIfList" + (localIfList));
        mMessengerClient.setLocalIfList(localIfList);
    }
    @Override
    public ISimpleLocalIf[] getLocalIfList()
    {
        Log.i(TAG, "got request from ue, getLocalIfList");
        return mMessengerClient.getLocalIfList();
    }
    
    @Override
    public void setImportedIf(tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf)
    {
        Log.i(TAG, "got request from ue, setImportedIf" + (importedIf));
        mMessengerClient.setImportedIf(importedIf);
    }
    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf getImportedIf()
    {
        Log.i(TAG, "got request from ue, getImportedIf");
        return mMessengerClient.getImportedIf();
    }
    
    @Override
    public void setImportedIfList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfList)
    {
        Log.i(TAG, "got request from ue, setImportedIfList" + (importedIfList));
        mMessengerClient.setImportedIfList(importedIfList);
    }
    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf[] getImportedIfList()
    {
        Log.i(TAG, "got request from ue, getImportedIfList");
        return mMessengerClient.getImportedIfList();
    }
    
     @Override
     public ISimpleLocalIf localIfMethod(ISimpleLocalIf param)
     {
        Log.v(TAG, "Blocking calllocalIfMethod - should not be used ");
        return mMessengerClient.localIfMethod(param);
    }

    /**
    * This is an async method to be called via JNI.
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
     @Override
     public ISimpleLocalIf[] localIfMethodList(ISimpleLocalIf[] param)
     {
        Log.v(TAG, "Blocking calllocalIfMethodList - should not be used ");
        return mMessengerClient.localIfMethodList(param);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnLocalIfMethodListResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void localIfMethodListAsync(String callId, ISimpleLocalIf[] param){
        Log.v(TAG, "non blocking call localIfMethodList ");
        mMessengerClient.localIfMethodListAsync(param).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "localIfMethodList async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnLocalIfMethodListResult(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<ISimpleLocalIf[]> localIfMethodListAsync(ISimpleLocalIf[] param)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.localIfMethodListAsync(param);
    }
     @Override
     public tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIfMethod(tbIfaceimport.tbIfaceimport_api.IEmptyIf param)
     {
        Log.v(TAG, "Blocking callimportedIfMethod - should not be used ");
        return mMessengerClient.importedIfMethod(param);
    }

    /**
    * This is an async method to be called via JNI.
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
     @Override
     public tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfMethodList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param)
     {
        Log.v(TAG, "Blocking callimportedIfMethodList - should not be used ");
        return mMessengerClient.importedIfMethodList(param);
    }

    /**
    * This is an async method to be called via JNI.
    *
    * On success, calls nativeOnImportedIfMethodListResult with the same callId.
    * On failure, calls nativeAsyncOperationFailed with the callId and error message.
    * Exactly one of the two callbacks is guaranteed per invocation.
    *
    * @param callId async call identifier
    */
    public void importedIfMethodListAsync(String callId, tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param){
        Log.v(TAG, "non blocking call importedIfMethodList ");
        mMessengerClient.importedIfMethodListAsync(param).whenComplete((result, throwable) -> {
            if (throwable != null) {
                String errorMessage = throwable.getMessage() != null
                    ? throwable.getMessage() : throwable.getClass().getName();
                int errorCode = (throwable instanceof RemoteOperationException)
                    ? ((RemoteOperationException) throwable).getErrorCode() : 0;
                Log.w(TAG, "importedIfMethodList async failed: " + errorMessage);
                nativeAsyncOperationFailed(callId, errorMessage, errorCode);
            } else {
                nativeOnImportedIfMethodListResult(result, callId);
            }
        });
    }

    @Override
    public CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf[]> importedIfMethodListAsync(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param)
    {
        Log.v(TAG, "NON Blocking call method ");
        return mMessengerClient.importedIfMethodListAsync(param);
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

    //Event listener
    @Override
    public void onLocalIfChanged(ISimpleLocalIf newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnLocalIfChanged(newValue);
    }
    @Override
    public void onLocalIfListChanged(ISimpleLocalIf[] newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnLocalIfListChanged(newValue);
    }
    @Override
    public void onImportedIfChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnImportedIfChanged(newValue);
    }
    @Override
    public void onImportedIfListChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] newValue)
    {
        Log.i(TAG, "NOTIFICATION from messenger client " + newValue);
        nativeOnImportedIfListChanged(newValue);
    }
    @Override
    public void onLocalIfSignal(ISimpleLocalIf param)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal localIfSignal "+ " " + param);
        nativeOnLocalIfSignal(param);
    }
    @Override
    public void onLocalIfSignalList(ISimpleLocalIf[] param)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal localIfSignalList "+ " " + param);
        nativeOnLocalIfSignalList(param);
    }
    @Override
    public void onImportedIfSignal(tbIfaceimport.tbIfaceimport_api.IEmptyIf param)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal importedIfSignal "+ " " + param);
        nativeOnImportedIfSignal(param);
    }
    @Override
    public void onImportedIfSignalList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param)
    {
        Log.i(TAG, "NOTIFICATION from messenger client Signal importedIfSignalList "+ " " + param);
        nativeOnImportedIfSignalList(param);
    }
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
