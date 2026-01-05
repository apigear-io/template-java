package counter.counter_api;

import counter.counter_api.ICounterEventListener;
import counter.counter_api.ICounter;

import java.util.Collection;
import java.util.HashSet;
  public abstract class AbstractCounter implements ICounter {
    public Collection<ICounterEventListener> listeners = new HashSet<>();

    @Override
    public void addEventListener(ICounterEventListener listener) {
      listeners.add(listener); 
    }
    @Override
    public void removeEventListener(ICounterEventListener listener) {
      listeners.remove(listener);
    }
    @Override
    public void fireVectorChanged(customTypes.customTypes_api.Vector3D newValue) {
      for (ICounterEventListener listener : listeners) {
        listener.onVectorChanged(newValue);
      }
    }
  
    @Override
    public void fireExternVectorChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D newValue) {
      for (ICounterEventListener listener : listeners) {
        listener.onExternVectorChanged(newValue);
      }
    }
  
    @Override
    public void fireVectorArrayChanged(customTypes.customTypes_api.Vector3D[] newValue) {
      for (ICounterEventListener listener : listeners) {
        listener.onVectorArrayChanged(newValue);
      }
    }
  
    @Override
    public void fireExternVectorArrayChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] newValue) {
      for (ICounterEventListener listener : listeners) {
        listener.onExternVectorArrayChanged(newValue);
      }
    }
  
    @Override
    public void fireValueChanged(customTypes.customTypes_api.Vector3D vector, org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector, customTypes.customTypes_api.Vector3D[] vectorArray, org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray) {
      for (ICounterEventListener listener : listeners) {
        listener.onValueChanged(vector, extern_vector, vectorArray, extern_vectorArray);
      }
    }
  
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for (ICounterEventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
