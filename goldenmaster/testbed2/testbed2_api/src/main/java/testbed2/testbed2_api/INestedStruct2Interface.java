package testbed2.testbed2_api;

import testbed2.testbed2_api.INestedStruct2InterfaceEventListener;
import testbed2.testbed2_api.Struct1;
import testbed2.testbed2_api.Struct2;
import testbed2.testbed2_api.Struct3;
import testbed2.testbed2_api.Struct4;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_api.NestedStruct2;
import testbed2.testbed2_api.NestedStruct3;
import testbed2.testbed2_api.Enum1;
import testbed2.testbed2_api.Enum2;
import testbed2.testbed2_api.Enum3;

import java.util.List;
import java.util.concurrent.CompletableFuture;


  public interface INestedStruct2Interface {
    // properties
    void setProp1(NestedStruct1 prop1);
    NestedStruct1 getProp1();
    void fireProp1Changed(NestedStruct1 newValue);
  
    void setProp2(NestedStruct2 prop2);
    NestedStruct2 getProp2();
    void fireProp2Changed(NestedStruct2 newValue);
  
    // methods
    NestedStruct1 func1(NestedStruct1 param1);
    CompletableFuture<NestedStruct1> func1Async(NestedStruct1 param1);
    NestedStruct1 func2(NestedStruct1 param1, NestedStruct2 param2);
    CompletableFuture<NestedStruct1> func2Async(NestedStruct1 param1, NestedStruct2 param2);
    public void fireSig1(NestedStruct1 param1);
    public void fireSig2(NestedStruct1 param1, NestedStruct2 param2);
    boolean _isReady();
    void _shutdown();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(INestedStruct2InterfaceEventListener listener);
    void removeEventListener(INestedStruct2InterfaceEventListener listener);
  }
