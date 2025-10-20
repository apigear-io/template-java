package testbed1.testbed1_api;

import testbed1.testbed1_api.IStructArrayInterfaceEventListener;
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

import java.util.concurrent.CompletableFuture;


  public interface IStructArrayInterface {
    // properties
    void setPropBool(StructBool[] propBool);
    StructBool[] getPropBool();
    void firePropBoolChanged(StructBool[] newValue);
  
    void setPropInt(StructInt[] propInt);
    StructInt[] getPropInt();
    void firePropIntChanged(StructInt[] newValue);
  
    void setPropFloat(StructFloat[] propFloat);
    StructFloat[] getPropFloat();
    void firePropFloatChanged(StructFloat[] newValue);
  
    void setPropString(StructString[] propString);
    StructString[] getPropString();
    void firePropStringChanged(StructString[] newValue);
  
    void setPropEnum(Enum0[] propEnum);
    Enum0[] getPropEnum();
    void firePropEnumChanged(Enum0[] newValue);
  
    // methods
    StructBool[] funcBool(StructBool[] paramBool);
    CompletableFuture<StructBool[]> funcBoolAsync(StructBool[] paramBool);
    StructInt[] funcInt(StructInt[] paramInt);
    CompletableFuture<StructInt[]> funcIntAsync(StructInt[] paramInt);
    StructFloat[] funcFloat(StructFloat[] paramFloat);
    CompletableFuture<StructFloat[]> funcFloatAsync(StructFloat[] paramFloat);
    StructString[] funcString(StructString[] paramString);
    CompletableFuture<StructString[]> funcStringAsync(StructString[] paramString);
    Enum0[] funcEnum(Enum0[] paramEnum);
    CompletableFuture<Enum0[]> funcEnumAsync(Enum0[] paramEnum);
    public void fireSigBool(StructBool[] paramBool);
    public void fireSigInt(StructInt[] paramInt);
    public void fireSigFloat(StructFloat[] paramFloat);
    public void fireSigString(StructString[] paramString);
    public void fireSigEnum(Enum0[] paramEnum);
    boolean _isReady();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(IStructArrayInterfaceEventListener listener);
    void removeEventListener(IStructArrayInterfaceEventListener listener);
  }
