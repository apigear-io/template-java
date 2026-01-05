package tbNames.tbNames_api;

import tbNames.tbNames_api.INamEsEventListener;
import tbNames.tbNames_api.INamEs;
import tbNames.tbNames_api.EnumWithUnderScores;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractNamEs implements INamEs {
    public Collection<INamEsEventListener> listeners = new HashSet<>();

    @Override
    public void addEventListener(INamEsEventListener listener) {
      listeners.add(listener); 
    }
    @Override
    public void removeEventListener(INamEsEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireSwitchChanged(boolean newValue) {
      for (INamEsEventListener listener : listeners) {
        listener.onSwitchChanged(newValue);
      }
    }
  
    @Override
    public void fireSomePropertyChanged(int newValue) {
      for (INamEsEventListener listener : listeners) {
        listener.onSomePropertyChanged(newValue);
      }
    }
  
    @Override
    public void fireSomePoperty2Changed(int newValue) {
      for (INamEsEventListener listener : listeners) {
        listener.onSomePoperty2Changed(newValue);
      }
    }
  
    @Override
    public void fireEnumPropertyChanged(EnumWithUnderScores newValue) {
      for (INamEsEventListener listener : listeners) {
        listener.onEnumPropertyChanged(newValue);
      }
    }
  
    @Override
    public void fireSomeSignal(boolean SOME_PARAM) {
      for (INamEsEventListener listener : listeners) {
        listener.onSomeSignal(SOME_PARAM);
      }
    }
  
    @Override
    public void fireSomeSignal2(boolean Some_Param) {
      for (INamEsEventListener listener : listeners) {
        listener.onSomeSignal2(Some_Param);
      }
    }
  
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (INamEsEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
