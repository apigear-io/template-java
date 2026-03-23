package tbSimple.tbSimple_api;

import tbSimple.tbSimple_api.ISimpleArrayInterfaceEventListener;

import java.util.concurrent.CompletableFuture;


  public interface ISimpleArrayInterface {
    // properties
    void setPropBool(boolean[] propBool);
    boolean[] getPropBool();
    void firePropBoolChanged(boolean[] newValue);
  
    void setPropInt(int[] propInt);
    int[] getPropInt();
    void firePropIntChanged(int[] newValue);
  
    void setPropInt32(int[] propInt32);
    int[] getPropInt32();
    void firePropInt32Changed(int[] newValue);
  
    void setPropInt64(long[] propInt64);
    long[] getPropInt64();
    void firePropInt64Changed(long[] newValue);
  
    void setPropFloat(float[] propFloat);
    float[] getPropFloat();
    void firePropFloatChanged(float[] newValue);
  
    void setPropFloat32(float[] propFloat32);
    float[] getPropFloat32();
    void firePropFloat32Changed(float[] newValue);
  
    void setPropFloat64(double[] propFloat64);
    double[] getPropFloat64();
    void firePropFloat64Changed(double[] newValue);
  
    void setPropString(String[] propString);
    String[] getPropString();
    void firePropStringChanged(String[] newValue);
  
    void setPropReadOnlyString(String propReadOnlyString);
    String getPropReadOnlyString();
    void firePropReadOnlyStringChanged(String newValue);
  
    // methods
    boolean[] funcBool(boolean[] paramBool);
    CompletableFuture<boolean[]> funcBoolAsync(boolean[] paramBool);
    int[] funcInt(int[] paramInt);
    CompletableFuture<int[]> funcIntAsync(int[] paramInt);
    int[] funcInt32(int[] paramInt32);
    CompletableFuture<int[]> funcInt32Async(int[] paramInt32);
    long[] funcInt64(long[] paramInt64);
    CompletableFuture<long[]> funcInt64Async(long[] paramInt64);
    float[] funcFloat(float[] paramFloat);
    CompletableFuture<float[]> funcFloatAsync(float[] paramFloat);
    float[] funcFloat32(float[] paramFloat32);
    CompletableFuture<float[]> funcFloat32Async(float[] paramFloat32);
    double[] funcFloat64(double[] paramFloat);
    CompletableFuture<double[]> funcFloat64Async(double[] paramFloat);
    String[] funcString(String[] paramString);
    CompletableFuture<String[]> funcStringAsync(String[] paramString);
    public void fireSigBool(boolean[] paramBool);
    public void fireSigInt(int[] paramInt);
    public void fireSigInt32(int[] paramInt32);
    public void fireSigInt64(long[] paramInt64);
    public void fireSigFloat(float[] paramFloat);
    public void fireSigFloat32(float[] paramFloa32);
    public void fireSigFloat64(double[] paramFloat64);
    public void fireSigString(String[] paramString);
    boolean _isReady();
    void _shutdown();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(ISimpleArrayInterfaceEventListener listener);
    void removeEventListener(ISimpleArrayInterfaceEventListener listener);
  }
