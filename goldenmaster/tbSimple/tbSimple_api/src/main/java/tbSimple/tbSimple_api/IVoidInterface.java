package tbSimple.tbSimple_api;

import tbSimple.tbSimple_api.IVoidInterfaceEventListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;


  public interface IVoidInterface {
    // properties
    // methods
    void funcVoid();
    CompletableFuture<Void> funcVoidAsync();
    public void fireSigVoid();
    boolean _isReady();
    void _shutdown();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(IVoidInterfaceEventListener listener);
    void removeEventListener(IVoidInterfaceEventListener listener);
  }
