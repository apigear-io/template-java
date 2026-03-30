package tbSame1.tbSame1_api;
import tbSame1.tbSame1_api.Struct1;
import tbSame1.tbSame1_api.Struct2;
import tbSame1.tbSame1_api.Enum1;
import tbSame1.tbSame1_api.Enum2;

import java.util.List;

  public interface ISameStruct2InterfaceEventListener {
    void onProp1Changed(Struct2 newValue);
    void onProp2Changed(Struct2 newValue);
    void onSig1(Struct1 param1);
    void onSig2(Struct1 param1, Struct2 param2);
  void on_readyStatusChanged(boolean isReady);
  }
