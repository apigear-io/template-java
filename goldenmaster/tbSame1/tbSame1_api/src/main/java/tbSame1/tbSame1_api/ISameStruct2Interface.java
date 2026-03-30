package tbSame1.tbSame1_api;

import tbSame1.tbSame1_api.ISameStruct2InterfaceEventListener;
import tbSame1.tbSame1_api.Struct1;
import tbSame1.tbSame1_api.Struct2;
import tbSame1.tbSame1_api.Enum1;
import tbSame1.tbSame1_api.Enum2;

import java.util.List;
import java.util.concurrent.CompletableFuture;


  public interface ISameStruct2Interface {
    // properties
    void setProp1(Struct2 prop1);
    Struct2 getProp1();
    void fireProp1Changed(Struct2 newValue);
  
    void setProp2(Struct2 prop2);
    Struct2 getProp2();
    void fireProp2Changed(Struct2 newValue);
  
    // methods
    Struct1 func1(Struct1 param1);
    CompletableFuture<Struct1> func1Async(Struct1 param1);
    Struct1 func2(Struct1 param1, Struct2 param2);
    CompletableFuture<Struct1> func2Async(Struct1 param1, Struct2 param2);
    public void fireSig1(Struct1 param1);
    public void fireSig2(Struct1 param1, Struct2 param2);
    boolean _isReady();
    void _shutdown();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(ISameStruct2InterfaceEventListener listener);
    void removeEventListener(ISameStruct2InterfaceEventListener listener);
  }
