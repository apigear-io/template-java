package tb.enum.api;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TbEnum {

  // enumerations
  public enum Enum0 {
      @JsonProperty("0")
      Value0,
      @JsonProperty("1")
      Value1,
      @JsonProperty("2")
      Value2,
  }
  public enum Enum1 {
      @JsonProperty("1")
      Value1,
      @JsonProperty("2")
      Value2,
      @JsonProperty("3")
      Value3,
  }
  public enum Enum2 {
      @JsonProperty("2")
      Value2,
      @JsonProperty("1")
      Value1,
      @JsonProperty("0")
      Value0,
  }
  public enum Enum3 {
      @JsonProperty("3")
      Value3,
      @JsonProperty("2")
      Value2,
      @JsonProperty("1")
      Value1,
  }

  // data structures

  // interfaces
  public static interface IEnumInterfaceEventListener {
    void onProp0Changed(Enum0 oldValue, Enum0 newValue);
    void onProp1Changed(Enum1 oldValue, Enum1 newValue);
    void onProp2Changed(Enum2 oldValue, Enum2 newValue);
    void onProp3Changed(Enum3 oldValue, Enum3 newValue);
    void onSig0(Enum0 param0);
    void onSig1(Enum1 param1);
    void onSig2(Enum2 param2);
    void onSig3(Enum3 param3);
  }

  public static interface IEnumInterface {
    // properties
    void setProp0(Enum0 prop0);
    Enum0 getProp0();
    void fireProp0Changed(Enum0 oldValue, Enum0 newValue);
  
    void setProp1(Enum1 prop1);
    Enum1 getProp1();
    void fireProp1Changed(Enum1 oldValue, Enum1 newValue);
  
    void setProp2(Enum2 prop2);
    Enum2 getProp2();
    void fireProp2Changed(Enum2 oldValue, Enum2 newValue);
  
    void setProp3(Enum3 prop3);
    Enum3 getProp3();
    void fireProp3Changed(Enum3 oldValue, Enum3 newValue);
  
    // methods
    Enum0 func0(Enum0 param0);
    Enum1 func1(Enum1 param1);
    Enum2 func2(Enum2 param2);
    Enum3 func3(Enum3 param3);

    // signal listeners
    void addEventListener(IEnumInterfaceEventListener listener);
    void removeEventListener(IEnumInterfaceEventListener listener);
  }

  public static class AbstractEnumInterface implements IEnumInterface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireProp0Changed(Enum0 oldValue, Enum0 newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp0Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireProp1Changed(Enum1 oldValue, Enum1 newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp1Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireProp2Changed(Enum2 oldValue, Enum2 newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp2Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireProp3Changed(Enum3 oldValue, Enum3 newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp3Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireSig0(Enum0 param0) {
      for (ISig0EventListener listener : events) {
        listener.onSig0(param0);
      }
    }
  
    @Override
    public void fireSig1(Enum1 param1) {
      for (ISig1EventListener listener : events) {
        listener.onSig1(param1);
      }
    }
  
    @Override
    public void fireSig2(Enum2 param2) {
      for (ISig2EventListener listener : events) {
        listener.onSig2(param2);
      }
    }
  
    @Override
    public void fireSig3(Enum3 param3) {
      for (ISig3EventListener listener : events) {
        listener.onSig3(param3);
      }
    }
  
    
  }
}