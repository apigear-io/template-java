package tbSimple.tbSimple_api;

import tbSimple.tbSimple_api.INoPropertiesInterfaceEventListener;
import tbSimple.tbSimple_api.INoPropertiesInterface;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractNoPropertiesInterface implements INoPropertiesInterface {
    public Collection<INoPropertiesInterfaceEventListener> listeners = new HashSet<>();

    @Override
    public void addEventListener(INoPropertiesInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    @Override
    public void removeEventListener(INoPropertiesInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireSigVoid() {
      for (INoPropertiesInterfaceEventListener listener : listeners) {
        listener.onSigVoid();
      }
    }
  
    @Override
    public void fireSigBool(boolean paramBool) {
      for (INoPropertiesInterfaceEventListener listener : listeners) {
        listener.onSigBool(paramBool);
      }
    }
  
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (INoPropertiesInterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
