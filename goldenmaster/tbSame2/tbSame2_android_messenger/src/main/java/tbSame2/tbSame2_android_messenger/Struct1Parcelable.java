package tbSame2.tbSame2_android_messenger;

import tbSame2.tbSame2_api.Struct1;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

  public  class Struct1Parcelable implements Parcelable {

    public Struct1 data;

    public Struct1Parcelable(Struct1 data) {
        this.data = data != null ? new Struct1(data) : null;
    }

    public Struct1 getStruct1()
    {
        return data != null ? new Struct1(data) : null;
    }

    protected Struct1Parcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

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
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
        dest.writeInt(data.field1);
        dest.writeInt(data.field2);
        dest.writeInt(data.field3);


    }
        public static Struct1Parcelable[] wrapArray(Struct1[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(Struct1Parcelable::new)
           .toArray(Struct1Parcelable[]::new);
    }

    public static Struct1[] unwrapArray(Struct1Parcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(Struct1Parcelable::getStruct1)
           .toArray(Struct1[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
