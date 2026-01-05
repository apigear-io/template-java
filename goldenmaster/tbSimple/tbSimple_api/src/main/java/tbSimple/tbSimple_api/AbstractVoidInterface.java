package tbSimple.tbSimple_api;

import tbSimple.tbSimple_api.IVoidInterfaceEventListener;
import tbSimple.tbSimple_api.IVoidInterface;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractVoidInterface implements IVoidInterface {
    public Collection<IVoidInterfaceEventListener> listeners = new HashSet<>();

    @Override
    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    @Override
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireSigVoid() {
      for (IVoidInterfaceEventListener listener : listeners) {
        listener.onSigVoid();
      }
    }
  
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (IVoidInterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
