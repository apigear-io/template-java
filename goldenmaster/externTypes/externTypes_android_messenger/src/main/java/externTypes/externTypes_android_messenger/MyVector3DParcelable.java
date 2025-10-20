package externTypes.externTypes_android_messenger;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import android.os.Parcel;
import android.os.Parcelable;


  public  class MyVector3DParcelable implements Parcelable {
    public Vector3D data;

    public MyVector3DParcelable(Vector3D data) {
        // WARNING Copy if not simple type
        this.data = data;
    }

    public Vector3D getMyVector3D()
    {
        // WARNING Copy if not simple type.
        return data;
    }

    protected MyVector3DParcelable(Parcel in) {
        //WARNING Fill the data field by field with in.createTypedArray, in. read[dataType] or in.readParcelable, depending on type.
    }

    public static final Creator<MyVector3DParcelable> CREATOR = new Creator<MyVector3DParcelable>() {
        @Override
        public MyVector3DParcelable createFromParcel(Parcel in) {
            return new MyVector3DParcelable(in);
        }

        @Override
        public MyVector3DParcelable[] newArray(int size) {
            return new MyVector3DParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    // WARNING Fill dest field by field with dest.write[TypedArray/Type/Parcelabe](data.field, flags);
    }

    // Helpers for arrays of this type
    public static MyVector3DParcelable[] wrapArray(Vector3D[] elements)
    {
        if (elements == null) return null;
        MyVector3DParcelable[] out = new MyVector3DParcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new MyVector3DParcelable(elements[i]);
        }
        return out;
    }

    public static Vector3D[] unwrapArray(MyVector3DParcelable[] parcelables) {
        if (parcelables == null) return null;
        Vector3D[] out = new Vector3D[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getMyVector3D();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
