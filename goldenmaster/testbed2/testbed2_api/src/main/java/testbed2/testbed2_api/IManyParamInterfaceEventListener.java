package testbed2.testbed2_api;
import testbed2.testbed2_api.Struct1;
import testbed2.testbed2_api.Struct2;
import testbed2.testbed2_api.Struct3;
import testbed2.testbed2_api.Struct4;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_api.NestedStruct2;
import testbed2.testbed2_api.NestedStruct3;
import testbed2.testbed2_api.Enum1;
import testbed2.testbed2_api.Enum2;
import testbed2.testbed2_api.Enum3;

  public interface IManyParamInterfaceEventListener {
    void onProp1Changed(int newValue);
    void onProp2Changed(int newValue);
    void onProp3Changed(int newValue);
    void onProp4Changed(int newValue);
    void onSig1(int param1);
    void onSig2(int param1, int param2);
    void onSig3(int param1, int param2, int param3);
    void onSig4(int param1, int param2, int param3, int param4);
  void on_readyStatusChanged(boolean isReady);
  }
