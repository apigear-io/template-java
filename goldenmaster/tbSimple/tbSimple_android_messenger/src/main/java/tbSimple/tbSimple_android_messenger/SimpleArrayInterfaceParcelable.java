package tbSimple.tbSimple_android_messenger;

import tbSimple.tbSimple_api.ISimpleArrayInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Arrays;

  public  class SimpleArrayInterfaceParcelable implements Parcelable {

    private static final String TAG = "SimpleArrayInterfaceParcelable";

    public ISimpleArrayInterface data;

    public SimpleArrayInterfaceParcelable(ISimpleArrayInterface data) {
        this.data = data;
    }

    public ISimpleArrayInterface getSimpleArrayInterface()
    {
        return data;
    }

    protected SimpleArrayInterfaceParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
    }

    public static final Creator<SimpleArrayInterfaceParcelable> CREATOR = new Creator<SimpleArrayInterfaceParcelable>() {
        @Override
        public SimpleArrayInterfaceParcelable createFromParcel(Parcel in) {
            return new SimpleArrayInterfaceParcelable(in);
        }

        @Override
        public SimpleArrayInterfaceParcelable[] newArray(int size) {
            return new SimpleArrayInterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
        dest.writeBooleanArray(data.getPropBool());
        dest.writeIntArray(data.getPropInt());
        dest.writeIntArray(data.getPropInt32());
        dest.writeLongArray(data.getPropInt64());
        dest.writeFloatArray(data.getPropFloat());
        dest.writeFloatArray(data.getPropFloat32());
        dest.writeDoubleArray(data.getPropFloat64());
        dest.writeStringArray(data.getPropString());
        dest.writeString(data.getPropReadOnlyString());


    }
        public static SimpleArrayInterfaceParcelable[] wrapArray(ISimpleArrayInterface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(SimpleArrayInterfaceParcelable::new)
           .toArray(SimpleArrayInterfaceParcelable[]::new);
    }

    public static ISimpleArrayInterface[] unwrapArray(SimpleArrayInterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(SimpleArrayInterfaceParcelable::getSimpleArrayInterface)
           .toArray(ISimpleArrayInterface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
