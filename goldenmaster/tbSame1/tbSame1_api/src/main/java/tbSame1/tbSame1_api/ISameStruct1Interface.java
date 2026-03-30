package tbSame1.tbSame1_api;

import tbSame1.tbSame1_api.ISameStruct1InterfaceEventListener;
import tbSame1.tbSame1_api.Struct1;
import tbSame1.tbSame1_api.Struct2;
import tbSame1.tbSame1_api.Enum1;
import tbSame1.tbSame1_api.Enum2;

import java.util.List;
import java.util.concurrent.CompletableFuture;


  public interface ISameStruct1Interface {
    // properties
    void setProp1(Struct1 prop1);
    Struct1 getProp1();
    void fireProp1Changed(Struct1 newValue);
  
    // methods
    Struct1 func1(Struct1 param1);
    CompletableFuture<Struct1> func1Async(Struct1 param1);
    public void fireSig1(Struct1 param1);
    boolean _isReady();
    void _shutdown();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(ISameStruct1InterfaceEventListener listener);
    void removeEventListener(ISameStruct1InterfaceEventListener listener);
  }
