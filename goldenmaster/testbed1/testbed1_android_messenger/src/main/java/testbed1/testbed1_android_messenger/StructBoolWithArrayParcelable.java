package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructBoolWithArray;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

  public  class StructBoolWithArrayParcelable implements Parcelable {

    public StructBoolWithArray data;

    public StructBoolWithArrayParcelable(StructBoolWithArray data) {
        this.data = data != null ? new StructBoolWithArray(data) : null;
    }

    public StructBoolWithArray getStructBoolWithArray()
    {
        return data != null ? new StructBoolWithArray(data) : null;
    }

    protected StructBoolWithArrayParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

        this.data = new StructBoolWithArray();
        data.fieldBool = Conversions.toList(in.createBooleanArray());
    }

    public static final Creator<StructBoolWithArrayParcelable> CREATOR = new Creator<StructBoolWithArrayParcelable>() {
        @Override
        public StructBoolWithArrayParcelable createFromParcel(Parcel in) {
            return new StructBoolWithArrayParcelable(in);
        }

        @Override
        public StructBoolWithArrayParcelable[] newArray(int size) {
            return new StructBoolWithArrayParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }

        dest.writeBooleanArray(Conversions.toArray(data.fieldBool, new boolean[0]));


    }
        public static StructBoolWithArrayParcelable[] wrapArray(StructBoolWithArray[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(StructBoolWithArrayParcelable::new)
           .toArray(StructBoolWithArrayParcelable[]::new);
    }

    public static StructBoolWithArray[] unwrapArray(StructBoolWithArrayParcelable[] parcelables) {
        if (parcelables == null) return new StructBoolWithArray[0];
        return Arrays.stream(parcelables)
           .map(StructBoolWithArrayParcelable::getStructBoolWithArray)
           .toArray(StructBoolWithArray[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
