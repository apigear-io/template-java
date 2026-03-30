package tbSimple.tbSimple_api;

import java.util.List;

  public interface INoSignalsInterfaceEventListener {
    void onPropBoolChanged(boolean newValue);
    void onPropIntChanged(int newValue);
  void on_readyStatusChanged(boolean isReady);
  }
