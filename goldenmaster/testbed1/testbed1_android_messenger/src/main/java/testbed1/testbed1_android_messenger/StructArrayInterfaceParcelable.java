package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.IStructArrayInterface;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import testbed1.testbed1_api.Enum0;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_api.StructString;

  public  class StructArrayInterfaceParcelable implements Parcelable {

    public IStructArrayInterface data;

    public StructArrayInterfaceParcelable(IStructArrayInterface data) {
        this.data = data;
    }

    public IStructArrayInterface getStructArrayInterface()
    {
        return data;
    }

    protected StructArrayInterfaceParcelable(Parcel in) {
        StructBoolParcelable[] l_parcelablepropBool = in.createTypedArray(StructBoolParcelable.CREATOR);
        data.setPropBool(StructBoolParcelable.unwrapArray(l_parcelablepropBool));
        StructIntParcelable[] l_parcelablepropInt = in.createTypedArray(StructIntParcelable.CREATOR);
        data.setPropInt(StructIntParcelable.unwrapArray(l_parcelablepropInt));
        StructFloatParcelable[] l_parcelablepropFloat = in.createTypedArray(StructFloatParcelable.CREATOR);
        data.setPropFloat(StructFloatParcelable.unwrapArray(l_parcelablepropFloat));
        StructStringParcelable[] l_parcelablepropString = in.createTypedArray(StructStringParcelable.CREATOR);
        data.setPropString(StructStringParcelable.unwrapArray(l_parcelablepropString));
        Enum0Parcelable[] l_parcelablepropEnum = in.createTypedArray(Enum0Parcelable.CREATOR);
        data.setPropEnum(Enum0Parcelable.unwrapArray(l_parcelablepropEnum));
    }

    public static final Creator<StructArrayInterfaceParcelable> CREATOR = new Creator<StructArrayInterfaceParcelable>() {
        @Override
        public StructArrayInterfaceParcelable createFromParcel(Parcel in) {
            return new StructArrayInterfaceParcelable(in);
        }

        @Override
        public StructArrayInterfaceParcelable[] newArray(int size) {
            return new StructArrayInterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(StructBoolParcelable.wrapArray(data.getPropBool()), flags);
        dest.writeTypedArray(StructIntParcelable.wrapArray(data.getPropInt()), flags);
        dest.writeTypedArray(StructFloatParcelable.wrapArray(data.getPropFloat()), flags);
        dest.writeTypedArray(StructStringParcelable.wrapArray(data.getPropString()), flags);
        dest.writeTypedArray(Enum0Parcelable.wrapArray(data.getPropEnum()), flags);


    }
        public static StructArrayInterfaceParcelable[] wrapArray(IStructArrayInterface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(StructArrayInterfaceParcelable::new)
           .toArray(StructArrayInterfaceParcelable[]::new);
    }

    public static IStructArrayInterface[] unwrapArray(StructArrayInterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(StructArrayInterfaceParcelable::getStructArrayInterface)
           .toArray(IStructArrayInterface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
