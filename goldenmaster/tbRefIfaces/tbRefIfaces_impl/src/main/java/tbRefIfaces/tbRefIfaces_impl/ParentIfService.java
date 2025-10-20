package tbRefIfaces.tbRefIfaces_impl;

import android.os.Messenger;
import android.util.Log;

import tbRefIfaces.tbRefIfaces_api.IParentIf;
import tbRefIfaces.tbRefIfaces_api.AbstractParentIf;
import tbRefIfaces.tbRefIfaces_api.IParentIfEventListener;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import tbRefIfaces.tbRefIfaces_impl.SimpleLocalIfService;


import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.Arrays;


public class ParentIfService extends AbstractParentIf {

    private final static String TAG = "ParentIfService";
    private static boolean isServiceReady = true;//Use if you're waiting for some setup to be done
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);
    private ISimpleLocalIf m_localIf = null;
    private ISimpleLocalIf[] m_localIfList = new ISimpleLocalIf[]{};
    private tbIfaceimport.tbIfaceimport_api.IEmptyIf m_importedIf = null;
    private tbIfaceimport.tbIfaceimport_api.IEmptyIf[] m_importedIfList = new tbIfaceimport.tbIfaceimport_api.IEmptyIf[]{};

    public ParentIfService()
    {
        fire_readyStatusChanged(true);
    }
    @Override
    public void setLocalIf(ISimpleLocalIf localIf)
    {
        Log.i(TAG, "request setLocalIf called ");
        if (! m_localIf.equals(localIf))
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
    public void setLocalIfList(ISimpleLocalIf[] localIfList)
    {
        Log.i(TAG, "request setLocalIfList called ");
        if (! Arrays.equals(m_localIfList, localIfList))
        {
            m_localIfList = localIfList;
            onLocalIfListChanged(m_localIfList);
        }

    }

    @Override
    public ISimpleLocalIf[] getLocalIfList()
    {
        Log.i(TAG, "request getLocalIfList called,");
        return m_localIfList;
    }

  
    @Override
    public void setImportedIf(tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf)
    {
        Log.i(TAG, "request setImportedIf called ");
        if (! m_importedIf.equals(importedIf))
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
    public void setImportedIfList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfList)
    {
        Log.i(TAG, "request setImportedIfList called ");
        if (! Arrays.equals(m_importedIfList, importedIfList))
        {
            m_importedIfList = importedIfList;
            onImportedIfListChanged(m_importedIfList);
        }

    }

    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf[] getImportedIfList()
    {
        Log.i(TAG, "request getImportedIfList called,");
        return m_importedIfList;
    }

  
    // methods

    @Override
    public ISimpleLocalIf localIfMethod(ISimpleLocalIf param) {
        Log.w(TAG, "request method localIfMethod called, returnig default");
        return null;
    }

    @Override
    public  CompletableFuture<ISimpleLocalIf> localIfMethodAsync(ISimpleLocalIf param) {
        return CompletableFuture.supplyAsync(
                () -> {return localIfMethod(param); },
                executor);
    }

    @Override
    public ISimpleLocalIf[] localIfMethodList(ISimpleLocalIf[] param) {
        Log.w(TAG, "request method localIfMethodList called, returnig default");
        return new ISimpleLocalIf[]{};
    }

    @Override
    public  CompletableFuture<ISimpleLocalIf[]> localIfMethodListAsync(ISimpleLocalIf[] param) {
        return CompletableFuture.supplyAsync(
                () -> {return localIfMethodList(param); },
                executor);
    }

    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIfMethod(tbIfaceimport.tbIfaceimport_api.IEmptyIf param) {
        Log.w(TAG, "request method importedIfMethod called, returnig default");
        return null;
    }

    @Override
    public  CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf> importedIfMethodAsync(tbIfaceimport.tbIfaceimport_api.IEmptyIf param) {
        return CompletableFuture.supplyAsync(
                () -> {return importedIfMethod(param); },
                executor);
    }

    @Override
    public tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfMethodList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param) {
        Log.w(TAG, "request method importedIfMethodList called, returnig default");
        return new tbIfaceimport.tbIfaceimport_api.IEmptyIf[]{};
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

    //In theory event listener interface
    private void onLocalIfChanged(ISimpleLocalIf newValue)
    {
         Log.i(TAG, "onLocalIfChanged, will pass notification to all listeners");
         fireLocalIfChanged(newValue);
    }
    private void onLocalIfListChanged(ISimpleLocalIf[] newValue)
    {
         Log.i(TAG, "onLocalIfListChanged, will pass notification to all listeners");
         fireLocalIfListChanged(newValue);
    }
    private void onImportedIfChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf newValue)
    {
         Log.i(TAG, "onImportedIfChanged, will pass notification to all listeners");
         fireImportedIfChanged(newValue);
    }
    private void onImportedIfListChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] newValue)
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