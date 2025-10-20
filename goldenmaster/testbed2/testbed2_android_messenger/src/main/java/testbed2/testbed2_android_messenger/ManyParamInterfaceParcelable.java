package testbed2.testbed2_android_messenger;

import testbed2.testbed2_api.IManyParamInterface;
import android.os.Parcel;
import android.os.Parcelable;

  public  class ManyParamInterfaceParcelable implements Parcelable {

    public IManyParamInterface data;

    public ManyParamInterfaceParcelable(IManyParamInterface data) {
        this.data = data;
    }

    public IManyParamInterface getManyParamInterface()
    {
        return data;
    }

    protected ManyParamInterfaceParcelable(Parcel in) {
        data.setProp1(in.readInt());
        data.setProp2(in.readInt());
        data.setProp3(in.readInt());
        data.setProp4(in.readInt());
    }

    public static final Creator<ManyParamInterfaceParcelable> CREATOR = new Creator<ManyParamInterfaceParcelable>() {
        @Override
        public ManyParamInterfaceParcelable createFromParcel(Parcel in) {
            return new ManyParamInterfaceParcelable(in);
        }

        @Override
        public ManyParamInterfaceParcelable[] newArray(int size) {
            return new ManyParamInterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(data.getProp1());
        dest.writeInt(data.getProp2());
        dest.writeInt(data.getProp3());
        dest.writeInt(data.getProp4());


    }
        public static ManyParamInterfaceParcelable[] wrapArray(IManyParamInterface[] elements) {
        if (elements == null) return null;
        ManyParamInterfaceParcelable[] out = new ManyParamInterfaceParcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new ManyParamInterfaceParcelable(elements[i]);
        }
        return out;
    }

    public static IManyParamInterface[] unwrapArray(ManyParamInterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        IManyParamInterface[] out = new IManyParamInterface[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getManyParamInterface();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
