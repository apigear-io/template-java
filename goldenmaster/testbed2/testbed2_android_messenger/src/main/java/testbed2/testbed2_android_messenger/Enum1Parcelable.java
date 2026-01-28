package testbed2.testbed2_android_messenger;

import testbed2.testbed2_api.Enum1;
import android.os.Parcel;
import android.os.Parcelable;


import java.util.Arrays;

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
        this.data = Enum1.fromValue(in.readInt());
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
        return Arrays.stream(enums)
           .map(Enum1Parcelable::new)
           .toArray(Enum1Parcelable[]::new);
    }

    public static Enum1[] unwrapArray(Enum1Parcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(Enum1Parcelable::getEnum1)
           .toArray(Enum1[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
