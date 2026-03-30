package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructStructWithArray;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import testbed1.testbed1_api.StructStringWithArray;

  public  class StructStructWithArrayParcelable implements Parcelable {

    public StructStructWithArray data;

    public StructStructWithArrayParcelable(StructStructWithArray data) {
        this.data = data != null ? new StructStructWithArray(data) : null;
    }

    public StructStructWithArray getStructStructWithArray()
    {
        return data != null ? new StructStructWithArray(data) : null;
    }

    protected StructStructWithArrayParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

        this.data = new StructStructWithArray();
        data.fieldStruct = Conversions.toList(StructStringWithArrayParcelable.unwrapArray(in.createTypedArray(StructStringWithArrayParcelable.CREATOR)));
    }

    public static final Creator<StructStructWithArrayParcelable> CREATOR = new Creator<StructStructWithArrayParcelable>() {
        @Override
        public StructStructWithArrayParcelable createFromParcel(Parcel in) {
            return new StructStructWithArrayParcelable(in);
        }

        @Override
        public StructStructWithArrayParcelable[] newArray(int size) {
            return new StructStructWithArrayParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }

        dest.writeTypedArray(StructStringWithArrayParcelable.wrapArray(Conversions.toArray(data.fieldStruct, new StructStringWithArray[0])), flags);


    }
        public static StructStructWithArrayParcelable[] wrapArray(StructStructWithArray[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(StructStructWithArrayParcelable::new)
           .toArray(StructStructWithArrayParcelable[]::new);
    }

    public static StructStructWithArray[] unwrapArray(StructStructWithArrayParcelable[] parcelables) {
        if (parcelables == null) return new StructStructWithArray[0];
        return Arrays.stream(parcelables)
           .map(StructStructWithArrayParcelable::getStructStructWithArray)
           .toArray(StructStructWithArray[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
