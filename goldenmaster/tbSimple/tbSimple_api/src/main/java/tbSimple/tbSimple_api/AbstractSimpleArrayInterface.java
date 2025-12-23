package tbSimple.tbSimple_api;

import tbSimple.tbSimple_api.ISimpleArrayInterfaceEventListener;
import tbSimple.tbSimple_api.ISimpleArrayInterface;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractSimpleArrayInterface implements ISimpleArrayInterface {
    public Collection<ISimpleArrayInterfaceEventListener> listeners = new HashSet<>();

    public void addEventListener(ISimpleArrayInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(ISimpleArrayInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void firePropBoolChanged(boolean[] newValue) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onPropBoolChanged(newValue);
      }
    }
  
    @Override
    public void firePropIntChanged(int[] newValue) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onPropIntChanged(newValue);
      }
    }
  
    @Override
    public void firePropInt32Changed(int[] newValue) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onPropInt32Changed(newValue);
      }
    }
  
    @Override
    public void firePropInt64Changed(long[] newValue) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onPropInt64Changed(newValue);
      }
    }
  
    @Override
    public void firePropFloatChanged(float[] newValue) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onPropFloatChanged(newValue);
      }
    }
  
    @Override
    public void firePropFloat32Changed(float[] newValue) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onPropFloat32Changed(newValue);
      }
    }
  
    @Override
    public void firePropFloat64Changed(double[] newValue) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onPropFloat64Changed(newValue);
      }
    }
  
    @Override
    public void firePropStringChanged(String[] newValue) {
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
    public void fireSigBool(boolean[] paramBool) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onSigBool(paramBool);
      }
    }
  
    @Override
    public void fireSigInt(int[] paramInt) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onSigInt(paramInt);
      }
    }
  
    @Override
    public void fireSigInt32(int[] paramInt32) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onSigInt32(paramInt32);
      }
    }
  
    @Override
    public void fireSigInt64(long[] paramInt64) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onSigInt64(paramInt64);
      }
    }
  
    @Override
    public void fireSigFloat(float[] paramFloat) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onSigFloat(paramFloat);
      }
    }
  
    @Override
    public void fireSigFloat32(float[] paramFloa32) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onSigFloat32(paramFloa32);
      }
    }
  
    @Override
    public void fireSigFloat64(double[] paramFloat64) {
      for (ISimpleArrayInterfaceEventListener listener : listeners) {
        listener.onSigFloat64(paramFloat64);
      }
    }
  
    @Override
    public void fireSigString(String[] paramString) {
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
  }
