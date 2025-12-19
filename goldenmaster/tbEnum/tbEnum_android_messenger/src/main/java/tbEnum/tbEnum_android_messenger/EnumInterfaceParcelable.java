package tbEnum.tbEnum_android_messenger;

import tbEnum.tbEnum_api.IEnumInterface;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import tbEnum.tbEnum_api.Enum0;
import tbEnum.tbEnum_api.Enum1;
import tbEnum.tbEnum_api.Enum2;
import tbEnum.tbEnum_api.Enum3;

  public  class EnumInterfaceParcelable implements Parcelable {

    public IEnumInterface data;

    public EnumInterfaceParcelable(IEnumInterface data) {
        this.data = data;
    }

    public IEnumInterface getEnumInterface()
    {
        return data;
    }

    protected EnumInterfaceParcelable(Parcel in) {
        Enum0Parcelable l_parcelableprop0 = in.readParcelable(Enum0Parcelable.class.getClassLoader(), Enum0Parcelable.class);
        data.setProp0(l_parcelableprop0 != null ? l_parcelableprop0.data : null);
        Enum1Parcelable l_parcelableprop1 = in.readParcelable(Enum1Parcelable.class.getClassLoader(), Enum1Parcelable.class);
        data.setProp1(l_parcelableprop1 != null ? l_parcelableprop1.data : null);
        Enum2Parcelable l_parcelableprop2 = in.readParcelable(Enum2Parcelable.class.getClassLoader(), Enum2Parcelable.class);
        data.setProp2(l_parcelableprop2 != null ? l_parcelableprop2.data : null);
        Enum3Parcelable l_parcelableprop3 = in.readParcelable(Enum3Parcelable.class.getClassLoader(), Enum3Parcelable.class);
        data.setProp3(l_parcelableprop3 != null ? l_parcelableprop3.data : null);
    }

    public static final Creator<EnumInterfaceParcelable> CREATOR = new Creator<EnumInterfaceParcelable>() {
        @Override
        public EnumInterfaceParcelable createFromParcel(Parcel in) {
            return new EnumInterfaceParcelable(in);
        }

        @Override
        public EnumInterfaceParcelable[] newArray(int size) {
            return new EnumInterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(new Enum0Parcelable(data.getProp0()), flags);
        dest.writeParcelable(new Enum1Parcelable(data.getProp1()), flags);
        dest.writeParcelable(new Enum2Parcelable(data.getProp2()), flags);
        dest.writeParcelable(new Enum3Parcelable(data.getProp3()), flags);


    }
        public static EnumInterfaceParcelable[] wrapArray(IEnumInterface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(EnumInterfaceParcelable::new)
           .toArray(EnumInterfaceParcelable[]::new);
    }

    public static IEnumInterface[] unwrapArray(EnumInterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(EnumInterfaceParcelable::getEnumInterface)
           .toArray(IEnumInterface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
