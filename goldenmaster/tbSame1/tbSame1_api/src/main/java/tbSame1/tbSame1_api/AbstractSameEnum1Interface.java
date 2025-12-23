package tbSame1.tbSame1_api;

import tbSame1.tbSame1_api.ISameEnum1InterfaceEventListener;
import tbSame1.tbSame1_api.ISameEnum1Interface;
import tbSame1.tbSame1_api.Struct1;
import tbSame1.tbSame1_api.Struct2;
import tbSame1.tbSame1_api.Enum1;
import tbSame1.tbSame1_api.Enum2;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractSameEnum1Interface implements ISameEnum1Interface {
    public Collection<ISameEnum1InterfaceEventListener> listeners = new HashSet<>();

    public void addEventListener(ISameEnum1InterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(ISameEnum1InterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireProp1Changed(Enum1 newValue) {
      for (ISameEnum1InterfaceEventListener listener : listeners) {
        listener.onProp1Changed(newValue);
      }
    }
  
    @Override
    public void fireSig1(Enum1 param1) {
      for (ISameEnum1InterfaceEventListener listener : listeners) {
        listener.onSig1(param1);
      }
    }
  
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (ISameEnum1InterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
