package externTypes.externTypes_android_messenger;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

  public  class MyVector3DParcelable implements Parcelable {
    public Vector3D data;

    public MyVector3DParcelable(Vector3D data) {
        // WARNING Copy if not simple type. Remember about nulls.
        this.data = data;
    }

    public Vector3D getMyVector3D()
    {
        // WARNING Copy if not simple type. Remember about nulls.
        return data;
    }

    protected MyVector3DParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

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
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }

    // WARNING Fill dest field by field with dest.write[TypedArray/Type/Parcelabe](data.field, flags);
    }

    // Helpers for arrays of this type
    public static MyVector3DParcelable[] wrapArray(Vector3D[] elements)
    {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(MyVector3DParcelable::new)
           .toArray(MyVector3DParcelable[]::new);
    }

    public static Vector3D[] unwrapArray(MyVector3DParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(MyVector3DParcelable::getMyVector3D)
           .toArray(Vector3D[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
