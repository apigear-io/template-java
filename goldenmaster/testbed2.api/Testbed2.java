package testbed2.api;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Testbed2 {

  // enumerations
  public enum Enum1 {
      @JsonProperty("1")
      Value1,
      @JsonProperty("2")
      Value2,
      @JsonProperty("3")
      Value3,
      @JsonProperty("4")
      Value4,
  }
  public enum Enum2 {
      @JsonProperty("1")
      Value1,
      @JsonProperty("2")
      Value2,
      @JsonProperty("3")
      Value3,
      @JsonProperty("4")
      Value4,
  }
  public enum Enum3 {
      @JsonProperty("1")
      Value1,
      @JsonProperty("2")
      Value2,
      @JsonProperty("3")
      Value3,
      @JsonProperty("4")
      Value4,
  }

  // data structures
  public static class Struct1 {
    public Struct1(int field1) {
      this.field1 = field1;
    }
    @JsonProperty("field1")
    public int field1;
  }
  public static class Struct2 {
    public Struct2(int field1, int field2) {
      this.field1 = field1;
      this.field2 = field2;
    }
    @JsonProperty("field1")
    public int field1;
    @JsonProperty("field2")
    public int field2;
  }
  public static class Struct3 {
    public Struct3(int field1, int field2, int field3) {
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
  public static class Struct4 {
    public Struct4(int field1, int field2, int field3, int field4) {
      this.field1 = field1;
      this.field2 = field2;
      this.field3 = field3;
      this.field4 = field4;
    }
    @JsonProperty("field1")
    public int field1;
    @JsonProperty("field2")
    public int field2;
    @JsonProperty("field3")
    public int field3;
    @JsonProperty("field4")
    public int field4;
  }
  public static class NestedStruct1 {
    public NestedStruct1(Struct1 field1) {
      this.field1 = field1;
    }
    @JsonProperty("field1")
    public Struct1 field1;
  }
  public static class NestedStruct2 {
    public NestedStruct2(Struct1 field1, Struct2 field2) {
      this.field1 = field1;
      this.field2 = field2;
    }
    @JsonProperty("field1")
    public Struct1 field1;
    @JsonProperty("field2")
    public Struct2 field2;
  }
  public static class NestedStruct3 {
    public NestedStruct3(Struct1 field1, Struct2 field2, Struct3 field3) {
      this.field1 = field1;
      this.field2 = field2;
      this.field3 = field3;
    }
    @JsonProperty("field1")
    public Struct1 field1;
    @JsonProperty("field2")
    public Struct2 field2;
    @JsonProperty("field3")
    public Struct3 field3;
  }

  // interfaces
  public static interface IManyParamInterfaceEventListener {
    void onProp1Changed(int oldValue, int newValue);
    void onProp2Changed(int oldValue, int newValue);
    void onProp3Changed(int oldValue, int newValue);
    void onProp4Changed(int oldValue, int newValue);
    void onSig1(int param1);
    void onSig2(int param1, int param2);
    void onSig3(int param1, int param2, int param3);
    void onSig4(int param1, int param2, int param3, int param4);
  }

  public static interface IManyParamInterface {
    // properties
    void setProp1(int prop1);
    int getProp1();
    void fireProp1Changed(int oldValue, int newValue);
  
    void setProp2(int prop2);
    int getProp2();
    void fireProp2Changed(int oldValue, int newValue);
  
    void setProp3(int prop3);
    int getProp3();
    void fireProp3Changed(int oldValue, int newValue);
  
    void setProp4(int prop4);
    int getProp4();
    void fireProp4Changed(int oldValue, int newValue);
  
    // methods
    int func1(int param1);
    int func2(int param1, int param2);
    int func3(int param1, int param2, int param3);
    int func4(int param1, int param2, int param3, int param4);

    // signal listeners
    void addEventListener(IManyParamInterfaceEventListener listener);
    void removeEventListener(IManyParamInterfaceEventListener listener);
  }

  public static class AbstractManyParamInterface implements IManyParamInterface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireProp1Changed(int oldValue, int newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp1Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireProp2Changed(int oldValue, int newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp2Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireProp3Changed(int oldValue, int newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp3Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireProp4Changed(int oldValue, int newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp4Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireSig1(int param1) {
      for (ISig1EventListener listener : events) {
        listener.onSig1(param1);
      }
    }
  
    @Override
    public void fireSig2(int param1, int param2) {
      for (ISig2EventListener listener : events) {
        listener.onSig2(param1, param2);
      }
    }
  
    @Override
    public void fireSig3(int param1, int param2, int param3) {
      for (ISig3EventListener listener : events) {
        listener.onSig3(param1, param2, param3);
      }
    }
  
    @Override
    public void fireSig4(int param1, int param2, int param3, int param4) {
      for (ISig4EventListener listener : events) {
        listener.onSig4(param1, param2, param3, param4);
      }
    }
  
    
  }
  public static interface INestedStruct1InterfaceEventListener {
    void onProp1Changed(NestedStruct1 oldValue, NestedStruct1 newValue);
    void onSig1(NestedStruct1 param1);
  }

  public static interface INestedStruct1Interface {
    // properties
    void setProp1(NestedStruct1 prop1);
    NestedStruct1 getProp1();
    void fireProp1Changed(NestedStruct1 oldValue, NestedStruct1 newValue);
  
    // methods
    NestedStruct1 func1(NestedStruct1 param1);

    // signal listeners
    void addEventListener(INestedStruct1InterfaceEventListener listener);
    void removeEventListener(INestedStruct1InterfaceEventListener listener);
  }

  public static class AbstractNestedStruct1Interface implements INestedStruct1Interface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireProp1Changed(NestedStruct1 oldValue, NestedStruct1 newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp1Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireSig1(NestedStruct1 param1) {
      for (ISig1EventListener listener : events) {
        listener.onSig1(param1);
      }
    }
  
    
  }
  public static interface INestedStruct2InterfaceEventListener {
    void onProp1Changed(NestedStruct1 oldValue, NestedStruct1 newValue);
    void onProp2Changed(NestedStruct2 oldValue, NestedStruct2 newValue);
    void onSig1(NestedStruct1 param1);
    void onSig2(NestedStruct1 param1, NestedStruct2 param2);
  }

  public static interface INestedStruct2Interface {
    // properties
    void setProp1(NestedStruct1 prop1);
    NestedStruct1 getProp1();
    void fireProp1Changed(NestedStruct1 oldValue, NestedStruct1 newValue);
  
    void setProp2(NestedStruct2 prop2);
    NestedStruct2 getProp2();
    void fireProp2Changed(NestedStruct2 oldValue, NestedStruct2 newValue);
  
    // methods
    NestedStruct1 func1(NestedStruct1 param1);
    NestedStruct1 func2(NestedStruct1 param1, NestedStruct2 param2);

    // signal listeners
    void addEventListener(INestedStruct2InterfaceEventListener listener);
    void removeEventListener(INestedStruct2InterfaceEventListener listener);
  }

  public static class AbstractNestedStruct2Interface implements INestedStruct2Interface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireProp1Changed(NestedStruct1 oldValue, NestedStruct1 newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp1Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireProp2Changed(NestedStruct2 oldValue, NestedStruct2 newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp2Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireSig1(NestedStruct1 param1) {
      for (ISig1EventListener listener : events) {
        listener.onSig1(param1);
      }
    }
  
    @Override
    public void fireSig2(NestedStruct1 param1, NestedStruct2 param2) {
      for (ISig2EventListener listener : events) {
        listener.onSig2(param1, param2);
      }
    }
  
    
  }
  public static interface INestedStruct3InterfaceEventListener {
    void onProp1Changed(NestedStruct1 oldValue, NestedStruct1 newValue);
    void onProp2Changed(NestedStruct2 oldValue, NestedStruct2 newValue);
    void onProp3Changed(NestedStruct3 oldValue, NestedStruct3 newValue);
    void onSig1(NestedStruct1 param1);
    void onSig2(NestedStruct1 param1, NestedStruct2 param2);
    void onSig3(NestedStruct1 param1, NestedStruct2 param2, NestedStruct3 param3);
  }

  public static interface INestedStruct3Interface {
    // properties
    void setProp1(NestedStruct1 prop1);
    NestedStruct1 getProp1();
    void fireProp1Changed(NestedStruct1 oldValue, NestedStruct1 newValue);
  
    void setProp2(NestedStruct2 prop2);
    NestedStruct2 getProp2();
    void fireProp2Changed(NestedStruct2 oldValue, NestedStruct2 newValue);
  
    void setProp3(NestedStruct3 prop3);
    NestedStruct3 getProp3();
    void fireProp3Changed(NestedStruct3 oldValue, NestedStruct3 newValue);
  
    // methods
    NestedStruct1 func1(NestedStruct1 param1);
    NestedStruct1 func2(NestedStruct1 param1, NestedStruct2 param2);
    NestedStruct1 func3(NestedStruct1 param1, NestedStruct2 param2, NestedStruct3 param3);

    // signal listeners
    void addEventListener(INestedStruct3InterfaceEventListener listener);
    void removeEventListener(INestedStruct3InterfaceEventListener listener);
  }

  public static class AbstractNestedStruct3Interface implements INestedStruct3Interface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireProp1Changed(NestedStruct1 oldValue, NestedStruct1 newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp1Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireProp2Changed(NestedStruct2 oldValue, NestedStruct2 newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp2Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireProp3Changed(NestedStruct3 oldValue, NestedStruct3 newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onProp3Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void fireSig1(NestedStruct1 param1) {
      for (ISig1EventListener listener : events) {
        listener.onSig1(param1);
      }
    }
  
    @Override
    public void fireSig2(NestedStruct1 param1, NestedStruct2 param2) {
      for (ISig2EventListener listener : events) {
        listener.onSig2(param1, param2);
      }
    }
  
    @Override
    public void fireSig3(NestedStruct1 param1, NestedStruct2 param2, NestedStruct3 param3) {
      for (ISig3EventListener listener : events) {
        listener.onSig3(param1, param2, param3);
      }
    }
  
    
  }
}