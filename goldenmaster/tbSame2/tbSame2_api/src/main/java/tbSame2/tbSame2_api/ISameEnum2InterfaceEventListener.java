package tbSame2.tbSame2_api;
import tbSame2.tbSame2_api.Struct1;
import tbSame2.tbSame2_api.Struct2;
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_api.Enum2;

import java.util.List;

  public interface ISameEnum2InterfaceEventListener {
    void onProp1Changed(Enum1 newValue);
    void onProp2Changed(Enum2 newValue);
    void onSig1(Enum1 param1);
    void onSig2(Enum1 param1, Enum2 param2);
  void on_readyStatusChanged(boolean isReady);
  }
