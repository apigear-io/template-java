package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructInt;
import android.os.Parcel;
import android.os.Parcelable;

  public  class StructIntParcelable implements Parcelable {

    public StructInt data;

    public StructIntParcelable(StructInt data) {
        this.data = new StructInt(data);
    }

    public StructInt getStructInt()
    {
        return new StructInt(data);
    }

    protected StructIntParcelable(Parcel in) {
    this.data = new StructInt();
        data.fieldInt = in.readInt();
    }

    public static final Creator<StructIntParcelable> CREATOR = new Creator<StructIntParcelable>() {
        @Override
        public StructIntParcelable createFromParcel(Parcel in) {
            return new StructIntParcelable(in);
        }

        @Override
        public StructIntParcelable[] newArray(int size) {
            return new StructIntParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(data.fieldInt);


    }
        public static StructIntParcelable[] wrapArray(StructInt[] structs) {
        if (structs == null) return null;
        StructIntParcelable[] out = new StructIntParcelable[structs.length];
        for (int i = 0; i < structs.length; i++) {
            out[i] = new StructIntParcelable(structs[i]);
        }
        return out;
    }

    public static StructInt[] unwrapArray(StructIntParcelable[] parcelables) {
        if (parcelables == null) return null;
        StructInt[] out = new StructInt[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getStructInt();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
