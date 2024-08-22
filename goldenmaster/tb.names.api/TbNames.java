package tb.names.api;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TbNames {

  // enumerations

  // data structures

  // interfaces
  public static interface INamEsEventListener {
    void onSwitchChanged(boolean oldValue, boolean newValue);
    void onSomePropertyChanged(int oldValue, int newValue);
    void onSomePoperty2Changed(int oldValue, int newValue);
    void onSomeSignal(boolean SOME_PARAM);
    void onSomeSignal2(boolean Some_Param);
  }

  public static interface INamEs {
    // properties
    void setSwitch(boolean Switch);
    boolean getSwitch();
    void fireSwitchChanged(boolean oldValue, boolean newValue);
  
    void setSomeProperty(int SOME_PROPERTY);
    int getSomeProperty();
    void fireSomePropertyChanged(int oldValue, int newValue);
  
    void setSomePoperty2(int Some_Poperty2);
    int getSomePoperty2();
    void fireSomePoperty2Changed(int oldValue, int newValue);
  
    // methods
    void someFunction(boolean SOME_PARAM);
    void someFunction2(boolean Some_Param);

    // signal listeners
    void addEventListener(INamEsEventListener listener);
    void removeEventListener(INamEsEventListener listener);
  }

  public static class AbstractNamEs implements INamEs {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireSwitchChanged(boolean oldValue, boolean newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onSwitchChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void fireSomePropertyChanged(int oldValue, int newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onSomePropertyChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void fireSomePoperty2Changed(int oldValue, int newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onSomePoperty2Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireSomeSignal(boolean SOME_PARAM) {
      for (ISomeSignalEventListener listener : events) {
        listener.onSomeSignal(SOME_PARAM);
      }
    }
  
    @Override
    public void fireSomeSignal2(boolean Some_Param) {
      for (ISomeSignal2EventListener listener : events) {
        listener.onSomeSignal2(Some_Param);
      }
    }
  
    
  }
}