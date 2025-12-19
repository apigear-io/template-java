package tbSimple.tbSimple_android_messenger;

import tbSimple.tbSimple_api.INoPropertiesInterface;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

  public  class NoPropertiesInterfaceParcelable implements Parcelable {

    public INoPropertiesInterface data;

    public NoPropertiesInterfaceParcelable(INoPropertiesInterface data) {
        this.data = data;
    }

    public INoPropertiesInterface getNoPropertiesInterface()
    {
        return data;
    }

    protected NoPropertiesInterfaceParcelable(Parcel in) {
    }

    public static final Creator<NoPropertiesInterfaceParcelable> CREATOR = new Creator<NoPropertiesInterfaceParcelable>() {
        @Override
        public NoPropertiesInterfaceParcelable createFromParcel(Parcel in) {
            return new NoPropertiesInterfaceParcelable(in);
        }

        @Override
        public NoPropertiesInterfaceParcelable[] newArray(int size) {
            return new NoPropertiesInterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {


    }
        public static NoPropertiesInterfaceParcelable[] wrapArray(INoPropertiesInterface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(NoPropertiesInterfaceParcelable::new)
           .toArray(NoPropertiesInterfaceParcelable[]::new);
    }

    public static INoPropertiesInterface[] unwrapArray(NoPropertiesInterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(NoPropertiesInterfaceParcelable::getNoPropertiesInterface)
           .toArray(INoPropertiesInterface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
