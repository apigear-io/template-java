package tbSame1.tbSame1_android_messenger;

import tbSame1.tbSame1_api.ISameEnum1Interface;
import android.os.Parcel;
import android.os.Parcelable;
import tbSame1.tbSame1_api.Enum1;

  public  class SameEnum1InterfaceParcelable implements Parcelable {

    public ISameEnum1Interface data;

    public SameEnum1InterfaceParcelable(ISameEnum1Interface data) {
        this.data = data;
    }

    public ISameEnum1Interface getSameEnum1Interface()
    {
        return data;
    }

    protected SameEnum1InterfaceParcelable(Parcel in) {
        Enum1Parcelable l_parcelableprop1 = in.readParcelable(Enum1Parcelable.class.getClassLoader(), Enum1Parcelable.class);
        data.setProp1(l_parcelableprop1 != null ? l_parcelableprop1.data : null);
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
        dest.writeParcelable(new Enum1Parcelable(data.getProp1()), flags);


    }
        public static SameEnum1InterfaceParcelable[] wrapArray(ISameEnum1Interface[] elements) {
        if (elements == null) return null;
        SameEnum1InterfaceParcelable[] out = new SameEnum1InterfaceParcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new SameEnum1InterfaceParcelable(elements[i]);
        }
        return out;
    }

    public static ISameEnum1Interface[] unwrapArray(SameEnum1InterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        ISameEnum1Interface[] out = new ISameEnum1Interface[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getSameEnum1Interface();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
