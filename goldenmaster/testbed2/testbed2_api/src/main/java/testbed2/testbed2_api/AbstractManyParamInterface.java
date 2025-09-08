package testbed2.testbed2_api;

import testbed2.testbed2_api.IManyParamInterfaceEventListener;
import testbed2.testbed2_api.IManyParamInterface;
//TODO imported/extern modules
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
  public abstract class AbstractManyParamInterface implements IManyParamInterface {
    public Collection<IManyParamInterfaceEventListener> listeners = new HashSet<>();

    public void addEventListener(IManyParamInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IManyParamInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireProp1Changed(int newValue) {
      for (IManyParamInterfaceEventListener listener : listeners) {
        listener.onProp1Changed(newValue);
      }
    }
  
    @Override
    public void fireProp2Changed(int newValue) {
      for (IManyParamInterfaceEventListener listener : listeners) {
        listener.onProp2Changed(newValue);
      }
    }
  
    @Override
    public void fireProp3Changed(int newValue) {
      for (IManyParamInterfaceEventListener listener : listeners) {
        listener.onProp3Changed(newValue);
      }
    }
  
    @Override
    public void fireProp4Changed(int newValue) {
      for (IManyParamInterfaceEventListener listener : listeners) {
        listener.onProp4Changed(newValue);
      }
    }
  
    @Override
    public void fireSig1(int param1) {
      for (IManyParamInterfaceEventListener listener : listeners) {
        listener.onSig1(param1);
      }
    }
  
    @Override
    public void fireSig2(int param1, int param2) {
      for (IManyParamInterfaceEventListener listener : listeners) {
        listener.onSig2(param1, param2);
      }
    }
  
    @Override
    public void fireSig3(int param1, int param2, int param3) {
      for (IManyParamInterfaceEventListener listener : listeners) {
        listener.onSig3(param1, param2, param3);
      }
    }
  
    @Override
    public void fireSig4(int param1, int param2, int param3, int param4) {
      for (IManyParamInterfaceEventListener listener : listeners) {
        listener.onSig4(param1, param2, param3, param4);
      }
    }
  
    
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (IManyParamInterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
