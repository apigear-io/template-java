package tbSimple.tbSimple_api;

import tbSimple.tbSimple_api.IEmptyInterfaceEventListener;
import tbSimple.tbSimple_api.IEmptyInterface;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
  public abstract class AbstractEmptyInterface implements IEmptyInterface {
    private Collection<IEmptyInterfaceEventListener> listeners = ConcurrentHashMap.newKeySet();

    @Override
    public void addEventListener(IEmptyInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    @Override
    public void removeEventListener(IEmptyInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (IEmptyInterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
