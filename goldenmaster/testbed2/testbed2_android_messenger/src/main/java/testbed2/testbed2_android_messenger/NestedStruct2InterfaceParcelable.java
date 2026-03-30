package testbed2.testbed2_android_messenger;

import testbed2.testbed2_api.INestedStruct2Interface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_api.NestedStruct2;

  public  class NestedStruct2InterfaceParcelable implements Parcelable {

    private static final String TAG = "NestedStruct2InterfaceParcelable";

    public INestedStruct2Interface data;

    public NestedStruct2InterfaceParcelable(INestedStruct2Interface data) {
        this.data = data;
    }

    public INestedStruct2Interface getNestedStruct2Interface()
    {
        return data;
    }

    protected NestedStruct2InterfaceParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
    }

    public static final Creator<NestedStruct2InterfaceParcelable> CREATOR = new Creator<NestedStruct2InterfaceParcelable>() {
        @Override
        public NestedStruct2InterfaceParcelable createFromParcel(Parcel in) {
            return new NestedStruct2InterfaceParcelable(in);
        }

        @Override
        public NestedStruct2InterfaceParcelable[] newArray(int size) {
            return new NestedStruct2InterfaceParcelable[size];
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


    }
        public static NestedStruct2InterfaceParcelable[] wrapArray(INestedStruct2Interface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(NestedStruct2InterfaceParcelable::new)
           .toArray(NestedStruct2InterfaceParcelable[]::new);
    }

    public static INestedStruct2Interface[] unwrapArray(NestedStruct2InterfaceParcelable[] parcelables) {
        if (parcelables == null) return new INestedStruct2Interface[0];
        return Arrays.stream(parcelables)
           .map(NestedStruct2InterfaceParcelable::getNestedStruct2Interface)
           .toArray(INestedStruct2Interface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
