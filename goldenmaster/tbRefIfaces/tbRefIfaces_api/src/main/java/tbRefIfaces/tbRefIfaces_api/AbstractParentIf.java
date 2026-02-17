package tbRefIfaces.tbRefIfaces_api;

import tbRefIfaces.tbRefIfaces_api.IParentIfEventListener;
import tbRefIfaces.tbRefIfaces_api.IParentIf;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractParentIf implements IParentIf {
    private Collection<IParentIfEventListener> listeners = new HashSet<>();

    @Override
    public void addEventListener(IParentIfEventListener listener) {
      listeners.add(listener); 
    }
    @Override
    public void removeEventListener(IParentIfEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireLocalIfChanged(ISimpleLocalIf newValue) {
      for (IParentIfEventListener listener : listeners) {
        listener.onLocalIfChanged(newValue);
      }
    }
  
    @Override
    public void fireLocalIfListChanged(ISimpleLocalIf[] newValue) {
      for (IParentIfEventListener listener : listeners) {
        listener.onLocalIfListChanged(newValue);
      }
    }
  
    @Override
    public void fireImportedIfChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf newValue) {
      for (IParentIfEventListener listener : listeners) {
        listener.onImportedIfChanged(newValue);
      }
    }
  
    @Override
    public void fireImportedIfListChanged(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] newValue) {
      for (IParentIfEventListener listener : listeners) {
        listener.onImportedIfListChanged(newValue);
      }
    }
  
    @Override
    public void fireLocalIfSignal(ISimpleLocalIf param) {
      for (IParentIfEventListener listener : listeners) {
        listener.onLocalIfSignal(param);
      }
    }
  
    @Override
    public void fireLocalIfSignalList(ISimpleLocalIf[] param) {
      for (IParentIfEventListener listener : listeners) {
        listener.onLocalIfSignalList(param);
      }
    }
  
    @Override
    public void fireImportedIfSignal(tbIfaceimport.tbIfaceimport_api.IEmptyIf param) {
      for (IParentIfEventListener listener : listeners) {
        listener.onImportedIfSignal(param);
      }
    }
  
    @Override
    public void fireImportedIfSignalList(tbIfaceimport.tbIfaceimport_api.IEmptyIf[] param) {
      for (IParentIfEventListener listener : listeners) {
        listener.onImportedIfSignalList(param);
      }
    }
  
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (IParentIfEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
