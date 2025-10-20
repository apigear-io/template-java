package tbSimple.tbSimple_android_messenger;

import tbSimple.tbSimple_api.IVoidInterface;
import android.os.Parcel;
import android.os.Parcelable;

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
        VoidInterfaceParcelable[] out = new VoidInterfaceParcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new VoidInterfaceParcelable(elements[i]);
        }
        return out;
    }

    public static IVoidInterface[] unwrapArray(VoidInterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        IVoidInterface[] out = new IVoidInterface[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getVoidInterface();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
