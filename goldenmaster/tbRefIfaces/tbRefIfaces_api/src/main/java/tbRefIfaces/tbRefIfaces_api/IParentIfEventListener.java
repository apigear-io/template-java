package tbRefIfaces.tbRefIfaces_api;

  public interface IParentIfEventListener {
    void onLocalIfChanged(ISimpleLocalIf newValue);
    void onLocalIfListChanged(ISimpleLocalIf[] newValue);
    void onImportedIfChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf newValue);
    void onImportedIfListChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] newValue);
    void onLocalIfSignal(ISimpleLocalIf param);
    void onLocalIfSignalList(ISimpleLocalIf[] param);
    void onImportedIfSignal(tbIfaceimport.tbIfaceimport_api.IEmptyIf param);
    void onImportedIfSignalList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param);
  void on_readyStatusChanged(boolean isReady);
  }
