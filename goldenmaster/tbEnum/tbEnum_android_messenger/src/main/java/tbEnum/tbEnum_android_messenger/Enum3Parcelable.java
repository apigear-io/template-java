package tbEnum.tbEnum_android_messenger;

import tbEnum.tbEnum_api.Enum3;
import android.os.Parcel;
import android.os.Parcelable;



//TODO imports - may need some struct from this or imported module

  public  class Enum3Parcelable implements Parcelable {

    public Enum3 data;

    public Enum3Parcelable(Enum3 data) {
        this.data = data;
    }

    public Enum3 getEnum3()
    {
        return data;
    }

    protected Enum3Parcelable(Parcel in) {
        int intValue = in.readInt();
        this.data = Enum3.fromValue(intValue);
    }

    public static final Creator<Enum3Parcelable> CREATOR = new Creator<Enum3Parcelable>() {
        @Override
        public Enum3Parcelable createFromParcel(Parcel in) {
            return new Enum3Parcelable(in);
        }

        @Override
        public Enum3Parcelable[] newArray(int size) {
            return new Enum3Parcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(data.getValue());
    }

    public static Enum3Parcelable[] wrapArray(Enum3[] enums) {
        if (enums == null) return null;
        Enum3Parcelable[] result = new Enum3Parcelable[enums.length];
        for (int i = 0; i < enums.length; i++) {
            result[i] = new Enum3Parcelable(enums[i]);
        }
        return result;
    }

    public static Enum3[] unwrapArray(Enum3Parcelable[] parcelables) {
        if (parcelables == null) return null;
        Enum3[] out = new Enum3[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getEnum3();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
