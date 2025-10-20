package tbRefIfaces.tbRefIfaces_android_messenger;

import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;
import android.os.Parcel;
import android.os.Parcelable;

  public  class SimpleLocalIfParcelable implements Parcelable {

    public ISimpleLocalIf data;

    public SimpleLocalIfParcelable(ISimpleLocalIf data) {
        this.data = data;
    }

    public ISimpleLocalIf getSimpleLocalIf()
    {
        return data;
    }

    protected SimpleLocalIfParcelable(Parcel in) {
        data.setIntProperty(in.readInt());
    }

    public static final Creator<SimpleLocalIfParcelable> CREATOR = new Creator<SimpleLocalIfParcelable>() {
        @Override
        public SimpleLocalIfParcelable createFromParcel(Parcel in) {
            return new SimpleLocalIfParcelable(in);
        }

        @Override
        public SimpleLocalIfParcelable[] newArray(int size) {
            return new SimpleLocalIfParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(data.getIntProperty());


    }
        public static SimpleLocalIfParcelable[] wrapArray(ISimpleLocalIf[] elements) {
        if (elements == null) return null;
        SimpleLocalIfParcelable[] out = new SimpleLocalIfParcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new SimpleLocalIfParcelable(elements[i]);
        }
        return out;
    }

    public static ISimpleLocalIf[] unwrapArray(SimpleLocalIfParcelable[] parcelables) {
        if (parcelables == null) return null;
        ISimpleLocalIf[] out = new ISimpleLocalIf[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getSimpleLocalIf();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
