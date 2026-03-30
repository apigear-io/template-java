package tbSame1.tbSame1_api;

import tbSame1.tbSame1_api.ISameStruct1InterfaceEventListener;
import tbSame1.tbSame1_api.ISameStruct1Interface;
import tbSame1.tbSame1_api.Struct1;
import tbSame1.tbSame1_api.Struct2;
import tbSame1.tbSame1_api.Enum1;
import tbSame1.tbSame1_api.Enum2;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
  public abstract class AbstractSameStruct1Interface implements ISameStruct1Interface {
    private Collection<ISameStruct1InterfaceEventListener> listeners = ConcurrentHashMap.newKeySet();

    @Override
    public void addEventListener(ISameStruct1InterfaceEventListener listener) {
      listeners.add(listener); 
    }
    @Override
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

    @Override
    public void _shutdown() {}
  }
