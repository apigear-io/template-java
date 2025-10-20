package tbSimple.tbSimple_android_messenger;

import tbSimple.tbSimple_api.ISimpleInterface;
import android.os.Parcel;
import android.os.Parcelable;

  public  class SimpleInterfaceParcelable implements Parcelable {

    public ISimpleInterface data;

    public SimpleInterfaceParcelable(ISimpleInterface data) {
        this.data = data;
    }

    public ISimpleInterface getSimpleInterface()
    {
        return data;
    }

    protected SimpleInterfaceParcelable(Parcel in) {
        data.setPropBool(in.readBoolean());
        data.setPropInt(in.readInt());
        data.setPropInt32(in.readInt());
        data.setPropInt64(in.readLong());
        data.setPropFloat(in.readFloat());
        data.setPropFloat32(in.readFloat());
        data.setPropFloat64(in.readDouble());
        data.setPropString(in.readString());
    }

    public static final Creator<SimpleInterfaceParcelable> CREATOR = new Creator<SimpleInterfaceParcelable>() {
        @Override
        public SimpleInterfaceParcelable createFromParcel(Parcel in) {
            return new SimpleInterfaceParcelable(in);
        }

        @Override
        public SimpleInterfaceParcelable[] newArray(int size) {
            return new SimpleInterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data.getPropBool());
        dest.writeInt(data.getPropInt());
        dest.writeInt(data.getPropInt32());
        dest.writeLong(data.getPropInt64());
        dest.writeFloat(data.getPropFloat());
        dest.writeFloat(data.getPropFloat32());
        dest.writeDouble(data.getPropFloat64());
        dest.writeString(data.getPropString());


    }
        public static SimpleInterfaceParcelable[] wrapArray(ISimpleInterface[] elements) {
        if (elements == null) return null;
        SimpleInterfaceParcelable[] out = new SimpleInterfaceParcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new SimpleInterfaceParcelable(elements[i]);
        }
        return out;
    }

    public static ISimpleInterface[] unwrapArray(SimpleInterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        ISimpleInterface[] out = new ISimpleInterface[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getSimpleInterface();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
