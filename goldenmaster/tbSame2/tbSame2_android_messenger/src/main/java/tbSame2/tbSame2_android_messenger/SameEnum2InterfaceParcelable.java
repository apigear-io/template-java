package tbSame2.tbSame2_android_messenger;

import tbSame2.tbSame2_api.ISameEnum2Interface;
import android.os.Parcel;
import android.os.Parcelable;
import tbSame2.tbSame2_api.Enum1;
import tbSame2.tbSame2_api.Enum2;

  public  class SameEnum2InterfaceParcelable implements Parcelable {

    public ISameEnum2Interface data;

    public SameEnum2InterfaceParcelable(ISameEnum2Interface data) {
        this.data = data;
    }

    public ISameEnum2Interface getSameEnum2Interface()
    {
        return data;
    }

    protected SameEnum2InterfaceParcelable(Parcel in) {
        Enum1Parcelable l_parcelableprop1 = in.readParcelable(Enum1Parcelable.class.getClassLoader(), Enum1Parcelable.class);
        data.setProp1(l_parcelableprop1 != null ? l_parcelableprop1.data : null);
        Enum2Parcelable l_parcelableprop2 = in.readParcelable(Enum2Parcelable.class.getClassLoader(), Enum2Parcelable.class);
        data.setProp2(l_parcelableprop2 != null ? l_parcelableprop2.data : null);
    }

    public static final Creator<SameEnum2InterfaceParcelable> CREATOR = new Creator<SameEnum2InterfaceParcelable>() {
        @Override
        public SameEnum2InterfaceParcelable createFromParcel(Parcel in) {
            return new SameEnum2InterfaceParcelable(in);
        }

        @Override
        public SameEnum2InterfaceParcelable[] newArray(int size) {
            return new SameEnum2InterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(new Enum1Parcelable(data.getProp1()), flags);
        dest.writeParcelable(new Enum2Parcelable(data.getProp2()), flags);


    }
        public static SameEnum2InterfaceParcelable[] wrapArray(ISameEnum2Interface[] elements) {
        if (elements == null) return null;
        SameEnum2InterfaceParcelable[] out = new SameEnum2InterfaceParcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new SameEnum2InterfaceParcelable(elements[i]);
        }
        return out;
    }

    public static ISameEnum2Interface[] unwrapArray(SameEnum2InterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        ISameEnum2Interface[] out = new ISameEnum2Interface[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getSameEnum2Interface();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
