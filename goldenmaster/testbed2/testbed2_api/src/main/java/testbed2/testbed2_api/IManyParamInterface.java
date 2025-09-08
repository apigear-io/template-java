package testbed2.testbed2_api;

import testbed2.testbed2_api.IManyParamInterfaceEventListener;
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

import java.util.concurrent.CompletableFuture;


  public interface IManyParamInterface {
    // properties
    void setProp1(int prop1);
    int getProp1();
    void fireProp1Changed(int newValue);
  
    void setProp2(int prop2);
    int getProp2();
    void fireProp2Changed(int newValue);
  
    void setProp3(int prop3);
    int getProp3();
    void fireProp3Changed(int newValue);
  
    void setProp4(int prop4);
    int getProp4();
    void fireProp4Changed(int newValue);
  
    // methods
    int func1(int param1);
    CompletableFuture<Integer> func1Async(int param1);
    int func2(int param1, int param2);
    CompletableFuture<Integer> func2Async(int param1, int param2);
    int func3(int param1, int param2, int param3);
    CompletableFuture<Integer> func3Async(int param1, int param2, int param3);
    int func4(int param1, int param2, int param3, int param4);
    CompletableFuture<Integer> func4Async(int param1, int param2, int param3, int param4);
    public void fireSig1(int param1);
    public void fireSig2(int param1, int param2);
    public void fireSig3(int param1, int param2, int param3);
    public void fireSig4(int param1, int param2, int param3, int param4);
    boolean _isReady();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(IManyParamInterfaceEventListener listener);
    void removeEventListener(IManyParamInterfaceEventListener listener);
  }
