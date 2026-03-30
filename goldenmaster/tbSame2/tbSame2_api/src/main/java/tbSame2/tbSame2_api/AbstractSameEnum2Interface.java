package tbSame2.tbSame2_api;

import tbSame2.tbSame2_api.ISameEnum2InterfaceEventListener;
import tbSame2.tbSame2_api.ISameEnum2Interface;
import tbSame2.tbSame2_api.Struct1;
import tbSame2.tbSame2_api.Struct2;
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_api.Enum2;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
  public abstract class AbstractSameEnum2Interface implements ISameEnum2Interface {
    private Collection<ISameEnum2InterfaceEventListener> listeners = ConcurrentHashMap.newKeySet();

    @Override
    public void addEventListener(ISameEnum2InterfaceEventListener listener) {
      listeners.add(listener); 
    }
    @Override
    public void removeEventListener(ISameEnum2InterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireProp1Changed(Enum1 newValue) {
      for (ISameEnum2InterfaceEventListener listener : listeners) {
        listener.onProp1Changed(newValue);
      }
    }
  
    @Override
    public void fireProp2Changed(Enum2 newValue) {
      for (ISameEnum2InterfaceEventListener listener : listeners) {
        listener.onProp2Changed(newValue);
      }
    }
  
    @Override
    public void fireSig1(Enum1 param1) {
      for (ISameEnum2InterfaceEventListener listener : listeners) {
        listener.onSig1(param1);
      }
    }
  
    @Override
    public void fireSig2(Enum1 param1, Enum2 param2) {
      for (ISameEnum2InterfaceEventListener listener : listeners) {
        listener.onSig2(param1, param2);
      }
    }
  
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (ISameEnum2InterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }

    @Override
    public void _shutdown() {}
  }
