package tbSame2.tbSame2_android_messenger;

import tbSame2.tbSame2_api.ISameEnum1Interface;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import tbSame2.tbSame2_api.Enum1;

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
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }
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
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
        dest.writeParcelable(new Enum1Parcelable(data.getProp1()), flags);


    }
        public static SameEnum1InterfaceParcelable[] wrapArray(ISameEnum1Interface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(SameEnum1InterfaceParcelable::new)
           .toArray(SameEnum1InterfaceParcelable[]::new);
    }

    public static ISameEnum1Interface[] unwrapArray(SameEnum1InterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(SameEnum1InterfaceParcelable::getSameEnum1Interface)
           .toArray(ISameEnum1Interface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
