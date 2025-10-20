package tbSimple.tbSimple_android_messenger;

import tbSimple.tbSimple_api.IEmptyInterface;
import android.os.Parcel;
import android.os.Parcelable;

  public  class EmptyInterfaceParcelable implements Parcelable {

    public IEmptyInterface data;

    public EmptyInterfaceParcelable(IEmptyInterface data) {
        this.data = data;
    }

    public IEmptyInterface getEmptyInterface()
    {
        return data;
    }

    protected EmptyInterfaceParcelable(Parcel in) {
    }

    public static final Creator<EmptyInterfaceParcelable> CREATOR = new Creator<EmptyInterfaceParcelable>() {
        @Override
        public EmptyInterfaceParcelable createFromParcel(Parcel in) {
            return new EmptyInterfaceParcelable(in);
        }

        @Override
        public EmptyInterfaceParcelable[] newArray(int size) {
            return new EmptyInterfaceParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {


    }
        public static EmptyInterfaceParcelable[] wrapArray(IEmptyInterface[] elements) {
        if (elements == null) return null;
        EmptyInterfaceParcelable[] out = new EmptyInterfaceParcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new EmptyInterfaceParcelable(elements[i]);
        }
        return out;
    }

    public static IEmptyInterface[] unwrapArray(EmptyInterfaceParcelable[] parcelables) {
        if (parcelables == null) return null;
        IEmptyInterface[] out = new IEmptyInterface[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getEmptyInterface();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
