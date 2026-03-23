package tbNames.tbNames_api;

import tbNames.tbNames_api.INamEsEventListener;
import tbNames.tbNames_api.EnumWithUnderScores;

import java.util.concurrent.CompletableFuture;


  public interface INamEs {
    // properties
    void setSwitch(boolean Switch);
    boolean getSwitch();
    void fireSwitchChanged(boolean newValue);
  
    void setSomeProperty(int SOME_PROPERTY);
    int getSomeProperty();
    void fireSomePropertyChanged(int newValue);
  
    void setSomePoperty2(int Some_Poperty2);
    int getSomePoperty2();
    void fireSomePoperty2Changed(int newValue);
  
    void setEnumProperty(EnumWithUnderScores enum_property);
    EnumWithUnderScores getEnumProperty();
    void fireEnumPropertyChanged(EnumWithUnderScores newValue);
  
    // methods
    void someFunction(boolean SOME_PARAM);
    CompletableFuture<Void> someFunctionAsync(boolean SOME_PARAM);
    void someFunction2(boolean Some_Param);
    CompletableFuture<Void> someFunction2Async(boolean Some_Param);
    public void fireSomeSignal(boolean SOME_PARAM);
    public void fireSomeSignal2(boolean Some_Param);
    boolean _isReady();
    void _shutdown();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(INamEsEventListener listener);
    void removeEventListener(INamEsEventListener listener);
  }
