package tbSame2.tbSame2_android_messenger;

import tbSame2.tbSame2_api.Struct1;
import android.os.Parcel;
import android.os.Parcelable;

  public  class Struct1Parcelable implements Parcelable {

    public Struct1 data;

    public Struct1Parcelable(Struct1 data) {
        this.data = new Struct1(data);
    }

    public Struct1 getStruct1()
    {
        return new Struct1(data);
    }

    protected Struct1Parcelable(Parcel in) {
    this.data = new Struct1();
        data.field1 = in.readInt();
        data.field2 = in.readInt();
        data.field3 = in.readInt();
    }

    public static final Creator<Struct1Parcelable> CREATOR = new Creator<Struct1Parcelable>() {
        @Override
        public Struct1Parcelable createFromParcel(Parcel in) {
            return new Struct1Parcelable(in);
        }

        @Override
        public Struct1Parcelable[] newArray(int size) {
            return new Struct1Parcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(data.field1);
        dest.writeInt(data.field2);
        dest.writeInt(data.field3);


    }
        public static Struct1Parcelable[] wrapArray(Struct1[] structs) {
        if (structs == null) return null;
        Struct1Parcelable[] out = new Struct1Parcelable[structs.length];
        for (int i = 0; i < structs.length; i++) {
            out[i] = new Struct1Parcelable(structs[i]);
        }
        return out;
    }

    public static Struct1[] unwrapArray(Struct1Parcelable[] parcelables) {
        if (parcelables == null) return null;
        Struct1[] out = new Struct1[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getStruct1();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
