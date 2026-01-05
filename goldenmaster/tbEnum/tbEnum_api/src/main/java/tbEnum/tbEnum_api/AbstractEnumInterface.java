package tbEnum.tbEnum_api;

import tbEnum.tbEnum_api.IEnumInterfaceEventListener;
import tbEnum.tbEnum_api.IEnumInterface;
import tbEnum.tbEnum_api.Enum0;
import tbEnum.tbEnum_api.Enum1;
import tbEnum.tbEnum_api.Enum2;
import tbEnum.tbEnum_api.Enum3;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractEnumInterface implements IEnumInterface {
    public Collection<IEnumInterfaceEventListener> listeners = new HashSet<>();

    @Override
    public void addEventListener(IEnumInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    @Override
    public void removeEventListener(IEnumInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireProp0Changed(Enum0 newValue) {
      for (IEnumInterfaceEventListener listener : listeners) {
        listener.onProp0Changed(newValue);
      }
    }
  
    @Override
    public void fireProp1Changed(Enum1 newValue) {
      for (IEnumInterfaceEventListener listener : listeners) {
        listener.onProp1Changed(newValue);
      }
    }
  
    @Override
    public void fireProp2Changed(Enum2 newValue) {
      for (IEnumInterfaceEventListener listener : listeners) {
        listener.onProp2Changed(newValue);
      }
    }
  
    @Override
    public void fireProp3Changed(Enum3 newValue) {
      for (IEnumInterfaceEventListener listener : listeners) {
        listener.onProp3Changed(newValue);
      }
    }
  
    @Override
    public void fireSig0(Enum0 param0) {
      for (IEnumInterfaceEventListener listener : listeners) {
        listener.onSig0(param0);
      }
    }
  
    @Override
    public void fireSig1(Enum1 param1) {
      for (IEnumInterfaceEventListener listener : listeners) {
        listener.onSig1(param1);
      }
    }
  
    @Override
    public void fireSig2(Enum2 param2) {
      for (IEnumInterfaceEventListener listener : listeners) {
        listener.onSig2(param2);
      }
    }
  
    @Override
    public void fireSig3(Enum3 param3) {
      for (IEnumInterfaceEventListener listener : listeners) {
        listener.onSig3(param3);
      }
    }
  
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (IEnumInterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
