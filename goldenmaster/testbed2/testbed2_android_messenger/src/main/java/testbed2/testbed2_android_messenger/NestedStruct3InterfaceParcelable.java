package testbed2.testbed2_android_messenger;

import testbed2.testbed2_api.INestedStruct3Interface;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_api.NestedStruct2;
import testbed2.testbed2_api.NestedStruct3;

  public  class NestedStruct3InterfaceParcelable implements Parcelable {

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
        NestedStruct1Parcelable l_parcelableprop1 = in.readParcelable(NestedStruct1Parcelable.class.getClassLoader(), NestedStruct1Parcelable.class);
        data.setProp1(l_parcelableprop1 != null ? l_parcelableprop1.data : null);
        NestedStruct2Parcelable l_parcelableprop2 = in.readParcelable(NestedStruct2Parcelable.class.getClassLoader(), NestedStruct2Parcelable.class);
        data.setProp2(l_parcelableprop2 != null ? l_parcelableprop2.data : null);
        NestedStruct3Parcelable l_parcelableprop3 = in.readParcelable(NestedStruct3Parcelable.class.getClassLoader(), NestedStruct3Parcelable.class);
        data.setProp3(l_parcelableprop3 != null ? l_parcelableprop3.data : null);
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
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(NestedStruct3InterfaceParcelable::getNestedStruct3Interface)
           .toArray(INestedStruct3Interface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
