package tbSimple.tbSimple_api;

import tbSimple.tbSimple_api.INoSignalsInterfaceEventListener;

import java.util.concurrent.CompletableFuture;


  public interface INoSignalsInterface {
    // properties
    void setPropBool(boolean propBool);
    boolean getPropBool();
    void firePropBoolChanged(boolean newValue);
  
    void setPropInt(int propInt);
    int getPropInt();
    void firePropIntChanged(int newValue);
  
    // methods
    void funcVoid();
    CompletableFuture<Void> funcVoidAsync();
    boolean funcBool(boolean paramBool);
    CompletableFuture<Boolean> funcBoolAsync(boolean paramBool);
    boolean _isReady();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(INoSignalsInterfaceEventListener listener);
    void removeEventListener(INoSignalsInterfaceEventListener listener);
  }
