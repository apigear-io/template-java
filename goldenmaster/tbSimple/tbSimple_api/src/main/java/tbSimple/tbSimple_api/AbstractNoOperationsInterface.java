package tbSimple.tbSimple_api;

import tbSimple.tbSimple_api.INoOperationsInterfaceEventListener;
import tbSimple.tbSimple_api.INoOperationsInterface;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractNoOperationsInterface implements INoOperationsInterface {
    public Collection<INoOperationsInterfaceEventListener> listeners = new HashSet<>();

    public void addEventListener(INoOperationsInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(INoOperationsInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void firePropBoolChanged(boolean newValue) {
      for (INoOperationsInterfaceEventListener listener : listeners) {
        listener.onPropBoolChanged(newValue);
      }
    }
  
    @Override
    public void firePropIntChanged(int newValue) {
      for (INoOperationsInterfaceEventListener listener : listeners) {
        listener.onPropIntChanged(newValue);
      }
    }
  
    @Override
    public void fireSigVoid() {
      for (INoOperationsInterfaceEventListener listener : listeners) {
        listener.onSigVoid();
      }
    }
  
    @Override
    public void fireSigBool(boolean paramBool) {
      for (INoOperationsInterfaceEventListener listener : listeners) {
        listener.onSigBool(paramBool);
      }
    }
  
    
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (INoOperationsInterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
