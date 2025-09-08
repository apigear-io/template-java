package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructString;
import android.os.Parcel;
import android.os.Parcelable;

  public  class StructStringParcelable implements Parcelable {

    public StructString data;

    public StructStringParcelable(StructString data) {
        this.data = new StructString(data);
    }

    public StructString getStructString()
    {
        return new StructString(data);
    }

    protected StructStringParcelable(Parcel in) {
    this.data = new StructString();
        data.fieldString = in.readString();
    }

    public static final Creator<StructStringParcelable> CREATOR = new Creator<StructStringParcelable>() {
        @Override
        public StructStringParcelable createFromParcel(Parcel in) {
            return new StructStringParcelable(in);
        }

        @Override
        public StructStringParcelable[] newArray(int size) {
            return new StructStringParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data.fieldString);


    }
        public static StructStringParcelable[] wrapArray(StructString[] structs) {
        if (structs == null) return null;
        StructStringParcelable[] out = new StructStringParcelable[structs.length];
        for (int i = 0; i < structs.length; i++) {
            out[i] = new StructStringParcelable(structs[i]);
        }
        return out;
    }

    public static StructString[] unwrapArray(StructStringParcelable[] parcelables) {
        if (parcelables == null) return null;
        StructString[] out = new StructString[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getStructString();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
