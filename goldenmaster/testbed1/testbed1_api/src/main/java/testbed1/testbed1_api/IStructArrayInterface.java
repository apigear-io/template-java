package testbed1.testbed1_api;

import testbed1.testbed1_api.IStructArrayInterfaceEventListener;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_api.StructString;

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
  
    // methods
    StructBool[] funcBool(StructBool[] paramBool);
    CompletableFuture<StructBool[]> funcBoolAsync(StructBool[] paramBool);
    StructInt[] funcInt(StructInt[] paramInt);
    CompletableFuture<StructInt[]> funcIntAsync(StructInt[] paramInt);
    StructFloat[] funcFloat(StructFloat[] paramFloat);
    CompletableFuture<StructFloat[]> funcFloatAsync(StructFloat[] paramFloat);
    StructString[] funcString(StructString[] paramString);
    CompletableFuture<StructString[]> funcStringAsync(StructString[] paramString);
    public void fireSigBool(StructBool[] paramBool);
    public void fireSigInt(StructInt[] paramInt);
    public void fireSigFloat(StructFloat[] paramFloat);
    public void fireSigString(StructString[] paramString);
    boolean _isReady();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(IStructArrayInterfaceEventListener listener);
    void removeEventListener(IStructArrayInterfaceEventListener listener);
  }
