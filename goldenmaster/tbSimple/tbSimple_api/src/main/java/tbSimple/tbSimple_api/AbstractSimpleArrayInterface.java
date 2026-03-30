package tbSimple.tbSimple_api;

import tbSimple.tbSimple_api.ISimpleArrayInterfaceEventListener;
import tbSimple.tbSimple_api.ISimpleArrayInterface;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
  public abstract class AbstractSimpleArrayInterface implements ISimpleArrayInterface {
    private Collection<ISimpleArrayInterfaceEventListener> listeners = ConcurrentHashMap.newKeySet();

    @Override
    public void addEventListener(ISimpleArrayInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    @Override
    public void removeEventListener(ISimpleArrayInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void firePropBoolChanged(List<Boolean> newValue) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onPropBoolChanged(newValue);
      }
    }
  
    @Override
    public void firePropIntChanged(List<Integer> newValue) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onPropIntChanged(newValue);
      }
    }
  
    @Override
    public void firePropInt32Changed(List<Integer> newValue) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onPropInt32Changed(newValue);
      }
    }
  
    @Override
    public void firePropInt64Changed(List<Long> newValue) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onPropInt64Changed(newValue);
      }
    }
  
    @Override
    public void firePropFloatChanged(List<Float> newValue) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onPropFloatChanged(newValue);
      }
    }
  
    @Override
    public void firePropFloat32Changed(List<Float> newValue) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onPropFloat32Changed(newValue);
      }
    }
  
    @Override
    public void firePropFloat64Changed(List<Double> newValue) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onPropFloat64Changed(newValue);
      }
    }
  
    @Override
    public void firePropStringChanged(List<String> newValue) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onPropStringChanged(newValue);
      }
    }
  
    @Override
    public void firePropReadOnlyStringChanged(String newValue) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onPropReadOnlyStringChanged(newValue);
      }
    }
  
    @Override
    public void fireSigBool(List<Boolean> paramBool) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onSigBool(paramBool);
      }
    }
  
    @Override
    public void fireSigInt(List<Integer> paramInt) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onSigInt(paramInt);
      }
    }
  
    @Override
    public void fireSigInt32(List<Integer> paramInt32) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onSigInt32(paramInt32);
      }
    }
  
    @Override
    public void fireSigInt64(List<Long> paramInt64) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onSigInt64(paramInt64);
      }
    }
  
    @Override
    public void fireSigFloat(List<Float> paramFloat) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onSigFloat(paramFloat);
      }
    }
  
    @Override
    public void fireSigFloat32(List<Float> paramFloa32) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onSigFloat32(paramFloa32);
      }
    }
  
    @Override
    public void fireSigFloat64(List<Double> paramFloat64) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onSigFloat64(paramFloat64);
      }
    }
  
    @Override
    public void fireSigString(List<String> paramString) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onSigString(paramString);
      }
    }
  
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }

    @Override
    public void _shutdown() {}
  }
