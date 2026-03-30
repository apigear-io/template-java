package testbed1.testbed1_api;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_api.StructStruct;
import testbed1.testbed1_api.StructEnum;
import testbed1.testbed1_api.StructBoolWithArray;
import testbed1.testbed1_api.StructIntWithArray;
import testbed1.testbed1_api.StructFloatWithArray;
import testbed1.testbed1_api.StructStringWithArray;
import testbed1.testbed1_api.StructStructWithArray;
import testbed1.testbed1_api.StructEnumWithArray;
import testbed1.testbed1_api.Enum0;

import java.util.List;

  public interface IStructInterfaceEventListener {
    void onPropBoolChanged(StructBool newValue);
    void onPropIntChanged(StructInt newValue);
    void onPropFloatChanged(StructFloat newValue);
    void onPropStringChanged(StructString newValue);
    void onSigBool(StructBool paramBool);
    void onSigInt(StructInt paramInt);
    void onSigFloat(StructFloat paramFloat);
    void onSigString(StructString paramString);
  void on_readyStatusChanged(boolean isReady);
  }
