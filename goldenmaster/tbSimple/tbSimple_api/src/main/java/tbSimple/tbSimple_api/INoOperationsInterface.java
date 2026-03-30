package tbSimple.tbSimple_api;

import tbSimple.tbSimple_api.INoOperationsInterfaceEventListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;


  public interface INoOperationsInterface {
    // properties
    void setPropBool(boolean propBool);
    boolean getPropBool();
    void firePropBoolChanged(boolean newValue);
  
    void setPropInt(int propInt);
    int getPropInt();
    void firePropIntChanged(int newValue);
  
    // methods
    public void fireSigVoid();
    public void fireSigBool(boolean paramBool);
    boolean _isReady();
    void _shutdown();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(INoOperationsInterfaceEventListener listener);
    void removeEventListener(INoOperationsInterfaceEventListener listener);
  }
