package tbSimple.tbSimple_api;

import tbSimple.tbSimple_api.INoSignalsInterfaceEventListener;
import tbSimple.tbSimple_api.INoSignalsInterface;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractNoSignalsInterface implements INoSignalsInterface {
    public Collection<INoSignalsInterfaceEventListener> listeners = new HashSet<>();

    public void addEventListener(INoSignalsInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(INoSignalsInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void firePropBoolChanged(boolean newValue) {
      for (INoSignalsInterfaceEventListener listener : listeners) {
        listener.onPropBoolChanged(newValue);
      }
    }
  
    @Override
    public void firePropIntChanged(int newValue) {
      for (INoSignalsInterfaceEventListener listener : listeners) {
        listener.onPropIntChanged(newValue);
      }
    }
  
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (INoSignalsInterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
