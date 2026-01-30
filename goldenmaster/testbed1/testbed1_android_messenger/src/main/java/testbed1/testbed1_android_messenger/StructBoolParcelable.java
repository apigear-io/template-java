package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructBool;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

  public  class StructBoolParcelable implements Parcelable {

    public StructBool data;

    public StructBoolParcelable(StructBool data) {
        this.data = data != null ? new StructBool(data) : null;
    }

    public StructBool getStructBool()
    {
        return data != null ? new StructBool(data) : null;
    }

    protected StructBoolParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

        this.data = new StructBool();
        data.fieldBool = in.readBoolean();
    }

    public static final Creator<StructBoolParcelable> CREATOR = new Creator<StructBoolParcelable>() {
        @Override
        public StructBoolParcelable createFromParcel(Parcel in) {
            return new StructBoolParcelable(in);
        }

        @Override
        public StructBoolParcelable[] newArray(int size) {
            return new StructBoolParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
        dest.writeBoolean(data.fieldBool);


    }
        public static StructBoolParcelable[] wrapArray(StructBool[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(StructBoolParcelable::new)
           .toArray(StructBoolParcelable[]::new);
    }

    public static StructBool[] unwrapArray(StructBoolParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(StructBoolParcelable::getStructBool)
           .toArray(StructBool[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
