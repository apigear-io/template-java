package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructBoolWithArray;
import android.os.Parcel;
import android.os.Parcelable;

  public  class StructBoolWithArrayParcelable implements Parcelable {

    public StructBoolWithArray data;

    public StructBoolWithArrayParcelable(StructBoolWithArray data) {
        this.data = new StructBoolWithArray(data);
    }

    public StructBoolWithArray getStructBoolWithArray()
    {
        return new StructBoolWithArray(data);
    }

    protected StructBoolWithArrayParcelable(Parcel in) {
    this.data = new StructBoolWithArray();
        data.fieldBool = in.createBooleanArray();
    }

    public static final Creator<StructBoolWithArrayParcelable> CREATOR = new Creator<StructBoolWithArrayParcelable>() {
        @Override
        public StructBoolWithArrayParcelable createFromParcel(Parcel in) {
            return new StructBoolWithArrayParcelable(in);
        }

        @Override
        public StructBoolWithArrayParcelable[] newArray(int size) {
            return new StructBoolWithArrayParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBooleanArray(data.fieldBool);


    }
        public static StructBoolWithArrayParcelable[] wrapArray(StructBoolWithArray[] structs) {
        if (structs == null) return null;
        StructBoolWithArrayParcelable[] out = new StructBoolWithArrayParcelable[structs.length];
        for (int i = 0; i < structs.length; i++) {
            out[i] = new StructBoolWithArrayParcelable(structs[i]);
        }
        return out;
    }

    public static StructBoolWithArray[] unwrapArray(StructBoolWithArrayParcelable[] parcelables) {
        if (parcelables == null) return null;
        StructBoolWithArray[] out = new StructBoolWithArray[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getStructBoolWithArray();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
