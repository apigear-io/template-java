package tbSame1.tbSame1_android_messenger;

import tbSame1.tbSame1_api.ISameEnum2Interface;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import tbSame1.tbSame1_api.Enum1;
import tbSame1.tbSame1_api.Enum2;

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
        return Arrays.stream(elements)
           .map(SameEnum2InterfaceParcelable::new)
           .toArray(SameEnum2InterfaceParcelable[]::new);
    }

    public static ISameEnum2Interface[] unwrapArray(SameEnum2InterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(SameEnum2InterfaceParcelable::getSameEnum2Interface)
           .toArray(ISameEnum2Interface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
