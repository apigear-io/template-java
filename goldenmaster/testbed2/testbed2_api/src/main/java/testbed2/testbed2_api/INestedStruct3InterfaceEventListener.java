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

import java.util.List;

  public interface INestedStruct3InterfaceEventListener {
    void onProp1Changed(NestedStruct1 newValue);
    void onProp2Changed(NestedStruct2 newValue);
    void onProp3Changed(NestedStruct3 newValue);
    void onSig1(NestedStruct1 param1);
    void onSig2(NestedStruct1 param1, NestedStruct2 param2);
    void onSig3(NestedStruct1 param1, NestedStruct2 param2, NestedStruct3 param3);
  void on_readyStatusChanged(boolean isReady);
  }
