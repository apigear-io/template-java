package testbed2.testbed2_api;

import testbed2.testbed2_api.INestedStruct1InterfaceEventListener;
import testbed2.testbed2_api.INestedStruct1Interface;
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
  public abstract class AbstractNestedStruct1Interface implements INestedStruct1Interface {
    private Collection<INestedStruct1InterfaceEventListener> listeners = new HashSet<>();

    @Override
    public void addEventListener(INestedStruct1InterfaceEventListener listener) {
      listeners.add(listener); 
    }
    @Override
    public void removeEventListener(INestedStruct1InterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireProp1Changed(NestedStruct1 newValue) {
      for (INestedStruct1InterfaceEventListener listener : listeners) {
        listener.onProp1Changed(newValue);
      }
    }
  
    @Override
    public void fireSig1(NestedStruct1 param1) {
      for (INestedStruct1InterfaceEventListener listener : listeners) {
        listener.onSig1(param1);
      }
    }
  
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (INestedStruct1InterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
