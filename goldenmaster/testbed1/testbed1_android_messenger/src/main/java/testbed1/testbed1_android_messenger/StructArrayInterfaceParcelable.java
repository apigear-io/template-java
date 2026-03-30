package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.IStructArrayInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import testbed1.testbed1_api.Enum0;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_api.StructString;

  public  class StructArrayInterfaceParcelable implements Parcelable {

    private static final String TAG = "StructArrayInterfaceParcelable";

    public IStructArrayInterface data;

    public StructArrayInterfaceParcelable(IStructArrayInterface data) {
        this.data = data;
    }

    public IStructArrayInterface getStructArrayInterface()
    {
        return data;
    }

    protected StructArrayInterfaceParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
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
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
        dest.writeTypedArray(StructBoolParcelable.wrapArray(Conversions.toArray(data.getPropBool(), new StructBool[0])), flags);
        dest.writeTypedArray(StructIntParcelable.wrapArray(Conversions.toArray(data.getPropInt(), new StructInt[0])), flags);
        dest.writeTypedArray(StructFloatParcelable.wrapArray(Conversions.toArray(data.getPropFloat(), new StructFloat[0])), flags);
        dest.writeTypedArray(StructStringParcelable.wrapArray(Conversions.toArray(data.getPropString(), new StructString[0])), flags);
        dest.writeTypedArray(Enum0Parcelable.wrapArray(Conversions.toArray(data.getPropEnum(), new Enum0[0])), flags);


    }
        public static StructArrayInterfaceParcelable[] wrapArray(IStructArrayInterface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(StructArrayInterfaceParcelable::new)
           .toArray(StructArrayInterfaceParcelable[]::new);
    }

    public static IStructArrayInterface[] unwrapArray(StructArrayInterfaceParcelable[] parcelables) {
        if (parcelables == null) return new IStructArrayInterface[0];
        return Arrays.stream(parcelables)
           .map(StructArrayInterfaceParcelable::getStructArrayInterface)
           .toArray(IStructArrayInterface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
