package testbed1.testbed1_api;

import testbed1.testbed1_api.IStructArrayInterfaceEventListener;
import testbed1.testbed1_api.IStructArrayInterface;
//TODO imported/extern modules
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_api.StructString;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractStructArrayInterface implements IStructArrayInterface {
    public Collection<IStructArrayInterfaceEventListener> listeners = new HashSet<>();

    public void addEventListener(IStructArrayInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IStructArrayInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void firePropBoolChanged(StructBool[] newValue) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onPropBoolChanged(newValue);
      }
    }
  
    @Override
    public void firePropIntChanged(StructInt[] newValue) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onPropIntChanged(newValue);
      }
    }
  
    @Override
    public void firePropFloatChanged(StructFloat[] newValue) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onPropFloatChanged(newValue);
      }
    }
  
    @Override
    public void firePropStringChanged(StructString[] newValue) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onPropStringChanged(newValue);
      }
    }
  
    @Override
    public void fireSigBool(StructBool[] paramBool) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onSigBool(paramBool);
      }
    }
  
    @Override
    public void fireSigInt(StructInt[] paramInt) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onSigInt(paramInt);
      }
    }
  
    @Override
    public void fireSigFloat(StructFloat[] paramFloat) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onSigFloat(paramFloat);
      }
    }
  
    @Override
    public void fireSigString(StructString[] paramString) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onSigString(paramString);
      }
    }
  
    
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
