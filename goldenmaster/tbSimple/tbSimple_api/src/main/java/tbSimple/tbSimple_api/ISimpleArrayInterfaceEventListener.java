package tbSimple.tbSimple_api;

import java.util.List;

  public interface ISimpleArrayInterfaceEventListener {
    void onPropBoolChanged(List<Boolean> newValue);
    void onPropIntChanged(List<Integer> newValue);
    void onPropInt32Changed(List<Integer> newValue);
    void onPropInt64Changed(List<Long> newValue);
    void onPropFloatChanged(List<Float> newValue);
    void onPropFloat32Changed(List<Float> newValue);
    void onPropFloat64Changed(List<Double> newValue);
    void onPropStringChanged(List<String> newValue);
    void onPropReadOnlyStringChanged(String newValue);
    void onSigBool(List<Boolean> paramBool);
    void onSigInt(List<Integer> paramInt);
    void onSigInt32(List<Integer> paramInt32);
    void onSigInt64(List<Long> paramInt64);
    void onSigFloat(List<Float> paramFloat);
    void onSigFloat32(List<Float> paramFloa32);
    void onSigFloat64(List<Double> paramFloat64);
    void onSigString(List<String> paramString);
  void on_readyStatusChanged(boolean isReady);
  }
