package tbRefIfaces.tbRefIfaces_api;

  public interface ISimpleLocalIfEventListener {
    void onIntPropertyChanged(int newValue);
    void onIntSignal(int param);
  void on_readyStatusChanged(boolean isReady);
  }
