package testbed1.testbed1_api;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_api.StructString;

  public interface IStructArrayInterfaceEventListener {
    void onPropBoolChanged(StructBool[] newValue);
    void onPropIntChanged(StructInt[] newValue);
    void onPropFloatChanged(StructFloat[] newValue);
    void onPropStringChanged(StructString[] newValue);
    void onSigBool(StructBool[] paramBool);
    void onSigInt(StructInt[] paramInt);
    void onSigFloat(StructFloat[] paramFloat);
    void onSigString(StructString[] paramString);
  void on_readyStatusChanged(boolean isReady);
  }
