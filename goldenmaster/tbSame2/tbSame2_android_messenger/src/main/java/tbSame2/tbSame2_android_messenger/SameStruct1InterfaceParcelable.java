package tbSame2.tbSame2_android_messenger;

import tbSame2.tbSame2_api.ISameStruct1Interface;
import android.os.Parcel;
import android.os.Parcelable;
import tbSame2.tbSame2_api.Struct1;

  public  class SameStruct1InterfaceParcelable implements Parcelable {

    public ISameStruct1Interface data;

    public SameStruct1InterfaceParcelable(ISameStruct1Interface data) {
        this.data = data;
    }

    public ISameStruct1Interface getSameStruct1Interface()
    {
        return data;
    }

    protected SameStruct1InterfaceParcelable(Parcel in) {
        Struct1Parcelable l_parcelableprop1 = in.readParcelable(Struct1Parcelable.class.getClassLoader(), Struct1Parcelable.class);
        data.setProp1(l_parcelableprop1 != null ? l_parcelableprop1.data : null);
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
        dest.writeParcelable(new Struct1Parcelable(data.getProp1()), flags);


    }
        public static SameStruct1InterfaceParcelable[] wrapArray(ISameStruct1Interface[] elements) {
        if (elements == null) return null;
        SameStruct1InterfaceParcelable[] out = new SameStruct1InterfaceParcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new SameStruct1InterfaceParcelable(elements[i]);
        }
        return out;
    }

    public static ISameStruct1Interface[] unwrapArray(SameStruct1InterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        ISameStruct1Interface[] out = new ISameStruct1Interface[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getSameStruct1Interface();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
