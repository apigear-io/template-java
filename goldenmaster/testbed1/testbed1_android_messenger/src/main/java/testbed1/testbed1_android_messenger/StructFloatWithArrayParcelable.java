package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructFloatWithArray;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

  public  class StructFloatWithArrayParcelable implements Parcelable {

    public StructFloatWithArray data;

    public StructFloatWithArrayParcelable(StructFloatWithArray data) {
        this.data = data != null ? new StructFloatWithArray(data) : null;
    }

    public StructFloatWithArray getStructFloatWithArray()
    {
        return data != null ? new StructFloatWithArray(data) : null;
    }

    protected StructFloatWithArrayParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

        this.data = new StructFloatWithArray();
        data.fieldFloat = Conversions.toList(in.createFloatArray());
    }

    public static final Creator<StructFloatWithArrayParcelable> CREATOR = new Creator<StructFloatWithArrayParcelable>() {
        @Override
        public StructFloatWithArrayParcelable createFromParcel(Parcel in) {
            return new StructFloatWithArrayParcelable(in);
        }

        @Override
        public StructFloatWithArrayParcelable[] newArray(int size) {
            return new StructFloatWithArrayParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }

        dest.writeFloatArray(Conversions.toArray(data.fieldFloat, new float[0]));


    }
        public static StructFloatWithArrayParcelable[] wrapArray(StructFloatWithArray[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(StructFloatWithArrayParcelable::new)
           .toArray(StructFloatWithArrayParcelable[]::new);
    }

    public static StructFloatWithArray[] unwrapArray(StructFloatWithArrayParcelable[] parcelables) {
        if (parcelables == null) return new StructFloatWithArray[0];
        return Arrays.stream(parcelables)
           .map(StructFloatWithArrayParcelable::getStructFloatWithArray)
           .toArray(StructFloatWithArray[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
