package tbSimple.tbSimple_api;

import tbSimple.tbSimple_api.IEmptyInterfaceEventListener;

import java.util.concurrent.CompletableFuture;


  public interface IEmptyInterface {
    // properties
    // methods
    boolean _isReady();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(IEmptyInterfaceEventListener listener);
    void removeEventListener(IEmptyInterfaceEventListener listener);
  }
