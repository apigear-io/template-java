package testbed2.testbed2_android_messenger;

import testbed2.testbed2_api.INestedStruct2Interface;
import android.os.Parcel;
import android.os.Parcelable;
import testbed2.testbed2_api.NestedStruct1;
import testbed2.testbed2_api.NestedStruct2;

  public  class NestedStruct2InterfaceParcelable implements Parcelable {

    public INestedStruct2Interface data;

    public NestedStruct2InterfaceParcelable(INestedStruct2Interface data) {
        this.data = data;
    }

    public INestedStruct2Interface getNestedStruct2Interface()
    {
        return data;
    }

    protected NestedStruct2InterfaceParcelable(Parcel in) {
        NestedStruct1Parcelable l_parcelableprop1 = in.readParcelable(NestedStruct1Parcelable.class.getClassLoader(), NestedStruct1Parcelable.class);
        data.setProp1(l_parcelableprop1 != null ? l_parcelableprop1.data : null);
        NestedStruct2Parcelable l_parcelableprop2 = in.readParcelable(NestedStruct2Parcelable.class.getClassLoader(), NestedStruct2Parcelable.class);
        data.setProp2(l_parcelableprop2 != null ? l_parcelableprop2.data : null);
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
        dest.writeParcelable(new NestedStruct1Parcelable(data.getProp1()), flags);
        dest.writeParcelable(new NestedStruct2Parcelable(data.getProp2()), flags);


    }
        public static NestedStruct2InterfaceParcelable[] wrapArray(INestedStruct2Interface[] elements) {
        if (elements == null) return null;
        NestedStruct2InterfaceParcelable[] out = new NestedStruct2InterfaceParcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new NestedStruct2InterfaceParcelable(elements[i]);
        }
        return out;
    }

    public static INestedStruct2Interface[] unwrapArray(NestedStruct2InterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        INestedStruct2Interface[] out = new INestedStruct2Interface[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getNestedStruct2Interface();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
