package testbed2.testbed2_android_messenger;

import testbed2.testbed2_api.Struct4;
import android.os.Parcel;
import android.os.Parcelable;

  public  class Struct4Parcelable implements Parcelable {

    public Struct4 data;

    public Struct4Parcelable(Struct4 data) {
        this.data = new Struct4(data);
    }

    public Struct4 getStruct4()
    {
        return new Struct4(data);
    }

    protected Struct4Parcelable(Parcel in) {
    this.data = new Struct4();
        data.field1 = in.readInt();
        data.field2 = in.readInt();
        data.field3 = in.readInt();
        data.field4 = in.readInt();
    }

    public static final Creator<Struct4Parcelable> CREATOR = new Creator<Struct4Parcelable>() {
        @Override
        public Struct4Parcelable createFromParcel(Parcel in) {
            return new Struct4Parcelable(in);
        }

        @Override
        public Struct4Parcelable[] newArray(int size) {
            return new Struct4Parcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(data.field1);
        dest.writeInt(data.field2);
        dest.writeInt(data.field3);
        dest.writeInt(data.field4);


    }
        public static Struct4Parcelable[] wrapArray(Struct4[] structs) {
        if (structs == null) return null;
        Struct4Parcelable[] out = new Struct4Parcelable[structs.length];
        for (int i = 0; i < structs.length; i++) {
            out[i] = new Struct4Parcelable(structs[i]);
        }
        return out;
    }

    public static Struct4[] unwrapArray(Struct4Parcelable[] parcelables) {
        if (parcelables == null) return null;
        Struct4[] out = new Struct4[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getStruct4();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
