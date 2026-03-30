package tbSimple.tbSimple_android_messenger;

import tbSimple.tbSimple_api.ISimpleArrayInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        dest.writeBooleanArray(Conversions.toArray(data.getPropBool(), new boolean[0]));
        dest.writeIntArray(Conversions.toArray(data.getPropInt(), new int[0]));
        dest.writeIntArray(Conversions.toArray(data.getPropInt32(), new int[0]));
        dest.writeLongArray(Conversions.toArray(data.getPropInt64(), new long[0]));
        dest.writeFloatArray(Conversions.toArray(data.getPropFloat(), new float[0]));
        dest.writeFloatArray(Conversions.toArray(data.getPropFloat32(), new float[0]));
        dest.writeDoubleArray(Conversions.toArray(data.getPropFloat64(), new double[0]));
        dest.writeStringArray(Conversions.toArray(data.getPropString(), new String[0]));
        dest.writeString(data.getPropReadOnlyString());


    }
        public static SimpleArrayInterfaceParcelable[] wrapArray(ISimpleArrayInterface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(SimpleArrayInterfaceParcelable::new)
           .toArray(SimpleArrayInterfaceParcelable[]::new);
    }

    public static ISimpleArrayInterface[] unwrapArray(SimpleArrayInterfaceParcelable[] parcelables) {
        if (parcelables == null) return new ISimpleArrayInterface[0];
        return Arrays.stream(parcelables)
           .map(SimpleArrayInterfaceParcelable::getSimpleArrayInterface)
           .toArray(ISimpleArrayInterface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
