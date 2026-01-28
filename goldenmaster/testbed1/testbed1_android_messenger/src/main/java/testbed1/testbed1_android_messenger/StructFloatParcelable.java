package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructFloat;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

  public  class StructFloatParcelable implements Parcelable {

    public StructFloat data;

    public StructFloatParcelable(StructFloat data) {
        this.data = new StructFloat(data);
    }

    public StructFloat getStructFloat()
    {
        return new StructFloat(data);
    }

    protected StructFloatParcelable(Parcel in) {
        this.data = new StructFloat();
        data.fieldFloat = in.readFloat();
    }

    public static final Creator<StructFloatParcelable> CREATOR = new Creator<StructFloatParcelable>() {
        @Override
        public StructFloatParcelable createFromParcel(Parcel in) {
            return new StructFloatParcelable(in);
        }

        @Override
        public StructFloatParcelable[] newArray(int size) {
            return new StructFloatParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(data.fieldFloat);


    }
        public static StructFloatParcelable[] wrapArray(StructFloat[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(StructFloatParcelable::new)
           .toArray(StructFloatParcelable[]::new);
    }

    public static StructFloat[] unwrapArray(StructFloatParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(StructFloatParcelable::getStructFloat)
           .toArray(StructFloat[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
