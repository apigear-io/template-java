package tbEnum.tbEnum_api;

import tbEnum.tbEnum_api.IEnumInterfaceEventListener;
import tbEnum.tbEnum_api.Enum0;
import tbEnum.tbEnum_api.Enum1;
import tbEnum.tbEnum_api.Enum2;
import tbEnum.tbEnum_api.Enum3;

import java.util.concurrent.CompletableFuture;


  public interface IEnumInterface {
    // properties
    void setProp0(Enum0 prop0);
    Enum0 getProp0();
    void fireProp0Changed(Enum0 newValue);
  
    void setProp1(Enum1 prop1);
    Enum1 getProp1();
    void fireProp1Changed(Enum1 newValue);
  
    void setProp2(Enum2 prop2);
    Enum2 getProp2();
    void fireProp2Changed(Enum2 newValue);
  
    void setProp3(Enum3 prop3);
    Enum3 getProp3();
    void fireProp3Changed(Enum3 newValue);
  
    // methods
    Enum0 func0(Enum0 param0);
    CompletableFuture<Enum0> func0Async(Enum0 param0);
    Enum1 func1(Enum1 param1);
    CompletableFuture<Enum1> func1Async(Enum1 param1);
    Enum2 func2(Enum2 param2);
    CompletableFuture<Enum2> func2Async(Enum2 param2);
    Enum3 func3(Enum3 param3);
    CompletableFuture<Enum3> func3Async(Enum3 param3);
    public void fireSig0(Enum0 param0);
    public void fireSig1(Enum1 param1);
    public void fireSig2(Enum2 param2);
    public void fireSig3(Enum3 param3);
    boolean _isReady();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(IEnumInterfaceEventListener listener);
    void removeEventListener(IEnumInterfaceEventListener listener);
  }
