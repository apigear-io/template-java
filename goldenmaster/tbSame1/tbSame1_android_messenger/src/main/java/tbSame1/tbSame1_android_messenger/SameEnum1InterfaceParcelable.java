package tbSame1.tbSame1_android_messenger;

import tbSame1.tbSame1_api.ISameEnum1Interface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import tbSame1.tbSame1_api.Enum1;

  public  class SameEnum1InterfaceParcelable implements Parcelable {

    private static final String TAG = "SameEnum1InterfaceParcelable";

    public ISameEnum1Interface data;

    public SameEnum1InterfaceParcelable(ISameEnum1Interface data) {
        this.data = data;
    }

    public ISameEnum1Interface getSameEnum1Interface()
    {
        return data;
    }

    protected SameEnum1InterfaceParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
    }

    public static final Creator<SameEnum1InterfaceParcelable> CREATOR = new Creator<SameEnum1InterfaceParcelable>() {
        @Override
        public SameEnum1InterfaceParcelable createFromParcel(Parcel in) {
            return new SameEnum1InterfaceParcelable(in);
        }

        @Override
        public SameEnum1InterfaceParcelable[] newArray(int size) {
            return new SameEnum1InterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
        dest.writeParcelable(new Enum1Parcelable(data.getProp1()), flags);


    }
        public static SameEnum1InterfaceParcelable[] wrapArray(ISameEnum1Interface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(SameEnum1InterfaceParcelable::new)
           .toArray(SameEnum1InterfaceParcelable[]::new);
    }

    public static ISameEnum1Interface[] unwrapArray(SameEnum1InterfaceParcelable[] parcelables) {
        if (parcelables == null) return new ISameEnum1Interface[0];
        return Arrays.stream(parcelables)
           .map(SameEnum1InterfaceParcelable::getSameEnum1Interface)
           .toArray(ISameEnum1Interface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
