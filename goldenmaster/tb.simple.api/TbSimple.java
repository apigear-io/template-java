package tb.simple.api;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TbSimple {

  // enumerations

  // data structures

  // interfaces
  public static interface IVoidInterfaceEventListener {
    void onSigVoid();
  }

  public static interface IVoidInterface {
    // properties
    // methods
    void funcVoid();

    // signal listeners
    void addEventListener(IVoidInterfaceEventListener listener);
    void removeEventListener(IVoidInterfaceEventListener listener);
  }

  public static class AbstractVoidInterface implements IVoidInterface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireSigVoid() {
      for (ISigVoidEventListener listener : events) {
        listener.onSigVoid();
      }
    }
  
    
  }
  public static interface ISimpleInterfaceEventListener {
    void onPropBoolChanged(boolean oldValue, boolean newValue);
    void onPropIntChanged(int oldValue, int newValue);
    void onPropInt32Changed(int oldValue, int newValue);
    void onPropInt64Changed(long oldValue, long newValue);
    void onPropFloatChanged(float oldValue, float newValue);
    void onPropFloat32Changed(float oldValue, float newValue);
    void onPropFloat64Changed(double oldValue, double newValue);
    void onPropStringChanged(String oldValue, String newValue);
    void onSigBool(boolean paramBool);
    void onSigInt(int paramInt);
    void onSigInt32(int paramInt32);
    void onSigInt64(long paramInt64);
    void onSigFloat(float paramFloat);
    void onSigFloat32(float paramFloat32);
    void onSigFloat64(double paramFloat64);
    void onSigString(String paramString);
  }

  public static interface ISimpleInterface {
    // properties
    void setPropBool(boolean propBool);
    boolean getPropBool();
    void firePropBoolChanged(boolean oldValue, boolean newValue);
  
    void setPropInt(int propInt);
    int getPropInt();
    void firePropIntChanged(int oldValue, int newValue);
  
    void setPropInt32(int propInt32);
    int getPropInt32();
    void firePropInt32Changed(int oldValue, int newValue);
  
    void setPropInt64(long propInt64);
    long getPropInt64();
    void firePropInt64Changed(long oldValue, long newValue);
  
    void setPropFloat(float propFloat);
    float getPropFloat();
    void firePropFloatChanged(float oldValue, float newValue);
  
    void setPropFloat32(float propFloat32);
    float getPropFloat32();
    void firePropFloat32Changed(float oldValue, float newValue);
  
    void setPropFloat64(double propFloat64);
    double getPropFloat64();
    void firePropFloat64Changed(double oldValue, double newValue);
  
    void setPropString(String propString);
    String getPropString();
    void firePropStringChanged(String oldValue, String newValue);
  
    // methods
    void funcNoReturnValue(boolean paramBool);
    boolean funcBool(boolean paramBool);
    int funcInt(int paramInt);
    int funcInt32(int paramInt32);
    long funcInt64(long paramInt64);
    float funcFloat(float paramFloat);
    float funcFloat32(float paramFloat32);
    double funcFloat64(double paramFloat);
    String funcString(String paramString);

    // signal listeners
    void addEventListener(ISimpleInterfaceEventListener listener);
    void removeEventListener(ISimpleInterfaceEventListener listener);
  }

  public static class AbstractSimpleInterface implements ISimpleInterface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void firePropBoolChanged(boolean oldValue, boolean newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropBoolChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropIntChanged(int oldValue, int newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropIntChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropInt32Changed(int oldValue, int newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropInt32Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropInt64Changed(long oldValue, long newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropInt64Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropFloatChanged(float oldValue, float newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropFloatChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropFloat32Changed(float oldValue, float newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropFloat32Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropFloat64Changed(double oldValue, double newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropFloat64Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropStringChanged(String oldValue, String newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropStringChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void fireSigBool(boolean paramBool) {
      for (ISigBoolEventListener listener : events) {
        listener.onSigBool(paramBool);
      }
    }
  
    @Override
    public void fireSigInt(int paramInt) {
      for (ISigIntEventListener listener : events) {
        listener.onSigInt(paramInt);
      }
    }
  
    @Override
    public void fireSigInt32(int paramInt32) {
      for (ISigInt32EventListener listener : events) {
        listener.onSigInt32(paramInt32);
      }
    }
  
    @Override
    public void fireSigInt64(long paramInt64) {
      for (ISigInt64EventListener listener : events) {
        listener.onSigInt64(paramInt64);
      }
    }
  
    @Override
    public void fireSigFloat(float paramFloat) {
      for (ISigFloatEventListener listener : events) {
        listener.onSigFloat(paramFloat);
      }
    }
  
    @Override
    public void fireSigFloat32(float paramFloat32) {
      for (ISigFloat32EventListener listener : events) {
        listener.onSigFloat32(paramFloat32);
      }
    }
  
    @Override
    public void fireSigFloat64(double paramFloat64) {
      for (ISigFloat64EventListener listener : events) {
        listener.onSigFloat64(paramFloat64);
      }
    }
  
    @Override
    public void fireSigString(String paramString) {
      for (ISigStringEventListener listener : events) {
        listener.onSigString(paramString);
      }
    }
  
    
  }
  public static interface ISimpleArrayInterfaceEventListener {
    void onPropBoolChanged(boolean[] oldValue, boolean[] newValue);
    void onPropIntChanged(int[] oldValue, int[] newValue);
    void onPropInt32Changed(int[] oldValue, int[] newValue);
    void onPropInt64Changed(long[] oldValue, long[] newValue);
    void onPropFloatChanged(float[] oldValue, float[] newValue);
    void onPropFloat32Changed(float[] oldValue, float[] newValue);
    void onPropFloat64Changed(double[] oldValue, double[] newValue);
    void onPropStringChanged(String[] oldValue, String[] newValue);
    void onPropReadOnlyStringChanged(String oldValue, String newValue);
    void onSigBool(boolean[] paramBool);
    void onSigInt(int[] paramInt);
    void onSigInt32(int[] paramInt32);
    void onSigInt64(long[] paramInt64);
    void onSigFloat(float[] paramFloat);
    void onSigFloat32(float[] paramFloa32);
    void onSigFloat64(double[] paramFloat64);
    void onSigString(String[] paramString);
  }

  public static interface ISimpleArrayInterface {
    // properties
    void setPropBool(boolean[] propBool);
    boolean[] getPropBool();
    void firePropBoolChanged(boolean[] oldValue, boolean[] newValue);
  
    void setPropInt(int[] propInt);
    int[] getPropInt();
    void firePropIntChanged(int[] oldValue, int[] newValue);
  
    void setPropInt32(int[] propInt32);
    int[] getPropInt32();
    void firePropInt32Changed(int[] oldValue, int[] newValue);
  
    void setPropInt64(long[] propInt64);
    long[] getPropInt64();
    void firePropInt64Changed(long[] oldValue, long[] newValue);
  
    void setPropFloat(float[] propFloat);
    float[] getPropFloat();
    void firePropFloatChanged(float[] oldValue, float[] newValue);
  
    void setPropFloat32(float[] propFloat32);
    float[] getPropFloat32();
    void firePropFloat32Changed(float[] oldValue, float[] newValue);
  
    void setPropFloat64(double[] propFloat64);
    double[] getPropFloat64();
    void firePropFloat64Changed(double[] oldValue, double[] newValue);
  
    void setPropString(String[] propString);
    String[] getPropString();
    void firePropStringChanged(String[] oldValue, String[] newValue);
  
    void setPropReadOnlyString(String propReadOnlyString);
    String getPropReadOnlyString();
    void firePropReadOnlyStringChanged(String oldValue, String newValue);
  
    // methods
    boolean[] funcBool(boolean[] paramBool);
    int[] funcInt(int[] paramInt);
    int[] funcInt32(int[] paramInt32);
    long[] funcInt64(long[] paramInt64);
    float[] funcFloat(float[] paramFloat);
    float[] funcFloat32(float[] paramFloat32);
    double[] funcFloat64(double[] paramFloat);
    String[] funcString(String[] paramString);

    // signal listeners
    void addEventListener(ISimpleArrayInterfaceEventListener listener);
    void removeEventListener(ISimpleArrayInterfaceEventListener listener);
  }

  public static class AbstractSimpleArrayInterface implements ISimpleArrayInterface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void firePropBoolChanged(boolean[] oldValue, boolean[] newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropBoolChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropIntChanged(int[] oldValue, int[] newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropIntChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropInt32Changed(int[] oldValue, int[] newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropInt32Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropInt64Changed(long[] oldValue, long[] newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropInt64Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropFloatChanged(float[] oldValue, float[] newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropFloatChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropFloat32Changed(float[] oldValue, float[] newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropFloat32Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropFloat64Changed(double[] oldValue, double[] newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropFloat64Changed(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropStringChanged(String[] oldValue, String[] newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropStringChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropReadOnlyStringChanged(String oldValue, String newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropReadOnlyStringChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void fireSigBool(boolean[] paramBool) {
      for (ISigBoolEventListener listener : events) {
        listener.onSigBool(paramBool);
      }
    }
  
    @Override
    public void fireSigInt(int[] paramInt) {
      for (ISigIntEventListener listener : events) {
        listener.onSigInt(paramInt);
      }
    }
  
    @Override
    public void fireSigInt32(int[] paramInt32) {
      for (ISigInt32EventListener listener : events) {
        listener.onSigInt32(paramInt32);
      }
    }
  
    @Override
    public void fireSigInt64(long[] paramInt64) {
      for (ISigInt64EventListener listener : events) {
        listener.onSigInt64(paramInt64);
      }
    }
  
    @Override
    public void fireSigFloat(float[] paramFloat) {
      for (ISigFloatEventListener listener : events) {
        listener.onSigFloat(paramFloat);
      }
    }
  
    @Override
    public void fireSigFloat32(float[] paramFloa32) {
      for (ISigFloat32EventListener listener : events) {
        listener.onSigFloat32(paramFloa32);
      }
    }
  
    @Override
    public void fireSigFloat64(double[] paramFloat64) {
      for (ISigFloat64EventListener listener : events) {
        listener.onSigFloat64(paramFloat64);
      }
    }
  
    @Override
    public void fireSigString(String[] paramString) {
      for (ISigStringEventListener listener : events) {
        listener.onSigString(paramString);
      }
    }
  
    
  }
  public static interface INoPropertiesInterfaceEventListener {
    void onSigVoid();
    void onSigBool(boolean paramBool);
  }

  public static interface INoPropertiesInterface {
    // properties
    // methods
    void funcVoid();
    boolean funcBool(boolean paramBool);

    // signal listeners
    void addEventListener(INoPropertiesInterfaceEventListener listener);
    void removeEventListener(INoPropertiesInterfaceEventListener listener);
  }

  public static class AbstractNoPropertiesInterface implements INoPropertiesInterface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireSigVoid() {
      for (ISigVoidEventListener listener : events) {
        listener.onSigVoid();
      }
    }
  
    @Override
    public void fireSigBool(boolean paramBool) {
      for (ISigBoolEventListener listener : events) {
        listener.onSigBool(paramBool);
      }
    }
  
    
  }
  public static interface INoOperationsInterfaceEventListener {
    void onPropBoolChanged(boolean oldValue, boolean newValue);
    void onPropIntChanged(int oldValue, int newValue);
    void onSigVoid();
    void onSigBool(boolean paramBool);
  }

  public static interface INoOperationsInterface {
    // properties
    void setPropBool(boolean propBool);
    boolean getPropBool();
    void firePropBoolChanged(boolean oldValue, boolean newValue);
  
    void setPropInt(int propInt);
    int getPropInt();
    void firePropIntChanged(int oldValue, int newValue);
  
    // methods

    // signal listeners
    void addEventListener(INoOperationsInterfaceEventListener listener);
    void removeEventListener(INoOperationsInterfaceEventListener listener);
  }

  public static class AbstractNoOperationsInterface implements INoOperationsInterface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void firePropBoolChanged(boolean oldValue, boolean newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropBoolChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropIntChanged(int oldValue, int newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropIntChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void fireSigVoid() {
      for (ISigVoidEventListener listener : events) {
        listener.onSigVoid();
      }
    }
  
    @Override
    public void fireSigBool(boolean paramBool) {
      for (ISigBoolEventListener listener : events) {
        listener.onSigBool(paramBool);
      }
    }
  
    
  }
  public static interface INoSignalsInterfaceEventListener {
    void onPropBoolChanged(boolean oldValue, boolean newValue);
    void onPropIntChanged(int oldValue, int newValue);
  }

  public static interface INoSignalsInterface {
    // properties
    void setPropBool(boolean propBool);
    boolean getPropBool();
    void firePropBoolChanged(boolean oldValue, boolean newValue);
  
    void setPropInt(int propInt);
    int getPropInt();
    void firePropIntChanged(int oldValue, int newValue);
  
    // methods
    void funcVoid();
    boolean funcBool(boolean paramBool);

    // signal listeners
    void addEventListener(INoSignalsInterfaceEventListener listener);
    void removeEventListener(INoSignalsInterfaceEventListener listener);
  }

  public static class AbstractNoSignalsInterface implements INoSignalsInterface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void firePropBoolChanged(boolean oldValue, boolean newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropBoolChanged(oldValue, newValue);
      }
    }
  
    @Override
    public void firePropIntChanged(int oldValue, int newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.onPropIntChanged(oldValue, newValue);
      }
    }
  
    
  }
  public static interface IEmptyInterfaceEventListener {
  }

  public static interface IEmptyInterface {
    // properties
    // methods

    // signal listeners
    void addEventListener(IEmptyInterfaceEventListener listener);
    void removeEventListener(IEmptyInterfaceEventListener listener);
  }

  public static class AbstractEmptyInterface implements IEmptyInterface {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
    
  }
}