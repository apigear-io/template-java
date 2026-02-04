package tbSimple.tbSimple_android_messenger;

import tbSimple.tbSimple_api.INoOperationsInterface;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

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
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }
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
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
        dest.writeBoolean(data.getPropBool());
        dest.writeInt(data.getPropInt());


    }
        public static NoOperationsInterfaceParcelable[] wrapArray(INoOperationsInterface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(NoOperationsInterfaceParcelable::new)
           .toArray(NoOperationsInterfaceParcelable[]::new);
    }

    public static INoOperationsInterface[] unwrapArray(NoOperationsInterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(NoOperationsInterfaceParcelable::getNoOperationsInterface)
           .toArray(INoOperationsInterface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
