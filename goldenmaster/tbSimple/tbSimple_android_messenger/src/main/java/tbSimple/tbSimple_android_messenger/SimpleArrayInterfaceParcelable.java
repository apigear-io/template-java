package tbSimple.tbSimple_android_messenger;

import tbSimple.tbSimple_api.ISimpleArrayInterface;
import android.os.Parcel;
import android.os.Parcelable;

  public  class SimpleArrayInterfaceParcelable implements Parcelable {

    public ISimpleArrayInterface data;

    public SimpleArrayInterfaceParcelable(ISimpleArrayInterface data) {
        this.data = data;
    }

    public ISimpleArrayInterface getSimpleArrayInterface()
    {
        return data;
    }

    protected SimpleArrayInterfaceParcelable(Parcel in) {
        data.setPropBool(in.createBooleanArray());
        data.setPropInt(in.createIntArray());
        data.setPropInt32(in.createIntArray());
        data.setPropInt64(in.createLongArray());
        data.setPropFloat(in.createFloatArray());
        data.setPropFloat32(in.createFloatArray());
        data.setPropFloat64(in.createDoubleArray());
        data.setPropString(in.createStringArray());
        data.setPropReadOnlyString(in.readString());
    }

    public static final Creator<SimpleArrayInterfaceParcelable> CREATOR = new Creator<SimpleArrayInterfaceParcelable>() {
        @Override
        public SimpleArrayInterfaceParcelable createFromParcel(Parcel in) {
            return new SimpleArrayInterfaceParcelable(in);
        }

        @Override
        public SimpleArrayInterfaceParcelable[] newArray(int size) {
            return new SimpleArrayInterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBooleanArray(data.getPropBool());
        dest.writeIntArray(data.getPropInt());
        dest.writeIntArray(data.getPropInt32());
        dest.writeLongArray(data.getPropInt64());
        dest.writeFloatArray(data.getPropFloat());
        dest.writeFloatArray(data.getPropFloat32());
        dest.writeDoubleArray(data.getPropFloat64());
        dest.writeStringArray(data.getPropString());
        dest.writeString(data.getPropReadOnlyString());


    }
        public static SimpleArrayInterfaceParcelable[] wrapArray(ISimpleArrayInterface[] elements) {
        if (elements == null) return null;
        SimpleArrayInterfaceParcelable[] out = new SimpleArrayInterfaceParcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new SimpleArrayInterfaceParcelable(elements[i]);
        }
        return out;
    }

    public static ISimpleArrayInterface[] unwrapArray(SimpleArrayInterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        ISimpleArrayInterface[] out = new ISimpleArrayInterface[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getSimpleArrayInterface();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
