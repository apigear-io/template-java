package tbSimple.tbSimple_api;

import tbSimple.tbSimple_api.INoPropertiesInterfaceEventListener;

import java.util.concurrent.CompletableFuture;


  public interface INoPropertiesInterface {
    // properties
    // methods
    void funcVoid();
    CompletableFuture<Void> funcVoidAsync();
    boolean funcBool(boolean paramBool);
    CompletableFuture<Boolean> funcBoolAsync(boolean paramBool);
    public void fireSigVoid();
    public void fireSigBool(boolean paramBool);
    boolean _isReady();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(INoPropertiesInterfaceEventListener listener);
    void removeEventListener(INoPropertiesInterfaceEventListener listener);
  }
