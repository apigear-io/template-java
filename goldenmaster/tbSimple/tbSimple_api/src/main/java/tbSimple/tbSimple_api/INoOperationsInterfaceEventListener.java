package tbSimple.tbSimple_api;

import java.util.List;

  public interface INoOperationsInterfaceEventListener {
    void onPropBoolChanged(boolean newValue);
    void onPropIntChanged(int newValue);
    void onSigVoid();
    void onSigBool(boolean paramBool);
  void on_readyStatusChanged(boolean isReady);
  }
