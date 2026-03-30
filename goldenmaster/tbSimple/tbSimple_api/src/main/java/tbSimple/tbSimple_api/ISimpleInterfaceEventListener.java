package tbSimple.tbSimple_api;

import java.util.List;

  public interface ISimpleInterfaceEventListener {
    void onPropBoolChanged(boolean newValue);
    void onPropIntChanged(int newValue);
    void onPropInt32Changed(int newValue);
    void onPropInt64Changed(long newValue);
    void onPropFloatChanged(float newValue);
    void onPropFloat32Changed(float newValue);
    void onPropFloat64Changed(double newValue);
    void onPropStringChanged(String newValue);
    void onSigBool(boolean paramBool);
    void onSigInt(int paramInt);
    void onSigInt32(int paramInt32);
    void onSigInt64(long paramInt64);
    void onSigFloat(float paramFloat);
    void onSigFloat32(float paramFloat32);
    void onSigFloat64(double paramFloat64);
    void onSigString(String paramString);
  void on_readyStatusChanged(boolean isReady);
  }
