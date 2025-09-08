package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructFloat;
import android.os.Parcel;
import android.os.Parcelable;

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
        StructFloatParcelable[] out = new StructFloatParcelable[structs.length];
        for (int i = 0; i < structs.length; i++) {
            out[i] = new StructFloatParcelable(structs[i]);
        }
        return out;
    }

    public static StructFloat[] unwrapArray(StructFloatParcelable[] parcelables) {
        if (parcelables == null) return null;
        StructFloat[] out = new StructFloat[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getStructFloat();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
