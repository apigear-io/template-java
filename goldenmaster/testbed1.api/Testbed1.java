package testbed1.api;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Testbed1 {

  // enumerations

  // data structures
  public static class StructBool {
    public StructBool(boolean fieldBool) {
      this.fieldBool = fieldBool;
    }
    @JsonProperty("field_bool")
    public boolean fieldBool;
  }
  public static class StructInt {
    public StructInt(int fieldInt) {
      this.fieldInt = fieldInt;
    }
    @JsonProperty("field_int")
    public int fieldInt;
  }
  public static class StructFloat {
    public StructFloat(float fieldFloat) {
      this.fieldFloat = fieldFloat;
    }
    @JsonProperty("field_float")
    public float fieldFloat;
  }
  public static class StructString {
    public StructString(String fieldString) {
      this.fieldString = fieldString;
    }
    @JsonProperty("field_string")
    public String fieldString;
  }

  // interfaces
  public static interface IStructInterfaceEventListener {
    void onPropBoolChanged(StructBool oldValue, StructBool newValue);
    void onPropIntChanged(StructInt oldValue, StructInt newValue);
    void onPropFloatChanged(StructFloat oldValue, StructFloat newValue);
    void onPropStringChanged(StructString oldValue, StructString newValue);
    void onSigBool(StructBool paramBool);
    void onSigInt(StructInt paramInt);
    void onSigFloat(StructFloat paramFloat);
    void onSigString(StructString paramString);
  }

  public static interface IStructInterface {
    // properties
    void setPropBool(StructBool propBool);
    StructBool getPropBool();
    void firePropBoolChanged(StructBool oldValue, StructBool newValue);
  
    void setPropInt(StructInt propInt);
    StructInt getPropInt();
    void firePropIntChanged(StructInt oldValue, StructInt newValue);
  
    void setPropFloat(StructFloat propFloat);
    StructFloat getPropFloat();
    void firePropFloatChanged(StructFloat oldValue, StructFloat newValue);
  
    void setPropString(StructString propString);
    StructString getPropString();
    void firePropStringChanged(StructString oldValue, StructString newValue);
  
    // methods
    StructBool funcBool(StructBool paramBool);
    StructInt funcInt(StructInt paramInt);
    StructFloat funcFloat(StructFloat paramFloat);
    StructString funcString(StructString paramString);

    // signal listeners
    void addEventListener(IStructInterfaceEventListener listener);
    void removeEventListener(IStructInterfaceEventListener listener);
  }

  public static class AbstractStructInterface implements IStructInterface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void firePropBoolChanged(StructBool oldValue, StructBool newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropBoolChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropIntChanged(StructInt oldValue, StructInt newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropIntChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropFloatChanged(StructFloat oldValue, StructFloat newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropFloatChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropStringChanged(StructString oldValue, StructString newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropStringChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void fireSigBool(StructBool paramBool) {
      for (ISigBoolEventListener listener : events) {
        listener.onSigBool(paramBool);
      }
    }
  
    @Override
    public void fireSigInt(StructInt paramInt) {
      for (ISigIntEventListener listener : events) {
        listener.onSigInt(paramInt);
      }
    }
  
    @Override
    public void fireSigFloat(StructFloat paramFloat) {
      for (ISigFloatEventListener listener : events) {
        listener.onSigFloat(paramFloat);
      }
    }
  
    @Override
    public void fireSigString(StructString paramString) {
      for (ISigStringEventListener listener : events) {
        listener.onSigString(paramString);
      }
    }
  
    
  }
  public static interface IStructArrayInterfaceEventListener {
    void onPropBoolChanged(StructBool[] oldValue, StructBool[] newValue);
    void onPropIntChanged(StructInt[] oldValue, StructInt[] newValue);
    void onPropFloatChanged(StructFloat[] oldValue, StructFloat[] newValue);
    void onPropStringChanged(StructString[] oldValue, StructString[] newValue);
    void onSigBool(StructBool[] paramBool);
    void onSigInt(StructInt[] paramInt);
    void onSigFloat(StructFloat[] paramFloat);
    void onSigString(StructString[] paramString);
  }

  public static interface IStructArrayInterface {
    // properties
    void setPropBool(StructBool[] propBool);
    StructBool[] getPropBool();
    void firePropBoolChanged(StructBool[] oldValue, StructBool[] newValue);
  
    void setPropInt(StructInt[] propInt);
    StructInt[] getPropInt();
    void firePropIntChanged(StructInt[] oldValue, StructInt[] newValue);
  
    void setPropFloat(StructFloat[] propFloat);
    StructFloat[] getPropFloat();
    void firePropFloatChanged(StructFloat[] oldValue, StructFloat[] newValue);
  
    void setPropString(StructString[] propString);
    StructString[] getPropString();
    void firePropStringChanged(StructString[] oldValue, StructString[] newValue);
  
    // methods
    StructBool[] funcBool(StructBool[] paramBool);
    StructInt[] funcInt(StructInt[] paramInt);
    StructFloat[] funcFloat(StructFloat[] paramFloat);
    StructString[] funcString(StructString[] paramString);

    // signal listeners
    void addEventListener(IStructArrayInterfaceEventListener listener);
    void removeEventListener(IStructArrayInterfaceEventListener listener);
  }

  public static class AbstractStructArrayInterface implements IStructArrayInterface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void firePropBoolChanged(StructBool[] oldValue, StructBool[] newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropBoolChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropIntChanged(StructInt[] oldValue, StructInt[] newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropIntChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropFloatChanged(StructFloat[] oldValue, StructFloat[] newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropFloatChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropStringChanged(StructString[] oldValue, StructString[] newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropStringChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void fireSigBool(StructBool[] paramBool) {
      for (ISigBoolEventListener listener : events) {
        listener.onSigBool(paramBool);
      }
    }
  
    @Override
    public void fireSigInt(StructInt[] paramInt) {
      for (ISigIntEventListener listener : events) {
        listener.onSigInt(paramInt);
      }
    }
  
    @Override
    public void fireSigFloat(StructFloat[] paramFloat) {
      for (ISigFloatEventListener listener : events) {
        listener.onSigFloat(paramFloat);
      }
    }
  
    @Override
    public void fireSigString(StructString[] paramString) {
      for (ISigStringEventListener listener : events) {
        listener.onSigString(paramString);
      }
    }
  
    
  }
}