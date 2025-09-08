package testbed1.testbed1_api;

import testbed1.testbed1_api.IStructInterfaceEventListener;
import testbed1.testbed1_api.IStructInterface;
//TODO imported/extern modules
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_api.StructString;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractStructInterface implements IStructInterface {
    public Collection<IStructInterfaceEventListener> listeners = new HashSet<>();

    public void addEventListener(IStructInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IStructInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void firePropBoolChanged(StructBool newValue) {
      for (IStructInterfaceEventListener listener : listeners) {
        listener.onPropBoolChanged(newValue);
      }
    }
  
    @Override
    public void firePropIntChanged(StructInt newValue) {
      for (IStructInterfaceEventListener listener : listeners) {
        listener.onPropIntChanged(newValue);
      }
    }
  
    @Override
    public void firePropFloatChanged(StructFloat newValue) {
      for (IStructInterfaceEventListener listener : listeners) {
        listener.onPropFloatChanged(newValue);
      }
    }
  
    @Override
    public void firePropStringChanged(StructString newValue) {
      for (IStructInterfaceEventListener listener : listeners) {
        listener.onPropStringChanged(newValue);
      }
    }
  
    @Override
    public void fireSigBool(StructBool paramBool) {
      for (IStructInterfaceEventListener listener : listeners) {
        listener.onSigBool(paramBool);
      }
    }
  
    @Override
    public void fireSigInt(StructInt paramInt) {
      for (IStructInterfaceEventListener listener : listeners) {
        listener.onSigInt(paramInt);
      }
    }
  
    @Override
    public void fireSigFloat(StructFloat paramFloat) {
      for (IStructInterfaceEventListener listener : listeners) {
        listener.onSigFloat(paramFloat);
      }
    }
  
    @Override
    public void fireSigString(StructString paramString) {
      for (IStructInterfaceEventListener listener : listeners) {
        listener.onSigString(paramString);
      }
    }
  
    
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (IStructInterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
