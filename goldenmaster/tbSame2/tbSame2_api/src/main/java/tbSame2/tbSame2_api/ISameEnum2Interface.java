package tbSame2.tbSame2_api;

import tbSame2.tbSame2_api.ISameEnum2InterfaceEventListener;
import tbSame2.tbSame2_api.Struct1;
import tbSame2.tbSame2_api.Struct2;
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_api.Enum2;

import java.util.concurrent.CompletableFuture;


  public interface ISameEnum2Interface {
    // properties
    void setProp1(Enum1 prop1);
    Enum1 getProp1();
    void fireProp1Changed(Enum1 newValue);
  
    void setProp2(Enum2 prop2);
    Enum2 getProp2();
    void fireProp2Changed(Enum2 newValue);
  
    // methods
    Enum1 func1(Enum1 param1);
    CompletableFuture<Enum1> func1Async(Enum1 param1);
    Enum1 func2(Enum1 param1, Enum2 param2);
    CompletableFuture<Enum1> func2Async(Enum1 param1, Enum2 param2);
    public void fireSig1(Enum1 param1);
    public void fireSig2(Enum1 param1, Enum2 param2);
    boolean _isReady();
    void _shutdown();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(ISameEnum2InterfaceEventListener listener);
    void removeEventListener(ISameEnum2InterfaceEventListener listener);
  }
