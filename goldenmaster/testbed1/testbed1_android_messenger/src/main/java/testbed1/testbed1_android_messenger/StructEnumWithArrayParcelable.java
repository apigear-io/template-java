package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructEnumWithArray;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import testbed1.testbed1_api.Enum0;

  public  class StructEnumWithArrayParcelable implements Parcelable {

    public StructEnumWithArray data;

    public StructEnumWithArrayParcelable(StructEnumWithArray data) {
        this.data = new StructEnumWithArray(data);
    }

    public StructEnumWithArray getStructEnumWithArray()
    {
        return new StructEnumWithArray(data);
    }

    protected StructEnumWithArrayParcelable(Parcel in) {
        this.data = new StructEnumWithArray();
        Enum0Parcelable[] l_parcelablefieldEnum = in.createTypedArray(Enum0Parcelable.CREATOR);
        data.fieldEnum = Enum0Parcelable.unwrapArray(l_parcelablefieldEnum);
    }

    public static final Creator<StructEnumWithArrayParcelable> CREATOR = new Creator<StructEnumWithArrayParcelable>() {
        @Override
        public StructEnumWithArrayParcelable createFromParcel(Parcel in) {
            return new StructEnumWithArrayParcelable(in);
        }

        @Override
        public StructEnumWithArrayParcelable[] newArray(int size) {
            return new StructEnumWithArrayParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(Enum0Parcelable.wrapArray(data.fieldEnum), flags);


    }
        public static StructEnumWithArrayParcelable[] wrapArray(StructEnumWithArray[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(StructEnumWithArrayParcelable::new)
           .toArray(StructEnumWithArrayParcelable[]::new);
    }

    public static StructEnumWithArray[] unwrapArray(StructEnumWithArrayParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(StructEnumWithArrayParcelable::getStructEnumWithArray)
           .toArray(StructEnumWithArray[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
