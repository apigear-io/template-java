package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructStructWithArray;
import android.os.Parcel;
import android.os.Parcelable;
import testbed1.testbed1_api.StructStringWithArray;

  public  class StructStructWithArrayParcelable implements Parcelable {

    public StructStructWithArray data;

    public StructStructWithArrayParcelable(StructStructWithArray data) {
        this.data = new StructStructWithArray(data);
    }

    public StructStructWithArray getStructStructWithArray()
    {
        return new StructStructWithArray(data);
    }

    protected StructStructWithArrayParcelable(Parcel in) {
    this.data = new StructStructWithArray();
        StructStringWithArrayParcelable[] l_parcelablefieldStruct = in.createTypedArray(StructStringWithArrayParcelable.CREATOR);
        data.fieldStruct = StructStringWithArrayParcelable.unwrapArray(l_parcelablefieldStruct);
    }

    public static final Creator<StructStructWithArrayParcelable> CREATOR = new Creator<StructStructWithArrayParcelable>() {
        @Override
        public StructStructWithArrayParcelable createFromParcel(Parcel in) {
            return new StructStructWithArrayParcelable(in);
        }

        @Override
        public StructStructWithArrayParcelable[] newArray(int size) {
            return new StructStructWithArrayParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(StructStringWithArrayParcelable.wrapArray(data.fieldStruct), flags);


    }
        public static StructStructWithArrayParcelable[] wrapArray(StructStructWithArray[] structs) {
        if (structs == null) return null;
        StructStructWithArrayParcelable[] out = new StructStructWithArrayParcelable[structs.length];
        for (int i = 0; i < structs.length; i++) {
            out[i] = new StructStructWithArrayParcelable(structs[i]);
        }
        return out;
    }

    public static StructStructWithArray[] unwrapArray(StructStructWithArrayParcelable[] parcelables) {
        if (parcelables == null) return null;
        StructStructWithArray[] out = new StructStructWithArray[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getStructStructWithArray();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
