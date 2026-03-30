package tbRefIfaces.tbRefIfaces_api;

import java.util.List;

  public interface ISimpleLocalIfEventListener {
    void onIntPropertyChanged(int newValue);
    void onIntSignal(int param);
  void on_readyStatusChanged(boolean isReady);
  }
