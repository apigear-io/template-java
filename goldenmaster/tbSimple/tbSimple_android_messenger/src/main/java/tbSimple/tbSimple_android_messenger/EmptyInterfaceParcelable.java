package tbSimple.tbSimple_android_messenger;

import tbSimple.tbSimple_api.IEmptyInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Arrays;

  public  class EmptyInterfaceParcelable implements Parcelable {

    private static final String TAG = "EmptyInterfaceParcelable";

    public IEmptyInterface data;

    public EmptyInterfaceParcelable(IEmptyInterface data) {
        this.data = data;
    }

    public IEmptyInterface getEmptyInterface()
    {
        return data;
    }

    protected EmptyInterfaceParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
    }

    public static final Creator<EmptyInterfaceParcelable> CREATOR = new Creator<EmptyInterfaceParcelable>() {
        @Override
        public EmptyInterfaceParcelable createFromParcel(Parcel in) {
            return new EmptyInterfaceParcelable(in);
        }

        @Override
        public EmptyInterfaceParcelable[] newArray(int size) {
            return new EmptyInterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }


    }
        public static EmptyInterfaceParcelable[] wrapArray(IEmptyInterface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(EmptyInterfaceParcelable::new)
           .toArray(EmptyInterfaceParcelable[]::new);
    }

    public static IEmptyInterface[] unwrapArray(EmptyInterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(EmptyInterfaceParcelable::getEmptyInterface)
           .toArray(IEmptyInterface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
