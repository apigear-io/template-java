package tbSame2.tbSame2_api;

import tbSame2.tbSame2_api.ISameEnum1InterfaceEventListener;
import tbSame2.tbSame2_api.Struct1;
import tbSame2.tbSame2_api.Struct2;
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_api.Enum2;

import java.util.concurrent.CompletableFuture;


  public interface ISameEnum1Interface {
    // properties
    void setProp1(Enum1 prop1);
    Enum1 getProp1();
    void fireProp1Changed(Enum1 newValue);
  
    // methods
    Enum1 func1(Enum1 param1);
    CompletableFuture<Enum1> func1Async(Enum1 param1);
    public void fireSig1(Enum1 param1);
    boolean _isReady();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(ISameEnum1InterfaceEventListener listener);
    void removeEventListener(ISameEnum1InterfaceEventListener listener);
  }
