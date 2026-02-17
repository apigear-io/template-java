package tbEnum.tbEnum_android_messenger;

import tbEnum.tbEnum_api.Enum0;
import android.os.Parcel;
import android.os.Parcelable;


import java.util.Arrays;

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
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

        this.data = Enum0.fromValue(in.readInt());
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

        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }

        dest.writeInt(data.getValue());
    }

    public static Enum0Parcelable[] wrapArray(Enum0[] enums) {
        if (enums == null) return null;
        return Arrays.stream(enums)
           .map(Enum0Parcelable::new)
           .toArray(Enum0Parcelable[]::new);
    }

    public static Enum0[] unwrapArray(Enum0Parcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(Enum0Parcelable::getEnum0)
           .toArray(Enum0[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
