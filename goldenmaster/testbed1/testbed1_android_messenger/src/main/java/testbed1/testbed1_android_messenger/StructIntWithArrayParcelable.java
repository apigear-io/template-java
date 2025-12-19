package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructIntWithArray;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

  public  class StructIntWithArrayParcelable implements Parcelable {

    public StructIntWithArray data;

    public StructIntWithArrayParcelable(StructIntWithArray data) {
        this.data = new StructIntWithArray(data);
    }

    public StructIntWithArray getStructIntWithArray()
    {
        return new StructIntWithArray(data);
    }

    protected StructIntWithArrayParcelable(Parcel in) {
    this.data = new StructIntWithArray();
        data.fieldInt = in.createIntArray();
    }

    public static final Creator<StructIntWithArrayParcelable> CREATOR = new Creator<StructIntWithArrayParcelable>() {
        @Override
        public StructIntWithArrayParcelable createFromParcel(Parcel in) {
            return new StructIntWithArrayParcelable(in);
        }

        @Override
        public StructIntWithArrayParcelable[] newArray(int size) {
            return new StructIntWithArrayParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(data.fieldInt);


    }
        public static StructIntWithArrayParcelable[] wrapArray(StructIntWithArray[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(StructIntWithArrayParcelable::new)
           .toArray(StructIntWithArrayParcelable[]::new);
    }

    public static StructIntWithArray[] unwrapArray(StructIntWithArrayParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(StructIntWithArrayParcelable::getStructIntWithArray)
           .toArray(StructIntWithArray[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
