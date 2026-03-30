package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.IStructInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_api.StructString;

  public  class StructInterfaceParcelable implements Parcelable {

    private static final String TAG = "StructInterfaceParcelable";

    public IStructInterface data;

    public StructInterfaceParcelable(IStructInterface data) {
        this.data = data;
    }

    public IStructInterface getStructInterface()
    {
        return data;
    }

    protected StructInterfaceParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
    }

    public static final Creator<StructInterfaceParcelable> CREATOR = new Creator<StructInterfaceParcelable>() {
        @Override
        public StructInterfaceParcelable createFromParcel(Parcel in) {
            return new StructInterfaceParcelable(in);
        }

        @Override
        public StructInterfaceParcelable[] newArray(int size) {
            return new StructInterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
        dest.writeParcelable(new StructBoolParcelable(data.getPropBool()), flags);
        dest.writeParcelable(new StructIntParcelable(data.getPropInt()), flags);
        dest.writeParcelable(new StructFloatParcelable(data.getPropFloat()), flags);
        dest.writeParcelable(new StructStringParcelable(data.getPropString()), flags);


    }
        public static StructInterfaceParcelable[] wrapArray(IStructInterface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(StructInterfaceParcelable::new)
           .toArray(StructInterfaceParcelable[]::new);
    }

    public static IStructInterface[] unwrapArray(StructInterfaceParcelable[] parcelables) {
        if (parcelables == null) return new IStructInterface[0];
        return Arrays.stream(parcelables)
           .map(StructInterfaceParcelable::getStructInterface)
           .toArray(IStructInterface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
