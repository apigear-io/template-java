package tbEnum.tbEnum_android_messenger;

import tbEnum.tbEnum_api.Enum2;
import android.os.Parcel;
import android.os.Parcelable;


import java.util.Arrays;

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
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

        this.data = Enum2.fromValue(in.readInt());
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

        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }

        dest.writeInt(data.getValue());
    }

    public static Enum2Parcelable[] wrapArray(Enum2[] enums) {
        if (enums == null) return null;
        return Arrays.stream(enums)
           .map(Enum2Parcelable::new)
           .toArray(Enum2Parcelable[]::new);
    }

    public static Enum2[] unwrapArray(Enum2Parcelable[] parcelables) {
        if (parcelables == null) return new Enum2[0];
        return Arrays.stream(parcelables)
           .map(Enum2Parcelable::getEnum2)
           .toArray(Enum2[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
