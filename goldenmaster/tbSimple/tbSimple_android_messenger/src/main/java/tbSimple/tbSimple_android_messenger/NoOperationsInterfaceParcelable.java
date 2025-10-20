package tbSimple.tbSimple_android_messenger;

import tbSimple.tbSimple_api.INoOperationsInterface;
import android.os.Parcel;
import android.os.Parcelable;

  public  class NoOperationsInterfaceParcelable implements Parcelable {

    public INoOperationsInterface data;

    public NoOperationsInterfaceParcelable(INoOperationsInterface data) {
        this.data = data;
    }

    public INoOperationsInterface getNoOperationsInterface()
    {
        return data;
    }

    protected NoOperationsInterfaceParcelable(Parcel in) {
        data.setPropBool(in.readBoolean());
        data.setPropInt(in.readInt());
    }

    public static final Creator<NoOperationsInterfaceParcelable> CREATOR = new Creator<NoOperationsInterfaceParcelable>() {
        @Override
        public NoOperationsInterfaceParcelable createFromParcel(Parcel in) {
            return new NoOperationsInterfaceParcelable(in);
        }

        @Override
        public NoOperationsInterfaceParcelable[] newArray(int size) {
            return new NoOperationsInterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data.getPropBool());
        dest.writeInt(data.getPropInt());


    }
        public static NoOperationsInterfaceParcelable[] wrapArray(INoOperationsInterface[] elements) {
        if (elements == null) return null;
        NoOperationsInterfaceParcelable[] out = new NoOperationsInterfaceParcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new NoOperationsInterfaceParcelable(elements[i]);
        }
        return out;
    }

    public static INoOperationsInterface[] unwrapArray(NoOperationsInterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        INoOperationsInterface[] out = new INoOperationsInterface[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getNoOperationsInterface();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
