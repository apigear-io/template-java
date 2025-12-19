package tbSimple.tbSimple_android_messenger;

import tbSimple.tbSimple_api.INoSignalsInterface;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

  public  class NoSignalsInterfaceParcelable implements Parcelable {

    public INoSignalsInterface data;

    public NoSignalsInterfaceParcelable(INoSignalsInterface data) {
        this.data = data;
    }

    public INoSignalsInterface getNoSignalsInterface()
    {
        return data;
    }

    protected NoSignalsInterfaceParcelable(Parcel in) {
        data.setPropBool(in.readBoolean());
        data.setPropInt(in.readInt());
    }

    public static final Creator<NoSignalsInterfaceParcelable> CREATOR = new Creator<NoSignalsInterfaceParcelable>() {
        @Override
        public NoSignalsInterfaceParcelable createFromParcel(Parcel in) {
            return new NoSignalsInterfaceParcelable(in);
        }

        @Override
        public NoSignalsInterfaceParcelable[] newArray(int size) {
            return new NoSignalsInterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data.getPropBool());
        dest.writeInt(data.getPropInt());


    }
        public static NoSignalsInterfaceParcelable[] wrapArray(INoSignalsInterface[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(NoSignalsInterfaceParcelable::new)
           .toArray(NoSignalsInterfaceParcelable[]::new);
    }

    public static INoSignalsInterface[] unwrapArray(NoSignalsInterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(NoSignalsInterfaceParcelable::getNoSignalsInterface)
           .toArray(INoSignalsInterface[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
