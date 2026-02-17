package tbRefIfaces.tbRefIfaces_android_messenger;

import tbRefIfaces.tbRefIfaces_api.IParentIf;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Arrays;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;

  public  class ParentIfParcelable implements Parcelable {

    private static final String TAG = "ParentIfParcelable";

    public IParentIf data;

    public ParentIfParcelable(IParentIf data) {
        this.data = data;
    }

    public IParentIf getParentIf()
    {
        return data;
    }

    protected ParentIfParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
    }

    public static final Creator<ParentIfParcelable> CREATOR = new Creator<ParentIfParcelable>() {
        @Override
        public ParentIfParcelable createFromParcel(Parcel in) {
            return new ParentIfParcelable(in);
        }

        @Override
        public ParentIfParcelable[] newArray(int size) {
            return new ParentIfParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
        dest.writeParcelable(new SimpleLocalIfParcelable(data.getLocalIf()), flags);
        dest.writeTypedArray(SimpleLocalIfParcelable.wrapArray(data.getLocalIfList()), flags);
        dest.writeParcelable(new tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable(data.getImportedIf()), flags);
        dest.writeTypedArray(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(data.getImportedIfList()), flags);


    }
        public static ParentIfParcelable[] wrapArray(IParentIf[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(ParentIfParcelable::new)
           .toArray(ParentIfParcelable[]::new);
    }

    public static IParentIf[] unwrapArray(ParentIfParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(ParentIfParcelable::getParentIf)
           .toArray(IParentIf[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
