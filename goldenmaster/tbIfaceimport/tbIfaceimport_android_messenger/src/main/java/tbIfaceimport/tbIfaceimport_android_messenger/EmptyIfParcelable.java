package tbIfaceimport.tbIfaceimport_android_messenger;

import tbIfaceimport.tbIfaceimport_api.IEmptyIf;
import android.os.Parcel;
import android.os.Parcelable;

  public  class EmptyIfParcelable implements Parcelable {

    public IEmptyIf data;

    public EmptyIfParcelable(IEmptyIf data) {
        this.data = data;
    }

    public IEmptyIf getEmptyIf()
    {
        return data;
    }

    protected EmptyIfParcelable(Parcel in) {
    }

    public static final Creator<EmptyIfParcelable> CREATOR = new Creator<EmptyIfParcelable>() {
        @Override
        public EmptyIfParcelable createFromParcel(Parcel in) {
            return new EmptyIfParcelable(in);
        }

        @Override
        public EmptyIfParcelable[] newArray(int size) {
            return new EmptyIfParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {


    }
        public static EmptyIfParcelable[] wrapArray(IEmptyIf[] elements) {
        if (elements == null) return null;
        EmptyIfParcelable[] out = new EmptyIfParcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new EmptyIfParcelable(elements[i]);
        }
        return out;
    }

    public static IEmptyIf[] unwrapArray(EmptyIfParcelable[] parcelables) {
        if (parcelables == null) return null;
        IEmptyIf[] out = new IEmptyIf[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getEmptyIf();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
