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

import java.util.List;
import java.util.concurrent.CompletableFuture;


  public interface IStructArrayInterface {
    // properties
    void setPropBool(List<StructBool> propBool);
    List<StructBool> getPropBool();
    void firePropBoolChanged(List<StructBool> newValue);
  
    void setPropInt(List<StructInt> propInt);
    List<StructInt> getPropInt();
    void firePropIntChanged(List<StructInt> newValue);
  
    void setPropFloat(List<StructFloat> propFloat);
    List<StructFloat> getPropFloat();
    void firePropFloatChanged(List<StructFloat> newValue);
  
    void setPropString(List<StructString> propString);
    List<StructString> getPropString();
    void firePropStringChanged(List<StructString> newValue);
  
    void setPropEnum(List<Enum0> propEnum);
    List<Enum0> getPropEnum();
    void firePropEnumChanged(List<Enum0> newValue);
  
    // methods
    List<StructBool> funcBool(List<StructBool> paramBool);
    CompletableFuture<List<StructBool>> funcBoolAsync(List<StructBool> paramBool);
    List<StructInt> funcInt(List<StructInt> paramInt);
    CompletableFuture<List<StructInt>> funcIntAsync(List<StructInt> paramInt);
    List<StructFloat> funcFloat(List<StructFloat> paramFloat);
    CompletableFuture<List<StructFloat>> funcFloatAsync(List<StructFloat> paramFloat);
    List<StructString> funcString(List<StructString> paramString);
    CompletableFuture<List<StructString>> funcStringAsync(List<StructString> paramString);
    List<Enum0> funcEnum(List<Enum0> paramEnum);
    CompletableFuture<List<Enum0>> funcEnumAsync(List<Enum0> paramEnum);
    public void fireSigBool(List<StructBool> paramBool);
    public void fireSigInt(List<StructInt> paramInt);
    public void fireSigFloat(List<StructFloat> paramFloat);
    public void fireSigString(List<StructString> paramString);
    public void fireSigEnum(List<Enum0> paramEnum);
    boolean _isReady();
    void _shutdown();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(IStructArrayInterfaceEventListener listener);
    void removeEventListener(IStructArrayInterfaceEventListener listener);
  }
