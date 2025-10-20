package testbed2.testbed2_api;

import testbed2.testbed2_api.INestedStruct2InterfaceEventListener;
import testbed2.testbed2_api.INestedStruct2Interface;
import testbed2.testbed2_api.Struct1;
import testbed2.testbed2_api.Struct2;
import testbed2.testbed2_api.Struct3;
import testbed2.testbed2_api.Struct4;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_api.NestedStruct2;
import testbed2.testbed2_api.NestedStruct3;
import testbed2.testbed2_api.Enum1;
import testbed2.testbed2_api.Enum2;
import testbed2.testbed2_api.Enum3;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractNestedStruct2Interface implements INestedStruct2Interface {
    public Collection<INestedStruct2InterfaceEventListener> listeners = new HashSet<>();

    public void addEventListener(INestedStruct2InterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(INestedStruct2InterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireProp1Changed(NestedStruct1 newValue) {
      for (INestedStruct2InterfaceEventListener listener : listeners) {
        listener.onProp1Changed(newValue);
      }
    }
  
    @Override
    public void fireProp2Changed(NestedStruct2 newValue) {
      for (INestedStruct2InterfaceEventListener listener : listeners) {
        listener.onProp2Changed(newValue);
      }
    }
  
    @Override
    public void fireSig1(NestedStruct1 param1) {
      for (INestedStruct2InterfaceEventListener listener : listeners) {
        listener.onSig1(param1);
      }
    }
  
    @Override
    public void fireSig2(NestedStruct1 param1, NestedStruct2 param2) {
      for (INestedStruct2InterfaceEventListener listener : listeners) {
        listener.onSig2(param1, param2);
      }
    }
  
    
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (INestedStruct2InterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
