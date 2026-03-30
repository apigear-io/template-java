package tbRefIfaces.tbRefIfaces_api;

import java.util.List;

  public interface IParentIfEventListener {
    void onLocalIfChanged(ISimpleLocalIf newValue);
    void onLocalIfListChanged(List<ISimpleLocalIf> newValue);
    void onImportedIfChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf newValue);
    void onImportedIfListChanged(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> newValue);
    void onLocalIfSignal(ISimpleLocalIf param);
    void onLocalIfSignalList(List<ISimpleLocalIf> param);
    void onImportedIfSignal(tbIfaceimport.tbIfaceimport_api.IEmptyIf param);
    void onImportedIfSignalList(List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> param);
  void on_readyStatusChanged(boolean isReady);
  }
