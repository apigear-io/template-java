package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructEnum;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import testbed1.testbed1_api.Enum0;

  public  class StructEnumParcelable implements Parcelable {

    public StructEnum data;

    public StructEnumParcelable(StructEnum data) {
        this.data = data != null ? new StructEnum(data) : null;
    }

    public StructEnum getStructEnum()
    {
        return data != null ? new StructEnum(data) : null;
    }

    protected StructEnumParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

        this.data = new StructEnum();
        Enum0Parcelable l_parcelablefieldEnum = in.readParcelable(Enum0Parcelable.class.getClassLoader(), Enum0Parcelable.class);
        data.fieldEnum = l_parcelablefieldEnum != null ? l_parcelablefieldEnum.data : null;
    }

    public static final Creator<StructEnumParcelable> CREATOR = new Creator<StructEnumParcelable>() {
        @Override
        public StructEnumParcelable createFromParcel(Parcel in) {
            return new StructEnumParcelable(in);
        }

        @Override
        public StructEnumParcelable[] newArray(int size) {
            return new StructEnumParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }

        dest.writeParcelable(new Enum0Parcelable(data.fieldEnum), flags);


    }
        public static StructEnumParcelable[] wrapArray(StructEnum[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(StructEnumParcelable::new)
           .toArray(StructEnumParcelable[]::new);
    }

    public static StructEnum[] unwrapArray(StructEnumParcelable[] parcelables) {
        if (parcelables == null) return new StructEnum[0];
        return Arrays.stream(parcelables)
           .map(StructEnumParcelable::getStructEnum)
           .toArray(StructEnum[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
