package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructEnumWithArray;
import android.os.Parcel;
import android.os.Parcelable;
import testbed1.testbed1_api.Enum0;

  public  class StructEnumWithArrayParcelable implements Parcelable {

    public StructEnumWithArray data;

    public StructEnumWithArrayParcelable(StructEnumWithArray data) {
        this.data = new StructEnumWithArray(data);
    }

    public StructEnumWithArray getStructEnumWithArray()
    {
        return new StructEnumWithArray(data);
    }

    protected StructEnumWithArrayParcelable(Parcel in) {
    this.data = new StructEnumWithArray();
        Enum0Parcelable[] l_parcelablefieldEnum = in.createTypedArray(Enum0Parcelable.CREATOR);
        data.fieldEnum = Enum0Parcelable.unwrapArray(l_parcelablefieldEnum);
    }

    public static final Creator<StructEnumWithArrayParcelable> CREATOR = new Creator<StructEnumWithArrayParcelable>() {
        @Override
        public StructEnumWithArrayParcelable createFromParcel(Parcel in) {
            return new StructEnumWithArrayParcelable(in);
        }

        @Override
        public StructEnumWithArrayParcelable[] newArray(int size) {
            return new StructEnumWithArrayParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(Enum0Parcelable.wrapArray(data.fieldEnum), flags);


    }
        public static StructEnumWithArrayParcelable[] wrapArray(StructEnumWithArray[] structs) {
        if (structs == null) return null;
        StructEnumWithArrayParcelable[] out = new StructEnumWithArrayParcelable[structs.length];
        for (int i = 0; i < structs.length; i++) {
            out[i] = new StructEnumWithArrayParcelable(structs[i]);
        }
        return out;
    }

    public static StructEnumWithArray[] unwrapArray(StructEnumWithArrayParcelable[] parcelables) {
        if (parcelables == null) return null;
        StructEnumWithArray[] out = new StructEnumWithArray[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getStructEnumWithArray();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
