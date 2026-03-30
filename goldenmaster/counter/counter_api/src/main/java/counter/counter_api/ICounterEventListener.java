package counter.counter_api;

import java.util.List;

  public interface ICounterEventListener {
    void onVectorChanged(customTypes.customTypes_api.Vector3D newValue);
    void onExternVectorChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D newValue);
    void onVectorArrayChanged(List<customTypes.customTypes_api.Vector3D> newValue);
    void onExternVectorArrayChanged(List<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> newValue);
    void onValueChanged(customTypes.customTypes_api.Vector3D vector, org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector, List<customTypes.customTypes_api.Vector3D> vectorArray, List<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> extern_vectorArray);
  void on_readyStatusChanged(boolean isReady);
  }
