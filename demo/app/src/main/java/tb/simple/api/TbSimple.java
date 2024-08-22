package tb.simple.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.EventListener;


public class TbSimple {

  // enumerations

  // data structures

  // interfaces
  public static interface IVoidInterfaceEventListener extends EventListener {
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
  public static interface ISimpleInterfaceEventListener extends EventListener {
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
  public static interface ISimpleArrayInterfaceEventListener extends EventListener {
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
  public static interface INoPropertiesInterfaceEventListener extends EventListener {
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
  public static interface INoOperationsInterfaceEventListener extends EventListener {
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
  public static interface INoSignalsInterfaceEventListener extends EventListener {
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
  public static interface IEmptyInterfaceEventListener extends EventListener {
  }

  public static interface IEmptyInterface {
    // properties
    // methods

    // signal listeners
    void addEventListener(IEmptyInterfaceEventListener listener);
    void removeEventListener(IEmptyInterfaceEventListener listener);
  }

}