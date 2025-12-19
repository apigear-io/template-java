package tbIfaceimport.tbIfaceimport_android_messenger;

import tbIfaceimport.tbIfaceimport_api.IEmptyIf;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

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
        return Arrays.stream(elements)
           .map(EmptyIfParcelable::new)
           .toArray(EmptyIfParcelable[]::new);
    }

    public static IEmptyIf[] unwrapArray(EmptyIfParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(EmptyIfParcelable::getEmptyIf)
           .toArray(IEmptyIf[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
