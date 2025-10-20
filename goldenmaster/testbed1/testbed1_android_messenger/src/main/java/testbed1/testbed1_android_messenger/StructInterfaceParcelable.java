package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.IStructInterface;
import android.os.Parcel;
import android.os.Parcelable;
import testbed1.testbed1_api.StructBool;
import testbed1.testbed1_api.StructFloat;
import testbed1.testbed1_api.StructInt;
import testbed1.testbed1_api.StructString;

  public  class StructInterfaceParcelable implements Parcelable {

    public IStructInterface data;

    public StructInterfaceParcelable(IStructInterface data) {
        this.data = data;
    }

    public IStructInterface getStructInterface()
    {
        return data;
    }

    protected StructInterfaceParcelable(Parcel in) {
        StructBoolParcelable l_parcelablepropBool = in.readParcelable(StructBoolParcelable.class.getClassLoader(), StructBoolParcelable.class);
        data.setPropBool(l_parcelablepropBool != null ? l_parcelablepropBool.data : null);
        StructIntParcelable l_parcelablepropInt = in.readParcelable(StructIntParcelable.class.getClassLoader(), StructIntParcelable.class);
        data.setPropInt(l_parcelablepropInt != null ? l_parcelablepropInt.data : null);
        StructFloatParcelable l_parcelablepropFloat = in.readParcelable(StructFloatParcelable.class.getClassLoader(), StructFloatParcelable.class);
        data.setPropFloat(l_parcelablepropFloat != null ? l_parcelablepropFloat.data : null);
        StructStringParcelable l_parcelablepropString = in.readParcelable(StructStringParcelable.class.getClassLoader(), StructStringParcelable.class);
        data.setPropString(l_parcelablepropString != null ? l_parcelablepropString.data : null);
    }

    public static final Creator<StructInterfaceParcelable> CREATOR = new Creator<StructInterfaceParcelable>() {
        @Override
        public StructInterfaceParcelable createFromParcel(Parcel in) {
            return new StructInterfaceParcelable(in);
        }

        @Override
        public StructInterfaceParcelable[] newArray(int size) {
            return new StructInterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(new StructBoolParcelable(data.getPropBool()), flags);
        dest.writeParcelable(new StructIntParcelable(data.getPropInt()), flags);
        dest.writeParcelable(new StructFloatParcelable(data.getPropFloat()), flags);
        dest.writeParcelable(new StructStringParcelable(data.getPropString()), flags);


    }
        public static StructInterfaceParcelable[] wrapArray(IStructInterface[] elements) {
        if (elements == null) return null;
        StructInterfaceParcelable[] out = new StructInterfaceParcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new StructInterfaceParcelable(elements[i]);
        }
        return out;
    }

    public static IStructInterface[] unwrapArray(StructInterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        IStructInterface[] out = new IStructInterface[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getStructInterface();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
