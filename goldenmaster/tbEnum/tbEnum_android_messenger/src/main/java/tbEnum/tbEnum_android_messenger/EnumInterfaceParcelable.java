package tbEnum.tbEnum_android_messenger;

import tbEnum.tbEnum_api.IEnumInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import tbEnum.tbEnum_api.Enum0;
import tbEnum.tbEnum_api.Enum1;
import tbEnum.tbEnum_api.Enum2;
import tbEnum.tbEnum_api.Enum3;

  public  class EnumInterfaceParcelable implements Parcelable {

    private static final String TAG = "EnumInterfaceParcelable";

    public IEnumInterface data;

    public EnumInterfaceParcelable(IEnumInterface data) {
        this.data = data;
    }

    public IEnumInterface getEnumInterface()
    {
        return data;
    }

    protected EnumInterfaceParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
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
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
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
        if (parcelables == null) return new IEnumInterface[0];
        return Arrays.stream(parcelables)
           .map(EnumInterfaceParcelable::getEnumInterface)
           .toArray(IEnumInterface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
