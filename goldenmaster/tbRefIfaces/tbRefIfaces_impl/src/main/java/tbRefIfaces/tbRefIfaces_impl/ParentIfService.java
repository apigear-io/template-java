package tbRefIfaces.tbRefIfaces_impl;

import android.os.Messenger;
import android.util.Log;

import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_api.AbstractParentIf;
import tbRefIfaces.tbRefIfaces_api.IParentIfEventListener;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_impl.SimpleLocalIfService;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;


public class ParentIfService extends AbstractParentIf {

    private final static String TAG = "ParentIfService";
    private volatile boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private ISimpleLocalIf m_localIf = null;
    private List<ISimpleLocalIf> m_localIfList = new ArrayList<>();
    private tbIfaceimport.tbIfaceimport_api.IEmptyIf m_importedIf = null;
    private List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> m_importedIfList = new ArrayList<>();

    public ParentIfService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setLocalIf(ISimpleLocalIf localIf)
    {
        Log.i(TAG, "request setLocalIf called ");
        if (m_localIf != localIf)
        {
            m_localIf = localIf;
            onLocalIfChanged(m_localIf);
        }

    }

    @Override
    public ISimpleLocalIf getLocalIf()
    {
        Log.i(TAG, "request getLocalIf called,");
        return m_localIf;
    }

  
    @Override
    public void setLocalIfList(List<ISimpleLocalIf> localIfList)
    {
        Log.i(TAG, "request setLocalIfList called ");
        if (!m_localIfList.equals(localIfList))
        {
            m_localIfList = new ArrayList<>(localIfList);
            onLocalIfListChanged(new ArrayList<>(m_localIfList));
        }

    }

    @Override
    public List<ISimpleLocalIf> getLocalIfList()
    {
        Log.i(TAG, "request getLocalIfList called,");
        return new ArrayList<>(m_localIfList);
    }

  
    @Override
    public void setImportedIf(tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf)
    {
        Log.i(TAG, "request setImportedIf called ");
        if (m_importedIf != importedIf)
        {
            m_importedIf = importedIf;
            onImportedIfChanged(m_importedIf);
        }

    }

    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf getImportedIf()
    {
        Log.i(TAG, "request getImportedIf called,");
        return m_importedIf;
    }

  
    @Override
    public void setImportedIfList(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> importedIfList)
    {
        Log.i(TAG, "request setImportedIfList called ");
        if (!m_importedIfList.equals(importedIfList))
        {
            m_importedIfList = new ArrayList<>(importedIfList);
            onImportedIfListChanged(new ArrayList<>(m_importedIfList));
        }

    }

    @Override
    public List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> getImportedIfList()
    {
        Log.i(TAG, "request getImportedIfList called,");
        return new ArrayList<>(m_importedIfList);
    }

  
    // methods

    @Override
    public ISimpleLocalIf localIfMethod(ISimpleLocalIf param) {
        Log.i(TAG, "request method localIfMethod called, returnig default");
        return null;
    }

    @Override
    public  CompletableFuture<ISimpleLocalIf> localIfMethodAsync(ISimpleLocalIf param) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return localIfMethod(param); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<ISimpleLocalIf> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<ISimpleLocalIf> localIfMethodList(List<ISimpleLocalIf> param) {
        Log.i(TAG, "request method localIfMethodList called, returnig default");
        return new ArrayList<>();
    }

    @Override
    public  CompletableFuture<List<ISimpleLocalIf>> localIfMethodListAsync(List<ISimpleLocalIf> param) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return localIfMethodList(param); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<ISimpleLocalIf>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIfMethod(tbIfaceimport.tbIfaceimport_api.IEmptyIf param) {
        Log.i(TAG, "request method importedIfMethod called, returnig default");
        return null;
    }

    @Override
    public  CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf> importedIfMethodAsync(tbIfaceimport.tbIfaceimport_api.IEmptyIf param) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return importedIfMethod(param); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> importedIfMethodList(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> param) {
        Log.i(TAG, "request method importedIfMethodList called, returnig default");
        return new ArrayList<>();
    }

    @Override
    public  CompletableFuture<List<tbIfaceimport.tbIfaceimport_api.IEmptyIf>> importedIfMethodListAsync(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> param) {
        try {
            return CompletableFuture.supplyAsync(
                    () -> {return importedIfMethodList(param); },
                    executor);
        } catch (RejectedExecutionException e) {
            CompletableFuture<List<tbIfaceimport.tbIfaceimport_api.IEmptyIf>> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override
    public boolean _isReady() {
        return isServiceReady;
    }

    @Override
    public void _shutdown() {
        isServiceReady = false;
        fire_readyStatusChanged(false);
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    //In theory event listener interface
    private void onLocalIfChanged(ISimpleLocalIf newValue)
    {
         Log.i(TAG, "onLocalIfChanged, will pass notification to all listeners");
         fireLocalIfChanged(newValue);
    }
    private void onLocalIfListChanged(List<ISimpleLocalIf> newValue)
    {
         Log.i(TAG, "onLocalIfListChanged, will pass notification to all listeners");
         fireLocalIfListChanged(newValue);
    }
    private void onImportedIfChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf newValue)
    {
         Log.i(TAG, "onImportedIfChanged, will pass notification to all listeners");
         fireImportedIfChanged(newValue);
    }
    private void onImportedIfListChanged(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> newValue)
    {
         Log.i(TAG, "onImportedIfListChanged, will pass notification to all listeners");
         fireImportedIfListChanged(newValue);
    }
    public void onLocalIfSignal(ISimpleLocalIf param)
    {
        Log.i(TAG, "onLocalIfSignal, will pass notification to all listeners");
        fireLocalIfSignal(param);
    }
    public void onLocalIfSignalList(List<ISimpleLocalIf> param)
    {
        Log.i(TAG, "onLocalIfSignalList, will pass notification to all listeners");
        fireLocalIfSignalList(param);
    }
    public void onImportedIfSignal(tbIfaceimport.tbIfaceimport_api.IEmptyIf param)
    {
        Log.i(TAG, "onImportedIfSignal, will pass notification to all listeners");
        fireImportedIfSignal(param);
    }
    public void onImportedIfSignalList(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> param)
    {
        Log.i(TAG, "onImportedIfSignalList, will pass notification to all listeners");
        fireImportedIfSignalList(param);
    }

}