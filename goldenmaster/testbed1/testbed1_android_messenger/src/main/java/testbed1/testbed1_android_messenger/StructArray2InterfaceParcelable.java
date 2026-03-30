package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.IStructArray2Interface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import testbed1.testbed1_api.Enum0;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_api.StructBoolWithArray;
import testbed1.testbed1_api.StructEnumWithArray;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_api.StructFloatWithArray;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_api.StructIntWithArray;
import testbed1.testbed1_api.StructString;
import testbed1.testbed1_api.StructStringWithArray;

  public  class StructArray2InterfaceParcelable implements Parcelable {

    private static final String TAG = "StructArray2InterfaceParcelable";

    public IStructArray2Interface data;

    public StructArray2InterfaceParcelable(IStructArray2Interface data) {
        this.data = data;
    }

    public IStructArray2Interface getStructArray2Interface()
    {
        return data;
    }

    protected StructArray2InterfaceParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
    }

    public static final Creator<StructArray2InterfaceParcelable> CREATOR = new Creator<StructArray2InterfaceParcelable>() {
        @Override
        public StructArray2InterfaceParcelable createFromParcel(Parcel in) {
            return new StructArray2InterfaceParcelable(in);
        }

        @Override
        public StructArray2InterfaceParcelable[] newArray(int size) {
            return new StructArray2InterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
        dest.writeParcelable(new StructBoolWithArrayParcelable(data.getPropBool()), flags);
        dest.writeParcelable(new StructIntWithArrayParcelable(data.getPropInt()), flags);
        dest.writeParcelable(new StructFloatWithArrayParcelable(data.getPropFloat()), flags);
        dest.writeParcelable(new StructStringWithArrayParcelable(data.getPropString()), flags);
        dest.writeParcelable(new StructEnumWithArrayParcelable(data.getPropEnum()), flags);


    }
        public static StructArray2InterfaceParcelable[] wrapArray(IStructArray2Interface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(StructArray2InterfaceParcelable::new)
           .toArray(StructArray2InterfaceParcelable[]::new);
    }

    public static IStructArray2Interface[] unwrapArray(StructArray2InterfaceParcelable[] parcelables) {
        if (parcelables == null) return new IStructArray2Interface[0];
        return Arrays.stream(parcelables)
           .map(StructArray2InterfaceParcelable::getStructArray2Interface)
           .toArray(IStructArray2Interface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
