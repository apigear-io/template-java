package tbSimple.tbSimple_api;

import tbSimple.tbSimple_api.IEmptyInterfaceEventListener;
import tbSimple.tbSimple_api.IEmptyInterface;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractEmptyInterface implements IEmptyInterface {
    public Collection<IEmptyInterfaceEventListener> listeners = new HashSet<>();

    public void addEventListener(IEmptyInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IEmptyInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (IEmptyInterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
