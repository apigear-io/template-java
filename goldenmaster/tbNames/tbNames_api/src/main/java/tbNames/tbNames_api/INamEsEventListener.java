package tbNames.tbNames_api;
import tbNames.tbNames_api.EnumWithUnderScores;

import java.util.List;

  public interface INamEsEventListener {
    void onSwitchChanged(boolean newValue);
    void onSomePropertyChanged(int newValue);
    void onSomePoperty2Changed(int newValue);
    void onEnumPropertyChanged(EnumWithUnderScores newValue);
    void onSomeSignal(boolean SOME_PARAM);
    void onSomeSignal2(boolean Some_Param);
  void on_readyStatusChanged(boolean isReady);
  }
