package tbSame2.tbSame2_api;
import tbSame2.tbSame2_api.Struct1;
import tbSame2.tbSame2_api.Struct2;
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_api.Enum2;

  public interface ISameStruct1InterfaceEventListener {
    void onProp1Changed(Struct1 newValue);
    void onSig1(Struct1 param1);
  void on_readyStatusChanged(boolean isReady);
  }
