package tbEnum.tbEnum_api;
import tbEnum.tbEnum_api.Enum0;
import tbEnum.tbEnum_api.Enum1;
import tbEnum.tbEnum_api.Enum2;
import tbEnum.tbEnum_api.Enum3;

import java.util.List;

  public interface IEnumInterfaceEventListener {
    void onProp0Changed(Enum0 newValue);
    void onProp1Changed(Enum1 newValue);
    void onProp2Changed(Enum2 newValue);
    void onProp3Changed(Enum3 newValue);
    void onSig0(Enum0 param0);
    void onSig1(Enum1 param1);
    void onSig2(Enum2 param2);
    void onSig3(Enum3 param3);
  void on_readyStatusChanged(boolean isReady);
  }
