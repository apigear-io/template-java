package testbed2.testbed2_android_messenger;

import testbed2.testbed2_api.Enum3;
import android.os.Parcel;
import android.os.Parcelable;


import java.util.Arrays;

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
        boolean dataIsValid = in.readBoolean();
        this.data = dataIsValid ? Enum3.fromValue(in.readInt()) : null;
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

        dest.writeBoolean(data != null);
        if (data != null) {
            dest.writeInt(data.getValue());
        }
    }

    public static Enum3Parcelable[] wrapArray(Enum3[] enums) {
        if (enums == null) return null;
        return Arrays.stream(enums)
           .map(Enum3Parcelable::new)
           .toArray(Enum3Parcelable[]::new);
    }

    public static Enum3[] unwrapArray(Enum3Parcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(Enum3Parcelable::getEnum3)
           .toArray(Enum3[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
