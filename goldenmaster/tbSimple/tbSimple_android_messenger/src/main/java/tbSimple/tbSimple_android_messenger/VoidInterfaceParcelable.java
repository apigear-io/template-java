package tbSimple.tbSimple_android_messenger;

import tbSimple.tbSimple_api.IVoidInterface;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

  public  class VoidInterfaceParcelable implements Parcelable {

    public IVoidInterface data;

    public VoidInterfaceParcelable(IVoidInterface data) {
        this.data = data;
    }

    public IVoidInterface getVoidInterface()
    {
        return data;
    }

    protected VoidInterfaceParcelable(Parcel in) {
    }

    public static final Creator<VoidInterfaceParcelable> CREATOR = new Creator<VoidInterfaceParcelable>() {
        @Override
        public VoidInterfaceParcelable createFromParcel(Parcel in) {
            return new VoidInterfaceParcelable(in);
        }

        @Override
        public VoidInterfaceParcelable[] newArray(int size) {
            return new VoidInterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {


    }
        public static VoidInterfaceParcelable[] wrapArray(IVoidInterface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(VoidInterfaceParcelable::new)
           .toArray(VoidInterfaceParcelable[]::new);
    }

    public static IVoidInterface[] unwrapArray(VoidInterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(VoidInterfaceParcelable::getVoidInterface)
           .toArray(IVoidInterface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
