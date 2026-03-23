package counter.counter_api;

import counter.counter_api.ICounterEventListener;

import java.util.concurrent.CompletableFuture;


  public interface ICounter {
    // properties
    void setVector(customTypes.customTypes_api.Vector3D vector);
    customTypes.customTypes_api.Vector3D getVector();
    void fireVectorChanged(customTypes.customTypes_api.Vector3D newValue);
  
    void setExternVector(org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector);
    org.apache.commons.math3.geometry.euclidean.threed.Vector3D getExternVector();
    void fireExternVectorChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D newValue);
  
    void setVectorArray(customTypes.customTypes_api.Vector3D[] vectorArray);
    customTypes.customTypes_api.Vector3D[] getVectorArray();
    void fireVectorArrayChanged(customTypes.customTypes_api.Vector3D[] newValue);
  
    void setExternVectorArray(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray);
    org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] getExternVectorArray();
    void fireExternVectorArrayChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] newValue);
  
    // methods
    org.apache.commons.math3.geometry.euclidean.threed.Vector3D increment(org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec);
    CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> incrementAsync(org.apache.commons.math3.geometry.euclidean.threed.Vector3D vec);
    org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] incrementArray(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec);
    CompletableFuture<org.apache.commons.math3.geometry.euclidean.threed.Vector3D[]> incrementArrayAsync(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] vec);
    customTypes.customTypes_api.Vector3D decrement(customTypes.customTypes_api.Vector3D vec);
    CompletableFuture<customTypes.customTypes_api.Vector3D> decrementAsync(customTypes.customTypes_api.Vector3D vec);
    customTypes.customTypes_api.Vector3D[] decrementArray(customTypes.customTypes_api.Vector3D[] vec);
    CompletableFuture<customTypes.customTypes_api.Vector3D[]> decrementArrayAsync(customTypes.customTypes_api.Vector3D[] vec);
    public void fireValueChanged(customTypes.customTypes_api.Vector3D vector, org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector, customTypes.customTypes_api.Vector3D[] vectorArray, org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray);
    boolean _isReady();
    void _shutdown();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(ICounterEventListener listener);
    void removeEventListener(ICounterEventListener listener);
  }
