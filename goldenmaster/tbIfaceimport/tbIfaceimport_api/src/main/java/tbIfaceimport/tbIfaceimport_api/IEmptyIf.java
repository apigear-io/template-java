package tbIfaceimport.tbIfaceimport_api;

import tbIfaceimport.tbIfaceimport_api.IEmptyIfEventListener;

import java.util.concurrent.CompletableFuture;


  public interface IEmptyIf {
    // properties
    // methods
    boolean _isReady();
    void _shutdown();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(IEmptyIfEventListener listener);
    void removeEventListener(IEmptyIfEventListener listener);
  }
