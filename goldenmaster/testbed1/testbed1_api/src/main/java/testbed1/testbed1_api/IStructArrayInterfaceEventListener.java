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

  public interface IStructArrayInterfaceEventListener {
    void onPropBoolChanged(List<StructBool> newValue);
    void onPropIntChanged(List<StructInt> newValue);
    void onPropFloatChanged(List<StructFloat> newValue);
    void onPropStringChanged(List<StructString> newValue);
    void onPropEnumChanged(List<Enum0> newValue);
    void onSigBool(List<StructBool> paramBool);
    void onSigInt(List<StructInt> paramInt);
    void onSigFloat(List<StructFloat> paramFloat);
    void onSigString(List<StructString> paramString);
    void onSigEnum(List<Enum0> paramEnum);
  void on_readyStatusChanged(boolean isReady);
  }
