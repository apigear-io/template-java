package tbRefIfaces.tbRefIfaces_android_messenger;

import tbRefIfaces.tbRefIfaces_api.IParentIf;
import android.os.Parcel;
import android.os.Parcelable;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;

  public  class ParentIfParcelable implements Parcelable {

    public IParentIf data;

    public ParentIfParcelable(IParentIf data) {
        this.data = data;
    }

    public IParentIf getParentIf()
    {
        return data;
    }

    protected ParentIfParcelable(Parcel in) {
        SimpleLocalIfParcelable l_parcelablelocalIf = in.readParcelable(SimpleLocalIfParcelable.class.getClassLoader(), SimpleLocalIfParcelable.class);
        data.setLocalIf(l_parcelablelocalIf != null ? l_parcelablelocalIf.data : null);
        SimpleLocalIfParcelable[] l_parcelablelocalIfList = in.createTypedArray(SimpleLocalIfParcelable.CREATOR);
        data.setLocalIfList(SimpleLocalIfParcelable.unwrapArray(l_parcelablelocalIfList));
        tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable l_parcelableimportedIf = in.readParcelable(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class.getClassLoader(), tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.class);
        data.setImportedIf(l_parcelableimportedIf != null ? l_parcelableimportedIf.data : null);
        tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable[] l_parcelableimportedIfList = in.createTypedArray(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.CREATOR);
        data.setImportedIfList(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.unwrapArray(l_parcelableimportedIfList));
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
        dest.writeParcelable(new SimpleLocalIfParcelable(data.getLocalIf()), flags);
        dest.writeTypedArray(SimpleLocalIfParcelable.wrapArray(data.getLocalIfList()), flags);
        dest.writeParcelable(new tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable(data.getImportedIf()), flags);
        dest.writeTypedArray(tbIfaceimport.tbIfaceimport_android_messenger.EmptyIfParcelable.wrapArray(data.getImportedIfList()), flags);


    }
        public static ParentIfParcelable[] wrapArray(IParentIf[] elements) {
        if (elements == null) return null;
        ParentIfParcelable[] out = new ParentIfParcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new ParentIfParcelable(elements[i]);
        }
        return out;
    }

    public static IParentIf[] unwrapArray(ParentIfParcelable[] parcelables) {
        if (parcelables == null) return null;
        IParentIf[] out = new IParentIf[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getParentIf();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
