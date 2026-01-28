package testbed2.testbed2_android_messenger;

import testbed2.testbed2_api.Struct2;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

  public  class Struct2Parcelable implements Parcelable {

    public Struct2 data;

    public Struct2Parcelable(Struct2 data) {
        this.data = new Struct2(data);
    }

    public Struct2 getStruct2()
    {
        return new Struct2(data);
    }

    protected Struct2Parcelable(Parcel in) {
        this.data = new Struct2();
        data.field1 = in.readInt();
        data.field2 = in.readInt();
    }

    public static final Creator<Struct2Parcelable> CREATOR = new Creator<Struct2Parcelable>() {
        @Override
        public Struct2Parcelable createFromParcel(Parcel in) {
            return new Struct2Parcelable(in);
        }

        @Override
        public Struct2Parcelable[] newArray(int size) {
            return new Struct2Parcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(data.field1);
        dest.writeInt(data.field2);


    }
        public static Struct2Parcelable[] wrapArray(Struct2[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(Struct2Parcelable::new)
           .toArray(Struct2Parcelable[]::new);
    }

    public static Struct2[] unwrapArray(Struct2Parcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(Struct2Parcelable::getStruct2)
           .toArray(Struct2[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
