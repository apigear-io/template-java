package tb.same1.api;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TbSame1 {

  // enumerations
  public enum Enum1 {
      @JsonProperty("1")
      Value1,
      @JsonProperty("2")
      Value2,
  }
  public enum Enum2 {
      @JsonProperty("1")
      Value1,
      @JsonProperty("2")
      Value2,
  }

  // data structures
  public static class Struct1 {
    public Struct1(int field1, int field2, int field3) {
      this.field1 = field1;
      this.field2 = field2;
      this.field3 = field3;
    }
    @JsonProperty("field1")
    public int field1;
    @JsonProperty("field2")
    public int field2;
    @JsonProperty("field3")
    public int field3;
  }
  public static class Struct2 {
    public Struct2(int field1, int field2, int field3) {
      this.field1 = field1;
      this.field2 = field2;
      this.field3 = field3;
    }
    @JsonProperty("field1")
    public int field1;
    @JsonProperty("field2")
    public int field2;
    @JsonProperty("field3")
    public int field3;
  }

  // interfaces
  public static interface ISameStruct1InterfaceEventListener {
    void onProp1Changed(Struct1 oldValue, Struct1 newValue);
    void onSig1(Struct1 param1);
  }

  public static interface ISameStruct1Interface {
    // properties
    void setProp1(Struct1 prop1);
    Struct1 getProp1();
    void fireProp1Changed(Struct1 oldValue, Struct1 newValue);
  
    // methods
    Struct1 func1(Struct1 param1);

    // signal listeners
    void addEventListener(ISameStruct1InterfaceEventListener listener);
    void removeEventListener(ISameStruct1InterfaceEventListener listener);
  }

  public static class AbstractSameStruct1Interface implements ISameStruct1Interface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireProp1Changed(Struct1 oldValue, Struct1 newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp1Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireSig1(Struct1 param1) {
      for (ISig1EventListener listener : events) {
        listener.onSig1(param1);
      }
    }
  
    
  }
  public static interface ISameStruct2InterfaceEventListener {
    void onProp1Changed(Struct2 oldValue, Struct2 newValue);
    void onProp2Changed(Struct2 oldValue, Struct2 newValue);
    void onSig1(Struct1 param1);
    void onSig2(Struct1 param1, Struct2 param2);
  }

  public static interface ISameStruct2Interface {
    // properties
    void setProp1(Struct2 prop1);
    Struct2 getProp1();
    void fireProp1Changed(Struct2 oldValue, Struct2 newValue);
  
    void setProp2(Struct2 prop2);
    Struct2 getProp2();
    void fireProp2Changed(Struct2 oldValue, Struct2 newValue);
  
    // methods
    Struct1 func1(Struct1 param1);
    Struct1 func2(Struct1 param1, Struct2 param2);

    // signal listeners
    void addEventListener(ISameStruct2InterfaceEventListener listener);
    void removeEventListener(ISameStruct2InterfaceEventListener listener);
  }

  public static class AbstractSameStruct2Interface implements ISameStruct2Interface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireProp1Changed(Struct2 oldValue, Struct2 newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp1Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireProp2Changed(Struct2 oldValue, Struct2 newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp2Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireSig1(Struct1 param1) {
      for (ISig1EventListener listener : events) {
        listener.onSig1(param1);
      }
    }
  
    @Override
    public void fireSig2(Struct1 param1, Struct2 param2) {
      for (ISig2EventListener listener : events) {
        listener.onSig2(param1, param2);
      }
    }
  
    
  }
  public static interface ISameEnum1InterfaceEventListener {
    void onProp1Changed(Enum1 oldValue, Enum1 newValue);
    void onSig1(Enum1 param1);
  }

  public static interface ISameEnum1Interface {
    // properties
    void setProp1(Enum1 prop1);
    Enum1 getProp1();
    void fireProp1Changed(Enum1 oldValue, Enum1 newValue);
  
    // methods
    Enum1 func1(Enum1 param1);

    // signal listeners
    void addEventListener(ISameEnum1InterfaceEventListener listener);
    void removeEventListener(ISameEnum1InterfaceEventListener listener);
  }

  public static class AbstractSameEnum1Interface implements ISameEnum1Interface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireProp1Changed(Enum1 oldValue, Enum1 newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp1Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireSig1(Enum1 param1) {
      for (ISig1EventListener listener : events) {
        listener.onSig1(param1);
      }
    }
  
    
  }
  public static interface ISameEnum2InterfaceEventListener {
    void onProp1Changed(Enum1 oldValue, Enum1 newValue);
    void onProp2Changed(Enum2 oldValue, Enum2 newValue);
    void onSig1(Enum1 param1);
    void onSig2(Enum1 param1, Enum2 param2);
  }

  public static interface ISameEnum2Interface {
    // properties
    void setProp1(Enum1 prop1);
    Enum1 getProp1();
    void fireProp1Changed(Enum1 oldValue, Enum1 newValue);
  
    void setProp2(Enum2 prop2);
    Enum2 getProp2();
    void fireProp2Changed(Enum2 oldValue, Enum2 newValue);
  
    // methods
    Enum1 func1(Enum1 param1);
    Enum1 func2(Enum1 param1, Enum2 param2);

    // signal listeners
    void addEventListener(ISameEnum2InterfaceEventListener listener);
    void removeEventListener(ISameEnum2InterfaceEventListener listener);
  }

  public static class AbstractSameEnum2Interface implements ISameEnum2Interface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
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
    public void fireSig1(Enum1 param1) {
      for (ISig1EventListener listener : events) {
        listener.onSig1(param1);
      }
    }
  
    @Override
    public void fireSig2(Enum1 param1, Enum2 param2) {
      for (ISig2EventListener listener : events) {
        listener.onSig2(param1, param2);
      }
    }
  
    
  }
}