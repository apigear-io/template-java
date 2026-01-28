package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructFloatWithArray;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

  public  class StructFloatWithArrayParcelable implements Parcelable {

    public StructFloatWithArray data;

    public StructFloatWithArrayParcelable(StructFloatWithArray data) {
        this.data = new StructFloatWithArray(data);
    }

    public StructFloatWithArray getStructFloatWithArray()
    {
        return new StructFloatWithArray(data);
    }

    protected StructFloatWithArrayParcelable(Parcel in) {
        this.data = new StructFloatWithArray();
        data.fieldFloat = in.createFloatArray();
    }

    public static final Creator<StructFloatWithArrayParcelable> CREATOR = new Creator<StructFloatWithArrayParcelable>() {
        @Override
        public StructFloatWithArrayParcelable createFromParcel(Parcel in) {
            return new StructFloatWithArrayParcelable(in);
        }

        @Override
        public StructFloatWithArrayParcelable[] newArray(int size) {
            return new StructFloatWithArrayParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloatArray(data.fieldFloat);


    }
        public static StructFloatWithArrayParcelable[] wrapArray(StructFloatWithArray[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(StructFloatWithArrayParcelable::new)
           .toArray(StructFloatWithArrayParcelable[]::new);
    }

    public static StructFloatWithArray[] unwrapArray(StructFloatWithArrayParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(StructFloatWithArrayParcelable::getStructFloatWithArray)
           .toArray(StructFloatWithArray[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
