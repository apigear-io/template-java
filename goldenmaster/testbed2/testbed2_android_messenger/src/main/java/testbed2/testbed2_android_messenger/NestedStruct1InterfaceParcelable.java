package testbed2.testbed2_android_messenger;

import testbed2.testbed2_api.INestedStruct1Interface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import testbed2.testbed2_api.NestedStruct1;

  public  class NestedStruct1InterfaceParcelable implements Parcelable {

    private static final String TAG = "NestedStruct1InterfaceParcelable";

    public INestedStruct1Interface data;

    public NestedStruct1InterfaceParcelable(INestedStruct1Interface data) {
        this.data = data;
    }

    public INestedStruct1Interface getNestedStruct1Interface()
    {
        return data;
    }

    protected NestedStruct1InterfaceParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
    }

    public static final Creator<NestedStruct1InterfaceParcelable> CREATOR = new Creator<NestedStruct1InterfaceParcelable>() {
        @Override
        public NestedStruct1InterfaceParcelable createFromParcel(Parcel in) {
            return new NestedStruct1InterfaceParcelable(in);
        }

        @Override
        public NestedStruct1InterfaceParcelable[] newArray(int size) {
            return new NestedStruct1InterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
        dest.writeParcelable(new NestedStruct1Parcelable(data.getProp1()), flags);


    }
        public static NestedStruct1InterfaceParcelable[] wrapArray(INestedStruct1Interface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(NestedStruct1InterfaceParcelable::new)
           .toArray(NestedStruct1InterfaceParcelable[]::new);
    }

    public static INestedStruct1Interface[] unwrapArray(NestedStruct1InterfaceParcelable[] parcelables) {
        if (parcelables == null) return new INestedStruct1Interface[0];
        return Arrays.stream(parcelables)
           .map(NestedStruct1InterfaceParcelable::getNestedStruct1Interface)
           .toArray(INestedStruct1Interface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
