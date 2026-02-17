package tbRefIfaces.tbRefIfaces_android_messenger;

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Arrays;

  public  class SimpleLocalIfParcelable implements Parcelable {

    private static final String TAG = "SimpleLocalIfParcelable";

    public ISimpleLocalIf data;

    public SimpleLocalIfParcelable(ISimpleLocalIf data) {
        this.data = data;
    }

    public ISimpleLocalIf getSimpleLocalIf()
    {
        return data;
    }

    protected SimpleLocalIfParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
    }

    public static final Creator<SimpleLocalIfParcelable> CREATOR = new Creator<SimpleLocalIfParcelable>() {
        @Override
        public SimpleLocalIfParcelable createFromParcel(Parcel in) {
            return new SimpleLocalIfParcelable(in);
        }

        @Override
        public SimpleLocalIfParcelable[] newArray(int size) {
            return new SimpleLocalIfParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
        dest.writeInt(data.getIntProperty());


    }
        public static SimpleLocalIfParcelable[] wrapArray(ISimpleLocalIf[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(SimpleLocalIfParcelable::new)
           .toArray(SimpleLocalIfParcelable[]::new);
    }

    public static ISimpleLocalIf[] unwrapArray(SimpleLocalIfParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(SimpleLocalIfParcelable::getSimpleLocalIf)
           .toArray(ISimpleLocalIf[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
