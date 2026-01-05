package tbIfaceimport.tbIfaceimport_api;

import tbIfaceimport.tbIfaceimport_api.IEmptyIfEventListener;
import tbIfaceimport.tbIfaceimport_api.IEmptyIf;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractEmptyIf implements IEmptyIf {
    public Collection<IEmptyIfEventListener> listeners = new HashSet<>();

    @Override
    public void addEventListener(IEmptyIfEventListener listener) {
      listeners.add(listener); 
    }
    @Override
    public void removeEventListener(IEmptyIfEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (IEmptyIfEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
