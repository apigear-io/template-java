package tbRefIfaces.tbRefIfaces_api;

import tbRefIfaces.tbRefIfaces_api.IParentIfEventListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;


  public interface IParentIf {
    // properties
    void setLocalIf(ISimpleLocalIf localIf);
    ISimpleLocalIf getLocalIf();
    void fireLocalIfChanged(ISimpleLocalIf newValue);
  
    void setLocalIfList(List<ISimpleLocalIf> localIfList);
    List<ISimpleLocalIf> getLocalIfList();
    void fireLocalIfListChanged(List<ISimpleLocalIf> newValue);
  
    void setImportedIf(tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf);
    tbIfaceimport.tbIfaceimport_api.IEmptyIf getImportedIf();
    void fireImportedIfChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf newValue);
  
    void setImportedIfList(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> importedIfList);
    List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> getImportedIfList();
    void fireImportedIfListChanged(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> newValue);
  
    // methods
    ISimpleLocalIf localIfMethod(ISimpleLocalIf param);
    CompletableFuture<ISimpleLocalIf> localIfMethodAsync(ISimpleLocalIf param);
    List<ISimpleLocalIf> localIfMethodList(List<ISimpleLocalIf> param);
    CompletableFuture<List<ISimpleLocalIf>> localIfMethodListAsync(List<ISimpleLocalIf> param);
    tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIfMethod(tbIfaceimport.tbIfaceimport_api.IEmptyIf param);
    CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf> importedIfMethodAsync(tbIfaceimport.tbIfaceimport_api.IEmptyIf param);
    List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> importedIfMethodList(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> param);
    CompletableFuture<List<tbIfaceimport.tbIfaceimport_api.IEmptyIf>> importedIfMethodListAsync(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> param);
    public void fireLocalIfSignal(ISimpleLocalIf param);
    public void fireLocalIfSignalList(List<ISimpleLocalIf> param);
    public void fireImportedIfSignal(tbIfaceimport.tbIfaceimport_api.IEmptyIf param);
    public void fireImportedIfSignalList(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> param);
    boolean _isReady();
    void _shutdown();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(IParentIfEventListener listener);
    void removeEventListener(IParentIfEventListener listener);
  }
