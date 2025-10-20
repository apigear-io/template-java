package tbSimple.tbSimple_api;

import tbSimple.tbSimple_api.ISimpleInterfaceEventListener;
import tbSimple.tbSimple_api.ISimpleInterface;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractSimpleInterface implements ISimpleInterface {
    public Collection<ISimpleInterfaceEventListener> listeners = new HashSet<>();

    public void addEventListener(ISimpleInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(ISimpleInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void firePropBoolChanged(boolean newValue) {
      for (ISimpleInterfaceEventListener listener : listeners) {
        listener.onPropBoolChanged(newValue);
      }
    }
  
    @Override
    public void firePropIntChanged(int newValue) {
      for (ISimpleInterfaceEventListener listener : listeners) {
        listener.onPropIntChanged(newValue);
      }
    }
  
    @Override
    public void firePropInt32Changed(int newValue) {
      for (ISimpleInterfaceEventListener listener : listeners) {
        listener.onPropInt32Changed(newValue);
      }
    }
  
    @Override
    public void firePropInt64Changed(long newValue) {
      for (ISimpleInterfaceEventListener listener : listeners) {
        listener.onPropInt64Changed(newValue);
      }
    }
  
    @Override
    public void firePropFloatChanged(float newValue) {
      for (ISimpleInterfaceEventListener listener : listeners) {
        listener.onPropFloatChanged(newValue);
      }
    }
  
    @Override
    public void firePropFloat32Changed(float newValue) {
      for (ISimpleInterfaceEventListener listener : listeners) {
        listener.onPropFloat32Changed(newValue);
      }
    }
  
    @Override
    public void firePropFloat64Changed(double newValue) {
      for (ISimpleInterfaceEventListener listener : listeners) {
        listener.onPropFloat64Changed(newValue);
      }
    }
  
    @Override
    public void firePropStringChanged(String newValue) {
      for (ISimpleInterfaceEventListener listener : listeners) {
        listener.onPropStringChanged(newValue);
      }
    }
  
    @Override
    public void fireSigBool(boolean paramBool) {
      for (ISimpleInterfaceEventListener listener : listeners) {
        listener.onSigBool(paramBool);
      }
    }
  
    @Override
    public void fireSigInt(int paramInt) {
      for (ISimpleInterfaceEventListener listener : listeners) {
        listener.onSigInt(paramInt);
      }
    }
  
    @Override
    public void fireSigInt32(int paramInt32) {
      for (ISimpleInterfaceEventListener listener : listeners) {
        listener.onSigInt32(paramInt32);
      }
    }
  
    @Override
    public void fireSigInt64(long paramInt64) {
      for (ISimpleInterfaceEventListener listener : listeners) {
        listener.onSigInt64(paramInt64);
      }
    }
  
    @Override
    public void fireSigFloat(float paramFloat) {
      for (ISimpleInterfaceEventListener listener : listeners) {
        listener.onSigFloat(paramFloat);
      }
    }
  
    @Override
    public void fireSigFloat32(float paramFloat32) {
      for (ISimpleInterfaceEventListener listener : listeners) {
        listener.onSigFloat32(paramFloat32);
      }
    }
  
    @Override
    public void fireSigFloat64(double paramFloat64) {
      for (ISimpleInterfaceEventListener listener : listeners) {
        listener.onSigFloat64(paramFloat64);
      }
    }
  
    @Override
    public void fireSigString(String paramString) {
      for (ISimpleInterfaceEventListener listener : listeners) {
        listener.onSigString(paramString);
      }
    }
  
    
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (ISimpleInterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
