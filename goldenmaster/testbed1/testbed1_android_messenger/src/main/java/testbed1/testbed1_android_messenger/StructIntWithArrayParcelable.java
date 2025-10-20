package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructIntWithArray;
import android.os.Parcel;
import android.os.Parcelable;

  public  class StructIntWithArrayParcelable implements Parcelable {

    public StructIntWithArray data;

    public StructIntWithArrayParcelable(StructIntWithArray data) {
        this.data = new StructIntWithArray(data);
    }

    public StructIntWithArray getStructIntWithArray()
    {
        return new StructIntWithArray(data);
    }

    protected StructIntWithArrayParcelable(Parcel in) {
    this.data = new StructIntWithArray();
        data.fieldInt = in.createIntArray();
    }

    public static final Creator<StructIntWithArrayParcelable> CREATOR = new Creator<StructIntWithArrayParcelable>() {
        @Override
        public StructIntWithArrayParcelable createFromParcel(Parcel in) {
            return new StructIntWithArrayParcelable(in);
        }

        @Override
        public StructIntWithArrayParcelable[] newArray(int size) {
            return new StructIntWithArrayParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(data.fieldInt);


    }
        public static StructIntWithArrayParcelable[] wrapArray(StructIntWithArray[] structs) {
        if (structs == null) return null;
        StructIntWithArrayParcelable[] out = new StructIntWithArrayParcelable[structs.length];
        for (int i = 0; i < structs.length; i++) {
            out[i] = new StructIntWithArrayParcelable(structs[i]);
        }
        return out;
    }

    public static StructIntWithArray[] unwrapArray(StructIntWithArrayParcelable[] parcelables) {
        if (parcelables == null) return null;
        StructIntWithArray[] out = new StructIntWithArray[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getStructIntWithArray();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
