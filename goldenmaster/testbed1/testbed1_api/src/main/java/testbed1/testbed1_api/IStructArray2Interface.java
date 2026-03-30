package testbed1.testbed1_api;

import testbed1.testbed1_api.IStructArray2InterfaceEventListener;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_api.StructStruct;
import testbed1.testbed1_api.StructEnum;
import testbed1.testbed1_api.StructBoolWithArray;
import testbed1.testbed1_api.StructIntWithArray;
import testbed1.testbed1_api.StructFloatWithArray;
import testbed1.testbed1_api.StructStringWithArray;
import testbed1.testbed1_api.StructStructWithArray;
import testbed1.testbed1_api.StructEnumWithArray;
import testbed1.testbed1_api.Enum0;

import java.util.List;
import java.util.concurrent.CompletableFuture;


  public interface IStructArray2Interface {
    // properties
    void setPropBool(StructBoolWithArray propBool);
    StructBoolWithArray getPropBool();
    void firePropBoolChanged(StructBoolWithArray newValue);
  
    void setPropInt(StructIntWithArray propInt);
    StructIntWithArray getPropInt();
    void firePropIntChanged(StructIntWithArray newValue);
  
    void setPropFloat(StructFloatWithArray propFloat);
    StructFloatWithArray getPropFloat();
    void firePropFloatChanged(StructFloatWithArray newValue);
  
    void setPropString(StructStringWithArray propString);
    StructStringWithArray getPropString();
    void firePropStringChanged(StructStringWithArray newValue);
  
    void setPropEnum(StructEnumWithArray propEnum);
    StructEnumWithArray getPropEnum();
    void firePropEnumChanged(StructEnumWithArray newValue);
  
    // methods
    List<StructBool> funcBool(StructBoolWithArray paramBool);
    CompletableFuture<List<StructBool>> funcBoolAsync(StructBoolWithArray paramBool);
    List<StructInt> funcInt(StructIntWithArray paramInt);
    CompletableFuture<List<StructInt>> funcIntAsync(StructIntWithArray paramInt);
    List<StructFloat> funcFloat(StructFloatWithArray paramFloat);
    CompletableFuture<List<StructFloat>> funcFloatAsync(StructFloatWithArray paramFloat);
    List<StructString> funcString(StructStringWithArray paramString);
    CompletableFuture<List<StructString>> funcStringAsync(StructStringWithArray paramString);
    List<Enum0> funcEnum(StructEnumWithArray paramEnum);
    CompletableFuture<List<Enum0>> funcEnumAsync(StructEnumWithArray paramEnum);
    public void fireSigBool(StructBoolWithArray paramBool);
    public void fireSigInt(StructIntWithArray paramInt);
    public void fireSigFloat(StructFloatWithArray paramFloat);
    public void fireSigString(StructStringWithArray paramString);
    boolean _isReady();
    void _shutdown();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(IStructArray2InterfaceEventListener listener);
    void removeEventListener(IStructArray2InterfaceEventListener listener);
  }
