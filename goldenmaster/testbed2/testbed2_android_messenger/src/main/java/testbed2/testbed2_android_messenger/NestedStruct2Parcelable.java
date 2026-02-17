package testbed2.testbed2_android_messenger;

import testbed2.testbed2_api.NestedStruct2;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import testbed2.testbed2_api.Struct1;
import testbed2.testbed2_api.Struct2;

  public  class NestedStruct2Parcelable implements Parcelable {

    public NestedStruct2 data;

    public NestedStruct2Parcelable(NestedStruct2 data) {
        this.data = data != null ? new NestedStruct2(data) : null;
    }

    public NestedStruct2 getNestedStruct2()
    {
        return data != null ? new NestedStruct2(data) : null;
    }

    protected NestedStruct2Parcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

        this.data = new NestedStruct2();
        Struct1Parcelable l_parcelablefield1 = in.readParcelable(Struct1Parcelable.class.getClassLoader(), Struct1Parcelable.class);
        data.field1 = l_parcelablefield1 != null ? l_parcelablefield1.data : null;
        Struct2Parcelable l_parcelablefield2 = in.readParcelable(Struct2Parcelable.class.getClassLoader(), Struct2Parcelable.class);
        data.field2 = l_parcelablefield2 != null ? l_parcelablefield2.data : null;
    }

    public static final Creator<NestedStruct2Parcelable> CREATOR = new Creator<NestedStruct2Parcelable>() {
        @Override
        public NestedStruct2Parcelable createFromParcel(Parcel in) {
            return new NestedStruct2Parcelable(in);
        }

        @Override
        public NestedStruct2Parcelable[] newArray(int size) {
            return new NestedStruct2Parcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }

        dest.writeParcelable(new Struct1Parcelable(data.field1), flags);
        dest.writeParcelable(new Struct2Parcelable(data.field2), flags);


    }
        public static NestedStruct2Parcelable[] wrapArray(NestedStruct2[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(NestedStruct2Parcelable::new)
           .toArray(NestedStruct2Parcelable[]::new);
    }

    public static NestedStruct2[] unwrapArray(NestedStruct2Parcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(NestedStruct2Parcelable::getNestedStruct2)
           .toArray(NestedStruct2[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
