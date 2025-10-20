package tbSame1.tbSame1_android_messenger;

import tbSame1.tbSame1_api.ISameStruct2Interface;
import android.os.Parcel;
import android.os.Parcelable;
import tbSame1.tbSame1_api.Struct1;
import tbSame1.tbSame1_api.Struct2;

  public  class SameStruct2InterfaceParcelable implements Parcelable {

    public ISameStruct2Interface data;

    public SameStruct2InterfaceParcelable(ISameStruct2Interface data) {
        this.data = data;
    }

    public ISameStruct2Interface getSameStruct2Interface()
    {
        return data;
    }

    protected SameStruct2InterfaceParcelable(Parcel in) {
        Struct2Parcelable l_parcelableprop1 = in.readParcelable(Struct2Parcelable.class.getClassLoader(), Struct2Parcelable.class);
        data.setProp1(l_parcelableprop1 != null ? l_parcelableprop1.data : null);
        Struct2Parcelable l_parcelableprop2 = in.readParcelable(Struct2Parcelable.class.getClassLoader(), Struct2Parcelable.class);
        data.setProp2(l_parcelableprop2 != null ? l_parcelableprop2.data : null);
    }

    public static final Creator<SameStruct2InterfaceParcelable> CREATOR = new Creator<SameStruct2InterfaceParcelable>() {
        @Override
        public SameStruct2InterfaceParcelable createFromParcel(Parcel in) {
            return new SameStruct2InterfaceParcelable(in);
        }

        @Override
        public SameStruct2InterfaceParcelable[] newArray(int size) {
            return new SameStruct2InterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(new Struct2Parcelable(data.getProp1()), flags);
        dest.writeParcelable(new Struct2Parcelable(data.getProp2()), flags);


    }
        public static SameStruct2InterfaceParcelable[] wrapArray(ISameStruct2Interface[] elements) {
        if (elements == null) return null;
        SameStruct2InterfaceParcelable[] out = new SameStruct2InterfaceParcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new SameStruct2InterfaceParcelable(elements[i]);
        }
        return out;
    }

    public static ISameStruct2Interface[] unwrapArray(SameStruct2InterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        ISameStruct2Interface[] out = new ISameStruct2Interface[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getSameStruct2Interface();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
