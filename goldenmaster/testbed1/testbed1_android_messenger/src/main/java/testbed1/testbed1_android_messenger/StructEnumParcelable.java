package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructEnum;
import android.os.Parcel;
import android.os.Parcelable;
import testbed1.testbed1_api.Enum0;

  public  class StructEnumParcelable implements Parcelable {

    public StructEnum data;

    public StructEnumParcelable(StructEnum data) {
        this.data = new StructEnum(data);
    }

    public StructEnum getStructEnum()
    {
        return new StructEnum(data);
    }

    protected StructEnumParcelable(Parcel in) {
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
        dest.writeParcelable(new Enum0Parcelable(data.fieldEnum), flags);


    }
        public static StructEnumParcelable[] wrapArray(StructEnum[] structs) {
        if (structs == null) return null;
        StructEnumParcelable[] out = new StructEnumParcelable[structs.length];
        for (int i = 0; i < structs.length; i++) {
            out[i] = new StructEnumParcelable(structs[i]);
        }
        return out;
    }

    public static StructEnum[] unwrapArray(StructEnumParcelable[] parcelables) {
        if (parcelables == null) return null;
        StructEnum[] out = new StructEnum[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getStructEnum();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
