package testbed2.testbed2_android_messenger;

import testbed2.testbed2_api.NestedStruct3;
import android.os.Parcel;
import android.os.Parcelable;
import testbed2.testbed2_api.Struct2;
import testbed2.testbed2_api.Struct3;

  public  class NestedStruct3Parcelable implements Parcelable {

    public NestedStruct3 data;

    public NestedStruct3Parcelable(NestedStruct3 data) {
        this.data = new NestedStruct3(data);
    }

    public NestedStruct3 getNestedStruct3()
    {
        return new NestedStruct3(data);
    }

    protected NestedStruct3Parcelable(Parcel in) {
    this.data = new NestedStruct3();
        Enum1Parcelable l_parcelablefield1 = in.readParcelable(Enum1Parcelable.class.getClassLoader(), Enum1Parcelable.class);
        data.field1 = l_parcelablefield1 != null ? l_parcelablefield1.data : null;
        Struct2Parcelable l_parcelablefield2 = in.readParcelable(Struct2Parcelable.class.getClassLoader(), Struct2Parcelable.class);
        data.field2 = l_parcelablefield2 != null ? l_parcelablefield2.data : null;
        Struct3Parcelable l_parcelablefield3 = in.readParcelable(Struct3Parcelable.class.getClassLoader(), Struct3Parcelable.class);
        data.field3 = l_parcelablefield3 != null ? l_parcelablefield3.data : null;
        Enum1Parcelable[] l_parcelablefieldX = in.createTypedArray(Enum1Parcelable.CREATOR);
        data.fieldX = Enum1Parcelable.unwrapArray(l_parcelablefieldX);
        Struct2Parcelable[] l_parcelablefieldY = in.createTypedArray(Struct2Parcelable.CREATOR);
        data.fieldY = Struct2Parcelable.unwrapArray(l_parcelablefieldY);
        Struct3Parcelable[] l_parcelablefieldZ = in.createTypedArray(Struct3Parcelable.CREATOR);
        data.fieldZ = Struct3Parcelable.unwrapArray(l_parcelablefieldZ);
    }

    public static final Creator<NestedStruct3Parcelable> CREATOR = new Creator<NestedStruct3Parcelable>() {
        @Override
        public NestedStruct3Parcelable createFromParcel(Parcel in) {
            return new NestedStruct3Parcelable(in);
        }

        @Override
        public NestedStruct3Parcelable[] newArray(int size) {
            return new NestedStruct3Parcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(new Enum1Parcelable(data.field1), flags);
        dest.writeParcelable(new Struct2Parcelable(data.field2), flags);
        dest.writeParcelable(new Struct3Parcelable(data.field3), flags);
        dest.writeTypedArray(Enum1Parcelable.wrapArray(data.fieldX), flags);
        dest.writeTypedArray(Struct2Parcelable.wrapArray(data.fieldY), flags);
        dest.writeTypedArray(Struct3Parcelable.wrapArray(data.fieldZ), flags);


    }
        public static NestedStruct3Parcelable[] wrapArray(NestedStruct3[] structs) {
        if (structs == null) return null;
        NestedStruct3Parcelable[] out = new NestedStruct3Parcelable[structs.length];
        for (int i = 0; i < structs.length; i++) {
            out[i] = new NestedStruct3Parcelable(structs[i]);
        }
        return out;
    }

    public static NestedStruct3[] unwrapArray(NestedStruct3Parcelable[] parcelables) {
        if (parcelables == null) return null;
        NestedStruct3[] out = new NestedStruct3[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getNestedStruct3();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
