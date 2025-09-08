package tbSame1.tbSame1_api;
import tbSame1.tbSame1_api.Struct1;
import tbSame1.tbSame1_api.Struct2;
import tbSame1.tbSame1_api.Enum1;
import tbSame1.tbSame1_api.Enum2;

  public interface ISameStruct1InterfaceEventListener {
    void onProp1Changed(Struct1 newValue);
    void onSig1(Struct1 param1);
  void on_readyStatusChanged(boolean isReady);
  }
