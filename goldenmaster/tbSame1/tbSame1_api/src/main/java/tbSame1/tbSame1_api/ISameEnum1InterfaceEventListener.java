package tbSame1.tbSame1_api;
import tbSame1.tbSame1_api.Struct1;
import tbSame1.tbSame1_api.Struct2;
import tbSame1.tbSame1_api.Enum1;
import tbSame1.tbSame1_api.Enum2;

import java.util.List;

  public interface ISameEnum1InterfaceEventListener {
    void onProp1Changed(Enum1 newValue);
    void onSig1(Enum1 param1);
  void on_readyStatusChanged(boolean isReady);
  }
