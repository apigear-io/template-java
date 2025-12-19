package tbSame1.tbSame1_android_messenger;

import tbSame1.tbSame1_api.ISameStruct2Interface;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
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
        return Arrays.stream(elements)
           .map(SameStruct2InterfaceParcelable::new)
           .toArray(SameStruct2InterfaceParcelable[]::new);
    }

    public static ISameStruct2Interface[] unwrapArray(SameStruct2InterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(SameStruct2InterfaceParcelable::getSameStruct2Interface)
           .toArray(ISameStruct2Interface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
