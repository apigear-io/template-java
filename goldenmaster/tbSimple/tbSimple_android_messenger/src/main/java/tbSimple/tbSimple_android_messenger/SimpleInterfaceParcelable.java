package tbSimple.tbSimple_android_messenger;

import tbSimple.tbSimple_api.ISimpleInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Arrays;

  public  class SimpleInterfaceParcelable implements Parcelable {

    private static final String TAG = "SimpleInterfaceParcelable";

    public ISimpleInterface data;

    public SimpleInterfaceParcelable(ISimpleInterface data) {
        this.data = data;
    }

    public ISimpleInterface getSimpleInterface()
    {
        return data;
    }

    protected SimpleInterfaceParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
    }

    public static final Creator<SimpleInterfaceParcelable> CREATOR = new Creator<SimpleInterfaceParcelable>() {
        @Override
        public SimpleInterfaceParcelable createFromParcel(Parcel in) {
            return new SimpleInterfaceParcelable(in);
        }

        @Override
        public SimpleInterfaceParcelable[] newArray(int size) {
            return new SimpleInterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
        dest.writeBoolean(data.getPropBool());
        dest.writeInt(data.getPropInt());
        dest.writeInt(data.getPropInt32());
        dest.writeLong(data.getPropInt64());
        dest.writeFloat(data.getPropFloat());
        dest.writeFloat(data.getPropFloat32());
        dest.writeDouble(data.getPropFloat64());
        dest.writeString(data.getPropString());


    }
        public static SimpleInterfaceParcelable[] wrapArray(ISimpleInterface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(SimpleInterfaceParcelable::new)
           .toArray(SimpleInterfaceParcelable[]::new);
    }

    public static ISimpleInterface[] unwrapArray(SimpleInterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(SimpleInterfaceParcelable::getSimpleInterface)
           .toArray(ISimpleInterface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
