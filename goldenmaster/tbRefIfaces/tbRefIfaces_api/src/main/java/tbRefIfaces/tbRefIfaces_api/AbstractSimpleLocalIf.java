package tbRefIfaces.tbRefIfaces_api;

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIfEventListener;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractSimpleLocalIf implements ISimpleLocalIf {
    public Collection<ISimpleLocalIfEventListener> listeners = new HashSet<>();

    public void addEventListener(ISimpleLocalIfEventListener listener) {
      listeners.add(listener); 
    }
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
  
    
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (ISimpleLocalIfEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
