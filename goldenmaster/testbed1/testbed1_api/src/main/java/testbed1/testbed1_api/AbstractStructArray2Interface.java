package testbed1.testbed1_api;

import testbed1.testbed1_api.IStructArray2InterfaceEventListener;
import testbed1.testbed1_api.IStructArray2Interface;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_api.StructStruct;
import testbed1.testbed1_api.StructEnum;
import testbed1.testbed1_api.StructBoolWithArray;
import testbed1.testbed1_api.StructIntWithArray;
import testbed1.testbed1_api.StructFloatWithArray;
import testbed1.testbed1_api.StructStringWithArray;
import testbed1.testbed1_api.StructStructWithArray;
import testbed1.testbed1_api.StructEnumWithArray;
import testbed1.testbed1_api.Enum0;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractStructArray2Interface implements IStructArray2Interface {
    public Collection<IStructArray2InterfaceEventListener> listeners = new HashSet<>();

    public void addEventListener(IStructArray2InterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IStructArray2InterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void firePropBoolChanged(StructBoolWithArray newValue) {
      for (IStructArray2InterfaceEventListener listener : listeners) {
        listener.onPropBoolChanged(newValue);
      }
    }
  
    @Override
    public void firePropIntChanged(StructIntWithArray newValue) {
      for (IStructArray2InterfaceEventListener listener : listeners) {
        listener.onPropIntChanged(newValue);
      }
    }
  
    @Override
    public void firePropFloatChanged(StructFloatWithArray newValue) {
      for (IStructArray2InterfaceEventListener listener : listeners) {
        listener.onPropFloatChanged(newValue);
      }
    }
  
    @Override
    public void firePropStringChanged(StructStringWithArray newValue) {
      for (IStructArray2InterfaceEventListener listener : listeners) {
        listener.onPropStringChanged(newValue);
      }
    }
  
    @Override
    public void firePropEnumChanged(StructEnumWithArray newValue) {
      for (IStructArray2InterfaceEventListener listener : listeners) {
        listener.onPropEnumChanged(newValue);
      }
    }
  
    @Override
    public void fireSigBool(StructBoolWithArray paramBool) {
      for (IStructArray2InterfaceEventListener listener : listeners) {
        listener.onSigBool(paramBool);
      }
    }
  
    @Override
    public void fireSigInt(StructIntWithArray paramInt) {
      for (IStructArray2InterfaceEventListener listener : listeners) {
        listener.onSigInt(paramInt);
      }
    }
  
    @Override
    public void fireSigFloat(StructFloatWithArray paramFloat) {
      for (IStructArray2InterfaceEventListener listener : listeners) {
        listener.onSigFloat(paramFloat);
      }
    }
  
    @Override
    public void fireSigString(StructStringWithArray paramString) {
      for (IStructArray2InterfaceEventListener listener : listeners) {
        listener.onSigString(paramString);
      }
    }
  
    
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (IStructArray2InterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
