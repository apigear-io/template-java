package tbRefIfaces.tbRefIfacesjniservice;

import android.os.Messenger;
import android.util.Log;

import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_api.AbstractParentIf;
import tbRefIfaces.tbRefIfaces_api.IParentIfEventListener;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_android_messenger.SimpleLocalIfParcelable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class ParentIfJniService extends AbstractParentIf {


    private final static String TAG = "ParentIfJniService";
    private static volatile boolean isServiceReady = false;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ParentIfJniService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setLocalIf(ISimpleLocalIf localIf)
    {
        Log.i(TAG, "request setLocalIf called, will call native ");
        nativeSetLocalIf(localIf);
    }

    @Override
    public ISimpleLocalIf getLocalIf()
    {
        Log.i(TAG, "request getLocalIf called, will call native ");
        return nativeGetLocalIf();
    }

  
    @Override
    public void setLocalIfList(ISimpleLocalIf[] localIfList)
    {
        Log.i(TAG, "request setLocalIfList called, will call native ");
        nativeSetLocalIfList(localIfList);
    }

    @Override
    public ISimpleLocalIf[] getLocalIfList()
    {
        Log.i(TAG, "request getLocalIfList called, will call native ");
        return nativeGetLocalIfList();
    }

  
    @Override
    public void setImportedIf(tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf)
    {
        Log.i(TAG, "request setImportedIf called, will call native ");
        nativeSetImportedIf(importedIf);
    }

    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf getImportedIf()
    {
        Log.i(TAG, "request getImportedIf called, will call native ");
        return nativeGetImportedIf();
    }

  
    @Override
    public void setImportedIfList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfList)
    {
        Log.i(TAG, "request setImportedIfList called, will call native ");
        nativeSetImportedIfList(importedIfList);
    }

    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf[] getImportedIfList()
    {
        Log.i(TAG, "request getImportedIfList called, will call native ");
        return nativeGetImportedIfList();
    }

  
    // methods

    @Override
    public ISimpleLocalIf localIfMethod(ISimpleLocalIf param) {
        Log.i(TAG, "request method localIfMethod called, will call native");
        return nativeLocalIfMethod(param);
    }

    @Override
    public  CompletableFuture<ISimpleLocalIf> localIfMethodAsync(ISimpleLocalIf param) {
        return CompletableFuture.supplyAsync(
                () -> {return localIfMethod(param); },
                executor);
    }

    @Override
    public ISimpleLocalIf[] localIfMethodList(ISimpleLocalIf[] param) {
        Log.i(TAG, "request method localIfMethodList called, will call native");
        return nativeLocalIfMethodList(param);
    }

    @Override
    public  CompletableFuture<ISimpleLocalIf[]> localIfMethodListAsync(ISimpleLocalIf[] param) {
        return CompletableFuture.supplyAsync(
                () -> {return localIfMethodList(param); },
                executor);
    }

    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIfMethod(tbIfaceimport.tbIfaceimport_api.IEmptyIf param) {
        Log.i(TAG, "request method importedIfMethod called, will call native");
        return nativeImportedIfMethod(param);
    }

    @Override
    public  CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf> importedIfMethodAsync(tbIfaceimport.tbIfaceimport_api.IEmptyIf param) {
        return CompletableFuture.supplyAsync(
                () -> {return importedIfMethod(param); },
                executor);
    }

    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfMethodList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param) {
        Log.i(TAG, "request method importedIfMethodList called, will call native");
        return nativeImportedIfMethodList(param);
    }

    @Override
    public  CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf[]> importedIfMethodListAsync(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param) {
        return CompletableFuture.supplyAsync(
                () -> {return importedIfMethodList(param); },
                executor);
    }    

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    // Called on Native Impl Service
    private native void nativeSetLocalIf(ISimpleLocalIf localIf);
    private native ISimpleLocalIf nativeGetLocalIf();
  
    private native void nativeSetLocalIfList(ISimpleLocalIf[] localIfList);
    private native ISimpleLocalIf[] nativeGetLocalIfList();
  
    private native void nativeSetImportedIf(tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf);
    private native tbIfaceimport.tbIfaceimport_api.IEmptyIf nativeGetImportedIf();
  
    private native void nativeSetImportedIfList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfList);
    private native tbIfaceimport.tbIfaceimport_api.IEmptyIf[] nativeGetImportedIfList();
  
    // methods
    private native ISimpleLocalIf nativeLocalIfMethod(ISimpleLocalIf param);
    private native ISimpleLocalIf[] nativeLocalIfMethodList(ISimpleLocalIf[] param);
    private native tbIfaceimport.tbIfaceimport_api.IEmptyIf nativeImportedIfMethod(tbIfaceimport.tbIfaceimport_api.IEmptyIf param);
    private native tbIfaceimport.tbIfaceimport_api.IEmptyIf[] nativeImportedIfMethodList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param);

    // Called by Native Impl Service
    public void nativeServiceReady(boolean value) {
        isServiceReady = value;
    }

    //In theory event listener interface
    public void onLocalIfChanged(ISimpleLocalIf newValue)
    {
         Log.i(TAG, "onLocalIfChanged, will pass notification to all listeners");
         fireLocalIfChanged(newValue);
    }
    public void onLocalIfListChanged(ISimpleLocalIf[] newValue)
    {
         Log.i(TAG, "onLocalIfListChanged, will pass notification to all listeners");
         fireLocalIfListChanged(newValue);
    }
    public void onImportedIfChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf newValue)
    {
         Log.i(TAG, "onImportedIfChanged, will pass notification to all listeners");
         fireImportedIfChanged(newValue);
    }
    public void onImportedIfListChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] newValue)
    {
         Log.i(TAG, "onImportedIfListChanged, will pass notification to all listeners");
         fireImportedIfListChanged(newValue);
    }
    public void onLocalIfSignal(ISimpleLocalIf param)
    {
        Log.i(TAG, "onLocalIfSignal, will pass notification to all listeners");
        fireLocalIfSignal(param);
    }
    public void onLocalIfSignalList(ISimpleLocalIf[] param)
    {
        Log.i(TAG, "onLocalIfSignalList, will pass notification to all listeners");
        fireLocalIfSignalList(param);
    }
    public void onImportedIfSignal(tbIfaceimport.tbIfaceimport_api.IEmptyIf param)
    {
        Log.i(TAG, "onImportedIfSignal, will pass notification to all listeners");
        fireImportedIfSignal(param);
    }
    public void onImportedIfSignalList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param)
    {
        Log.i(TAG, "onImportedIfSignalList, will pass notification to all listeners");
        fireImportedIfSignalList(param);
    }

}
