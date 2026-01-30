package testbed2.testbed2_android_messenger;

import testbed2.testbed2_api.Struct4;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

  public  class Struct4Parcelable implements Parcelable {

    public Struct4 data;

    public Struct4Parcelable(Struct4 data) {
        this.data = data != null ? new Struct4(data) : null;
    }

    public Struct4 getStruct4()
    {
        return data != null ? new Struct4(data) : null;
    }

    protected Struct4Parcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

        this.data = new Struct4();
        data.field1 = in.readInt();
        data.field2 = in.readInt();
        data.field3 = in.readInt();
        data.field4 = in.readInt();
    }

    public static final Creator<Struct4Parcelable> CREATOR = new Creator<Struct4Parcelable>() {
        @Override
        public Struct4Parcelable createFromParcel(Parcel in) {
            return new Struct4Parcelable(in);
        }

        @Override
        public Struct4Parcelable[] newArray(int size) {
            return new Struct4Parcelable[size];
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
        dest.writeInt(data.field4);


    }
        public static Struct4Parcelable[] wrapArray(Struct4[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(Struct4Parcelable::new)
           .toArray(Struct4Parcelable[]::new);
    }

    public static Struct4[] unwrapArray(Struct4Parcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(Struct4Parcelable::getStruct4)
           .toArray(Struct4[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
