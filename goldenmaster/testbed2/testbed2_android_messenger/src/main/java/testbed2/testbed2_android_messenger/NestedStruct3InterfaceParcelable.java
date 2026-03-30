package testbed2.testbed2_android_messenger;

import testbed2.testbed2_api.INestedStruct3Interface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_api.NestedStruct2;
import testbed2.testbed2_api.NestedStruct3;

  public  class NestedStruct3InterfaceParcelable implements Parcelable {

    private static final String TAG = "NestedStruct3InterfaceParcelable";

    public INestedStruct3Interface data;

    public NestedStruct3InterfaceParcelable(INestedStruct3Interface data) {
        this.data = data;
    }

    public INestedStruct3Interface getNestedStruct3Interface()
    {
        return data;
    }

    protected NestedStruct3InterfaceParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
    }

    public static final Creator<NestedStruct3InterfaceParcelable> CREATOR = new Creator<NestedStruct3InterfaceParcelable>() {
        @Override
        public NestedStruct3InterfaceParcelable createFromParcel(Parcel in) {
            return new NestedStruct3InterfaceParcelable(in);
        }

        @Override
        public NestedStruct3InterfaceParcelable[] newArray(int size) {
            return new NestedStruct3InterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
        dest.writeParcelable(new NestedStruct1Parcelable(data.getProp1()), flags);
        dest.writeParcelable(new NestedStruct2Parcelable(data.getProp2()), flags);
        dest.writeParcelable(new NestedStruct3Parcelable(data.getProp3()), flags);


    }
        public static NestedStruct3InterfaceParcelable[] wrapArray(INestedStruct3Interface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(NestedStruct3InterfaceParcelable::new)
           .toArray(NestedStruct3InterfaceParcelable[]::new);
    }

    public static INestedStruct3Interface[] unwrapArray(NestedStruct3InterfaceParcelable[] parcelables) {
        if (parcelables == null) return new INestedStruct3Interface[0];
        return Arrays.stream(parcelables)
           .map(NestedStruct3InterfaceParcelable::getNestedStruct3Interface)
           .toArray(INestedStruct3Interface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
