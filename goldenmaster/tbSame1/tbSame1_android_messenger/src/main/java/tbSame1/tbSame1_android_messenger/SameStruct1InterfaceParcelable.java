package tbSame1.tbSame1_android_messenger;

import tbSame1.tbSame1_api.ISameStruct1Interface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Arrays;
import tbSame1.tbSame1_api.Struct1;

  public  class SameStruct1InterfaceParcelable implements Parcelable {

    private static final String TAG = "SameStruct1InterfaceParcelable";

    public ISameStruct1Interface data;

    public SameStruct1InterfaceParcelable(ISameStruct1Interface data) {
        this.data = data;
    }

    public ISameStruct1Interface getSameStruct1Interface()
    {
        return data;
    }

    protected SameStruct1InterfaceParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
    }

    public static final Creator<SameStruct1InterfaceParcelable> CREATOR = new Creator<SameStruct1InterfaceParcelable>() {
        @Override
        public SameStruct1InterfaceParcelable createFromParcel(Parcel in) {
            return new SameStruct1InterfaceParcelable(in);
        }

        @Override
        public SameStruct1InterfaceParcelable[] newArray(int size) {
            return new SameStruct1InterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
        dest.writeParcelable(new Struct1Parcelable(data.getProp1()), flags);


    }
        public static SameStruct1InterfaceParcelable[] wrapArray(ISameStruct1Interface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(SameStruct1InterfaceParcelable::new)
           .toArray(SameStruct1InterfaceParcelable[]::new);
    }

    public static ISameStruct1Interface[] unwrapArray(SameStruct1InterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(SameStruct1InterfaceParcelable::getSameStruct1Interface)
           .toArray(ISameStruct1Interface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
