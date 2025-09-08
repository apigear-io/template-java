package tbNames.tbNames_api;

  public interface INamEsEventListener {
    void onSwitchChanged(boolean newValue);
    void onSomePropertyChanged(int newValue);
    void onSomePoperty2Changed(int newValue);
    void onSomeSignal(boolean SOME_PARAM);
    void onSomeSignal2(boolean Some_Param);
  void on_readyStatusChanged(boolean isReady);
  }
