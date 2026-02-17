package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructInt;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

  public  class StructIntParcelable implements Parcelable {

    public StructInt data;

    public StructIntParcelable(StructInt data) {
        this.data = data != null ? new StructInt(data) : null;
    }

    public StructInt getStructInt()
    {
        return data != null ? new StructInt(data) : null;
    }

    protected StructIntParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

        this.data = new StructInt();
        data.fieldInt = in.readInt();
    }

    public static final Creator<StructIntParcelable> CREATOR = new Creator<StructIntParcelable>() {
        @Override
        public StructIntParcelable createFromParcel(Parcel in) {
            return new StructIntParcelable(in);
        }

        @Override
        public StructIntParcelable[] newArray(int size) {
            return new StructIntParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }

        dest.writeInt(data.fieldInt);


    }
        public static StructIntParcelable[] wrapArray(StructInt[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(StructIntParcelable::new)
           .toArray(StructIntParcelable[]::new);
    }

    public static StructInt[] unwrapArray(StructIntParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(StructIntParcelable::getStructInt)
           .toArray(StructInt[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
