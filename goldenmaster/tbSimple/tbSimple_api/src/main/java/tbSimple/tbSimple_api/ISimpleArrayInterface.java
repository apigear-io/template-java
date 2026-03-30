package tbSimple.tbSimple_api;

import tbSimple.tbSimple_api.ISimpleArrayInterfaceEventListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;


  public interface ISimpleArrayInterface {
    // properties
    void setPropBool(List<Boolean> propBool);
    List<Boolean> getPropBool();
    void firePropBoolChanged(List<Boolean> newValue);
  
    void setPropInt(List<Integer> propInt);
    List<Integer> getPropInt();
    void firePropIntChanged(List<Integer> newValue);
  
    void setPropInt32(List<Integer> propInt32);
    List<Integer> getPropInt32();
    void firePropInt32Changed(List<Integer> newValue);
  
    void setPropInt64(List<Long> propInt64);
    List<Long> getPropInt64();
    void firePropInt64Changed(List<Long> newValue);
  
    void setPropFloat(List<Float> propFloat);
    List<Float> getPropFloat();
    void firePropFloatChanged(List<Float> newValue);
  
    void setPropFloat32(List<Float> propFloat32);
    List<Float> getPropFloat32();
    void firePropFloat32Changed(List<Float> newValue);
  
    void setPropFloat64(List<Double> propFloat64);
    List<Double> getPropFloat64();
    void firePropFloat64Changed(List<Double> newValue);
  
    void setPropString(List<String> propString);
    List<String> getPropString();
    void firePropStringChanged(List<String> newValue);
  
    void setPropReadOnlyString(String propReadOnlyString);
    String getPropReadOnlyString();
    void firePropReadOnlyStringChanged(String newValue);
  
    // methods
    List<Boolean> funcBool(List<Boolean> paramBool);
    CompletableFuture<List<Boolean>> funcBoolAsync(List<Boolean> paramBool);
    List<Integer> funcInt(List<Integer> paramInt);
    CompletableFuture<List<Integer>> funcIntAsync(List<Integer> paramInt);
    List<Integer> funcInt32(List<Integer> paramInt32);
    CompletableFuture<List<Integer>> funcInt32Async(List<Integer> paramInt32);
    List<Long> funcInt64(List<Long> paramInt64);
    CompletableFuture<List<Long>> funcInt64Async(List<Long> paramInt64);
    List<Float> funcFloat(List<Float> paramFloat);
    CompletableFuture<List<Float>> funcFloatAsync(List<Float> paramFloat);
    List<Float> funcFloat32(List<Float> paramFloat32);
    CompletableFuture<List<Float>> funcFloat32Async(List<Float> paramFloat32);
    List<Double> funcFloat64(List<Double> paramFloat);
    CompletableFuture<List<Double>> funcFloat64Async(List<Double> paramFloat);
    List<String> funcString(List<String> paramString);
    CompletableFuture<List<String>> funcStringAsync(List<String> paramString);
    public void fireSigBool(List<Boolean> paramBool);
    public void fireSigInt(List<Integer> paramInt);
    public void fireSigInt32(List<Integer> paramInt32);
    public void fireSigInt64(List<Long> paramInt64);
    public void fireSigFloat(List<Float> paramFloat);
    public void fireSigFloat32(List<Float> paramFloa32);
    public void fireSigFloat64(List<Double> paramFloat64);
    public void fireSigString(List<String> paramString);
    boolean _isReady();
    void _shutdown();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(ISimpleArrayInterfaceEventListener listener);
    void removeEventListener(ISimpleArrayInterfaceEventListener listener);
  }
