package tbSame2.tbSame2_android_messenger;

import tbSame2.tbSame2_api.Enum1;
import android.os.Parcel;
import android.os.Parcelable;



//TODO imports - may need some struct from this or imported module

  public  class Enum1Parcelable implements Parcelable {

    public Enum1 data;

    public Enum1Parcelable(Enum1 data) {
        this.data = data;
    }

    public Enum1 getEnum1()
    {
        return data;
    }

    protected Enum1Parcelable(Parcel in) {
        int intValue = in.readInt();
        this.data = Enum1.fromValue(intValue);
    }

    public static final Creator<Enum1Parcelable> CREATOR = new Creator<Enum1Parcelable>() {
        @Override
        public Enum1Parcelable createFromParcel(Parcel in) {
            return new Enum1Parcelable(in);
        }

        @Override
        public Enum1Parcelable[] newArray(int size) {
            return new Enum1Parcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(data.getValue());
    }

    public static Enum1Parcelable[] wrapArray(Enum1[] enums) {
        if (enums == null) return null;
        Enum1Parcelable[] result = new Enum1Parcelable[enums.length];
        for (int i = 0; i < enums.length; i++) {
            result[i] = new Enum1Parcelable(enums[i]);
        }
        return result;
    }

    public static Enum1[] unwrapArray(Enum1Parcelable[] parcelables) {
        if (parcelables == null) return null;
        Enum1[] out = new Enum1[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getEnum1();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
