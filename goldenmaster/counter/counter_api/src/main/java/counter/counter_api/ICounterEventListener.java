package counter.counter_api;

  public interface ICounterEventListener {
    void onVectorChanged(customTypes.customTypes_api.Vector3D newValue);
    void onExternVectorChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D newValue);
    void onVectorArrayChanged(customTypes.customTypes_api.Vector3D[] newValue);
    void onExternVectorArrayChanged(org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] newValue);
    void onValueChanged(customTypes.customTypes_api.Vector3D vector, org.apache.commons.math3.geometry.euclidean.threed.Vector3D extern_vector, customTypes.customTypes_api.Vector3D[] vectorArray, org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] extern_vectorArray);
  void on_readyStatusChanged(boolean isReady);
  }
