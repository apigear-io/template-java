package tbSame1.tbSame1_api;

import tbSame1.tbSame1_api.ISameStruct2InterfaceEventListener;
import tbSame1.tbSame1_api.ISameStruct2Interface;
//TODO imported/extern modules
import tbSame1.tbSame1_api.Struct1;
import tbSame1.tbSame1_api.Struct2;
import tbSame1.tbSame1_api.Enum1;
import tbSame1.tbSame1_api.Enum2;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractSameStruct2Interface implements ISameStruct2Interface {
    public Collection<ISameStruct2InterfaceEventListener> listeners = new HashSet<>();

    public void addEventListener(ISameStruct2InterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(ISameStruct2InterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireProp1Changed(Struct2 newValue) {
      for (ISameStruct2InterfaceEventListener listener : listeners) {
        listener.onProp1Changed(newValue);
      }
    }
  
    @Override
    public void fireProp2Changed(Struct2 newValue) {
      for (ISameStruct2InterfaceEventListener listener : listeners) {
        listener.onProp2Changed(newValue);
      }
    }
  
    @Override
    public void fireSig1(Struct1 param1) {
      for (ISameStruct2InterfaceEventListener listener : listeners) {
        listener.onSig1(param1);
      }
    }
  
    @Override
    public void fireSig2(Struct1 param1, Struct2 param2) {
      for (ISameStruct2InterfaceEventListener listener : listeners) {
        listener.onSig2(param1, param2);
      }
    }
  
    
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (ISameStruct2InterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
