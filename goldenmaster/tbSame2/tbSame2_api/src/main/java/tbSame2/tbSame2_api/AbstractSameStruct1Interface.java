package tbSame2.tbSame2_api;

import tbSame2.tbSame2_api.ISameStruct1InterfaceEventListener;
import tbSame2.tbSame2_api.ISameStruct1Interface;
import tbSame2.tbSame2_api.Struct1;
import tbSame2.tbSame2_api.Struct2;
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_api.Enum2;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractSameStruct1Interface implements ISameStruct1Interface {
    public Collection<ISameStruct1InterfaceEventListener> listeners = new HashSet<>();

    public void addEventListener(ISameStruct1InterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(ISameStruct1InterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireProp1Changed(Struct1 newValue) {
      for (ISameStruct1InterfaceEventListener listener : listeners) {
        listener.onProp1Changed(newValue);
      }
    }
  
    @Override
    public void fireSig1(Struct1 param1) {
      for (ISameStruct1InterfaceEventListener listener : listeners) {
        listener.onSig1(param1);
      }
    }
  
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (ISameStruct1InterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
