package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructIntWithArray;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

  public  class StructIntWithArrayParcelable implements Parcelable {

    public StructIntWithArray data;

    public StructIntWithArrayParcelable(StructIntWithArray data) {
        this.data = data != null ? new StructIntWithArray(data) : null;
    }

    public StructIntWithArray getStructIntWithArray()
    {
        return data != null ? new StructIntWithArray(data) : null;
    }

    protected StructIntWithArrayParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

        this.data = new StructIntWithArray();
        data.fieldInt = Conversions.toList(in.createIntArray());
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
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }

        dest.writeIntArray(Conversions.toArray(data.fieldInt, new int[0]));


    }
        public static StructIntWithArrayParcelable[] wrapArray(StructIntWithArray[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(StructIntWithArrayParcelable::new)
           .toArray(StructIntWithArrayParcelable[]::new);
    }

    public static StructIntWithArray[] unwrapArray(StructIntWithArrayParcelable[] parcelables) {
        if (parcelables == null) return new StructIntWithArray[0];
        return Arrays.stream(parcelables)
           .map(StructIntWithArrayParcelable::getStructIntWithArray)
           .toArray(StructIntWithArray[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
