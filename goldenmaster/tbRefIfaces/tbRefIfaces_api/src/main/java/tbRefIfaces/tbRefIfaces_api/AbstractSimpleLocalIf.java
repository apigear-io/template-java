package tbRefIfaces.tbRefIfaces_api;

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIfEventListener;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
  public abstract class AbstractSimpleLocalIf implements ISimpleLocalIf {
    private Collection<ISimpleLocalIfEventListener> listeners = ConcurrentHashMap.newKeySet();

    @Override
    public void addEventListener(ISimpleLocalIfEventListener listener) {
      listeners.add(listener); 
    }
    @Override
    public void removeEventListener(ISimpleLocalIfEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireIntPropertyChanged(int newValue) {
      for (ISimpleLocalIfEventListener listener : listeners) {
        listener.onIntPropertyChanged(newValue);
      }
    }
  
    @Override
    public void fireIntSignal(int param) {
      for (ISimpleLocalIfEventListener listener : listeners) {
        listener.onIntSignal(param);
      }
    }
  
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (ISimpleLocalIfEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }

    @Override
    public void _shutdown() {}
  }
