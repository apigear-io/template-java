package tbRefIfaces.tbRefIfaces_api;

import tbRefIfaces.tbRefIfaces_api.IParentIfEventListener;

import java.util.concurrent.CompletableFuture;


  public interface IParentIf {
    // properties
    void setLocalIf(ISimpleLocalIf localIf);
    ISimpleLocalIf getLocalIf();
    void fireLocalIfChanged(ISimpleLocalIf newValue);
  
    void setLocalIfList(ISimpleLocalIf[] localIfList);
    ISimpleLocalIf[] getLocalIfList();
    void fireLocalIfListChanged(ISimpleLocalIf[] newValue);
  
    void setImportedIf(tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIf);
    tbIfaceimport.tbIfaceimport_api.IEmptyIf getImportedIf();
    void fireImportedIfChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf newValue);
  
    void setImportedIfList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfList);
    tbIfaceimport.tbIfaceimport_api.IEmptyIf[] getImportedIfList();
    void fireImportedIfListChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] newValue);
  
    // methods
    ISimpleLocalIf localIfMethod(ISimpleLocalIf param);
    CompletableFuture<ISimpleLocalIf> localIfMethodAsync(ISimpleLocalIf param);
    ISimpleLocalIf[] localIfMethodList(ISimpleLocalIf[] param);
    CompletableFuture<ISimpleLocalIf[]> localIfMethodListAsync(ISimpleLocalIf[] param);
    tbIfaceimport.tbIfaceimport_api.IEmptyIf importedIfMethod(tbIfaceimport.tbIfaceimport_api.IEmptyIf param);
    CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf> importedIfMethodAsync(tbIfaceimport.tbIfaceimport_api.IEmptyIf param);
    tbIfaceimport.tbIfaceimport_api.IEmptyIf[] importedIfMethodList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param);
    CompletableFuture<tbIfaceimport.tbIfaceimport_api.IEmptyIf[]> importedIfMethodListAsync(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param);
    public void fireLocalIfSignal(ISimpleLocalIf param);
    public void fireLocalIfSignalList(ISimpleLocalIf[] param);
    public void fireImportedIfSignal(tbIfaceimport.tbIfaceimport_api.IEmptyIf param);
    public void fireImportedIfSignalList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param);
    boolean _isReady();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(IParentIfEventListener listener);
    void removeEventListener(IParentIfEventListener listener);
  }
