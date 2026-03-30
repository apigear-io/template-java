package tbRefIfaces.tbRefIfaces_api;

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIfEventListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;


  public interface ISimpleLocalIf {
    // properties
    void setIntProperty(int intProperty);
    int getIntProperty();
    void fireIntPropertyChanged(int newValue);
  
    // methods
    int intMethod(int param);
    CompletableFuture<Integer> intMethodAsync(int param);
    public void fireIntSignal(int param);
    boolean _isReady();
    void _shutdown();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(ISimpleLocalIfEventListener listener);
    void removeEventListener(ISimpleLocalIfEventListener listener);
  }
