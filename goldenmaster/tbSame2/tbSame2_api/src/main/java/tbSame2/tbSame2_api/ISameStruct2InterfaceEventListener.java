package tbSame2.tbSame2_api;
import tbSame2.tbSame2_api.Struct1;
import tbSame2.tbSame2_api.Struct2;
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_api.Enum2;

  public interface ISameStruct2InterfaceEventListener {
    void onProp1Changed(Struct2 newValue);
    void onProp2Changed(Struct2 newValue);
    void onSig1(Struct1 param1);
    void onSig2(Struct1 param1, Struct2 param2);
  void on_readyStatusChanged(boolean isReady);
  }
