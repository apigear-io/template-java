package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructEnumWithArray;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import testbed1.testbed1_api.Enum0;

  public  class StructEnumWithArrayParcelable implements Parcelable {

    public StructEnumWithArray data;

    public StructEnumWithArrayParcelable(StructEnumWithArray data) {
        this.data = data != null ? new StructEnumWithArray(data) : null;
    }

    public StructEnumWithArray getStructEnumWithArray()
    {
        return data != null ? new StructEnumWithArray(data) : null;
    }

    protected StructEnumWithArrayParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

        this.data = new StructEnumWithArray();
        data.fieldEnum = Conversions.toList(Enum0Parcelable.unwrapArray(in.createTypedArray(Enum0Parcelable.CREATOR)));
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
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }

        dest.writeTypedArray(Enum0Parcelable.wrapArray(Conversions.toArray(data.fieldEnum, new Enum0[0])), flags);


    }
        public static StructEnumWithArrayParcelable[] wrapArray(StructEnumWithArray[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(StructEnumWithArrayParcelable::new)
           .toArray(StructEnumWithArrayParcelable[]::new);
    }

    public static StructEnumWithArray[] unwrapArray(StructEnumWithArrayParcelable[] parcelables) {
        if (parcelables == null) return new StructEnumWithArray[0];
        return Arrays.stream(parcelables)
           .map(StructEnumWithArrayParcelable::getStructEnumWithArray)
           .toArray(StructEnumWithArray[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
