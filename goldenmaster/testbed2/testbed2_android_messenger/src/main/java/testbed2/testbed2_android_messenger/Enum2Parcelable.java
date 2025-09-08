package testbed2.testbed2_android_messenger;

import testbed2.testbed2_api.Enum2;
import android.os.Parcel;
import android.os.Parcelable;



//TODO imports - may need some struct from this or imported module

  public  class Enum2Parcelable implements Parcelable {

    public Enum2 data;

    public Enum2Parcelable(Enum2 data) {
        this.data = data;
    }

    public Enum2 getEnum2()
    {
        return data;
    }

    protected Enum2Parcelable(Parcel in) {
        int intValue = in.readInt();
        this.data = Enum2.fromValue(intValue);
    }

    public static final Creator<Enum2Parcelable> CREATOR = new Creator<Enum2Parcelable>() {
        @Override
        public Enum2Parcelable createFromParcel(Parcel in) {
            return new Enum2Parcelable(in);
        }

        @Override
        public Enum2Parcelable[] newArray(int size) {
            return new Enum2Parcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(data.getValue());
    }

    public static Enum2Parcelable[] wrapArray(Enum2[] enums) {
        if (enums == null) return null;
        Enum2Parcelable[] result = new Enum2Parcelable[enums.length];
        for (int i = 0; i < enums.length; i++) {
            result[i] = new Enum2Parcelable(enums[i]);
        }
        return result;
    }

    public static Enum2[] unwrapArray(Enum2Parcelable[] parcelables) {
        if (parcelables == null) return null;
        Enum2[] out = new Enum2[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getEnum2();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
