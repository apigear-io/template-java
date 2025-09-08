package tbEnum.tbEnum_android_messenger;

import tbEnum.tbEnum_api.Enum0;
import android.os.Parcel;
import android.os.Parcelable;



//TODO imports - may need some struct from this or imported module

  public  class Enum0Parcelable implements Parcelable {

    public Enum0 data;

    public Enum0Parcelable(Enum0 data) {
        this.data = data;
    }

    public Enum0 getEnum0()
    {
        return data;
    }

    protected Enum0Parcelable(Parcel in) {
        int intValue = in.readInt();
        this.data = Enum0.fromValue(intValue);
    }

    public static final Creator<Enum0Parcelable> CREATOR = new Creator<Enum0Parcelable>() {
        @Override
        public Enum0Parcelable createFromParcel(Parcel in) {
            return new Enum0Parcelable(in);
        }

        @Override
        public Enum0Parcelable[] newArray(int size) {
            return new Enum0Parcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(data.getValue());
    }

    public static Enum0Parcelable[] wrapArray(Enum0[] enums) {
        if (enums == null) return null;
        Enum0Parcelable[] result = new Enum0Parcelable[enums.length];
        for (int i = 0; i < enums.length; i++) {
            result[i] = new Enum0Parcelable(enums[i]);
        }
        return result;
    }

    public static Enum0[] unwrapArray(Enum0Parcelable[] parcelables) {
        if (parcelables == null) return null;
        Enum0[] out = new Enum0[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getEnum0();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
