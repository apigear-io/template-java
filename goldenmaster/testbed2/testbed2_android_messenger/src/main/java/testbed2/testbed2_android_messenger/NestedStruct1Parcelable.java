package testbed2.testbed2_android_messenger;

import testbed2.testbed2_api.NestedStruct1;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import testbed2.testbed2_api.Struct1;

  public  class NestedStruct1Parcelable implements Parcelable {

    public NestedStruct1 data;

    public NestedStruct1Parcelable(NestedStruct1 data) {
        this.data = new NestedStruct1(data);
    }

    public NestedStruct1 getNestedStruct1()
    {
        return new NestedStruct1(data);
    }

    protected NestedStruct1Parcelable(Parcel in) {
    this.data = new NestedStruct1();
        Struct1Parcelable l_parcelablefield1 = in.readParcelable(Struct1Parcelable.class.getClassLoader(), Struct1Parcelable.class);
        data.field1 = l_parcelablefield1 != null ? l_parcelablefield1.data : null;
    }

    public static final Creator<NestedStruct1Parcelable> CREATOR = new Creator<NestedStruct1Parcelable>() {
        @Override
        public NestedStruct1Parcelable createFromParcel(Parcel in) {
            return new NestedStruct1Parcelable(in);
        }

        @Override
        public NestedStruct1Parcelable[] newArray(int size) {
            return new NestedStruct1Parcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(new Struct1Parcelable(data.field1), flags);


    }
        public static NestedStruct1Parcelable[] wrapArray(NestedStruct1[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(NestedStruct1Parcelable::new)
           .toArray(NestedStruct1Parcelable[]::new);
    }

    public static NestedStruct1[] unwrapArray(NestedStruct1Parcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(NestedStruct1Parcelable::getNestedStruct1)
           .toArray(NestedStruct1[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
