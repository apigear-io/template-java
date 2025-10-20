package customTypes.customTypes_android_messenger;

import customTypes.customTypes_api.Vector3D;
import android.os.Parcel;
import android.os.Parcelable;

  public  class Vector3DParcelable implements Parcelable {

    public Vector3D data;

    public Vector3DParcelable(Vector3D data) {
        this.data = new Vector3D(data);
    }

    public Vector3D getVector3D()
    {
        return new Vector3D(data);
    }

    protected Vector3DParcelable(Parcel in) {
    this.data = new Vector3D();
        data.x = in.readFloat();
        data.y = in.readFloat();
        data.z = in.readFloat();
    }

    public static final Creator<Vector3DParcelable> CREATOR = new Creator<Vector3DParcelable>() {
        @Override
        public Vector3DParcelable createFromParcel(Parcel in) {
            return new Vector3DParcelable(in);
        }

        @Override
        public Vector3DParcelable[] newArray(int size) {
            return new Vector3DParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(data.x);
        dest.writeFloat(data.y);
        dest.writeFloat(data.z);


    }
        public static Vector3DParcelable[] wrapArray(Vector3D[] structs) {
        if (structs == null) return null;
        Vector3DParcelable[] out = new Vector3DParcelable[structs.length];
        for (int i = 0; i < structs.length; i++) {
            out[i] = new Vector3DParcelable(structs[i]);
        }
        return out;
    }

    public static Vector3D[] unwrapArray(Vector3DParcelable[] parcelables) {
        if (parcelables == null) return null;
        Vector3D[] out = new Vector3D[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getVector3D();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
