package testbed2.testbed2_android_messenger;

import testbed2.testbed2_api.Struct3;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

  public  class Struct3Parcelable implements Parcelable {

    public Struct3 data;

    public Struct3Parcelable(Struct3 data) {
        this.data = new Struct3(data);
    }

    public Struct3 getStruct3()
    {
        return new Struct3(data);
    }

    protected Struct3Parcelable(Parcel in) {
        this.data = new Struct3();
        data.field1 = in.readInt();
        data.field2 = in.readInt();
        data.field3 = in.readInt();
    }

    public static final Creator<Struct3Parcelable> CREATOR = new Creator<Struct3Parcelable>() {
        @Override
        public Struct3Parcelable createFromParcel(Parcel in) {
            return new Struct3Parcelable(in);
        }

        @Override
        public Struct3Parcelable[] newArray(int size) {
            return new Struct3Parcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(data.field1);
        dest.writeInt(data.field2);
        dest.writeInt(data.field3);


    }
        public static Struct3Parcelable[] wrapArray(Struct3[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(Struct3Parcelable::new)
           .toArray(Struct3Parcelable[]::new);
    }

    public static Struct3[] unwrapArray(Struct3Parcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(Struct3Parcelable::getStruct3)
           .toArray(Struct3[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
