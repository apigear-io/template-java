package testbed1.testbed1_api;

import testbed1.testbed1_api.IStructArrayInterfaceEventListener;
import testbed1.testbed1_api.IStructArrayInterface;
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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
  public abstract class AbstractStructArrayInterface implements IStructArrayInterface {
    private Collection<IStructArrayInterfaceEventListener> listeners = ConcurrentHashMap.newKeySet();

    @Override
    public void addEventListener(IStructArrayInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    @Override
    public void removeEventListener(IStructArrayInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void firePropBoolChanged(List<StructBool> newValue) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onPropBoolChanged(newValue);
      }
    }
  
    @Override
    public void firePropIntChanged(List<StructInt> newValue) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onPropIntChanged(newValue);
      }
    }
  
    @Override
    public void firePropFloatChanged(List<StructFloat> newValue) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onPropFloatChanged(newValue);
      }
    }
  
    @Override
    public void firePropStringChanged(List<StructString> newValue) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onPropStringChanged(newValue);
      }
    }
  
    @Override
    public void firePropEnumChanged(List<Enum0> newValue) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onPropEnumChanged(newValue);
      }
    }
  
    @Override
    public void fireSigBool(List<StructBool> paramBool) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onSigBool(paramBool);
      }
    }
  
    @Override
    public void fireSigInt(List<StructInt> paramInt) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onSigInt(paramInt);
      }
    }
  
    @Override
    public void fireSigFloat(List<StructFloat> paramFloat) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onSigFloat(paramFloat);
      }
    }
  
    @Override
    public void fireSigString(List<StructString> paramString) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onSigString(paramString);
      }
    }
  
    @Override
    public void fireSigEnum(List<Enum0> paramEnum) {
      for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.onSigEnum(paramEnum);
      }
    }
  
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (IStructArrayInterfaceEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }

    @Override
    public void _shutdown() {}
  }
