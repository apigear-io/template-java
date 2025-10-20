package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructStringWithArray;
import android.os.Parcel;
import android.os.Parcelable;

  public  class StructStringWithArrayParcelable implements Parcelable {

    public StructStringWithArray data;

    public StructStringWithArrayParcelable(StructStringWithArray data) {
        this.data = new StructStringWithArray(data);
    }

    public StructStringWithArray getStructStringWithArray()
    {
        return new StructStringWithArray(data);
    }

    protected StructStringWithArrayParcelable(Parcel in) {
    this.data = new StructStringWithArray();
        data.fieldString = in.createStringArray();
    }

    public static final Creator<StructStringWithArrayParcelable> CREATOR = new Creator<StructStringWithArrayParcelable>() {
        @Override
        public StructStringWithArrayParcelable createFromParcel(Parcel in) {
            return new StructStringWithArrayParcelable(in);
        }

        @Override
        public StructStringWithArrayParcelable[] newArray(int size) {
            return new StructStringWithArrayParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(data.fieldString);


    }
        public static StructStringWithArrayParcelable[] wrapArray(StructStringWithArray[] structs) {
        if (structs == null) return null;
        StructStringWithArrayParcelable[] out = new StructStringWithArrayParcelable[structs.length];
        for (int i = 0; i < structs.length; i++) {
            out[i] = new StructStringWithArrayParcelable(structs[i]);
        }
        return out;
    }

    public static StructStringWithArray[] unwrapArray(StructStringWithArrayParcelable[] parcelables) {
        if (parcelables == null) return null;
        StructStringWithArray[] out = new StructStringWithArray[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getStructStringWithArray();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
