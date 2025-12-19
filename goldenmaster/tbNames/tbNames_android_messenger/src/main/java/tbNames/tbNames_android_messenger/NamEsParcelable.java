package tbNames.tbNames_android_messenger;

import tbNames.tbNames_api.INamEs;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import tbNames.tbNames_api.EnumWithUnderScores;

  public  class NamEsParcelable implements Parcelable {

    public INamEs data;

    public NamEsParcelable(INamEs data) {
        this.data = data;
    }

    public INamEs getNamEs()
    {
        return data;
    }

    protected NamEsParcelable(Parcel in) {
        data.setSwitch(in.readBoolean());
        data.setSomeProperty(in.readInt());
        data.setSomePoperty2(in.readInt());
        EnumWithUnderScoresParcelable l_parcelableenumProperty = in.readParcelable(EnumWithUnderScoresParcelable.class.getClassLoader(), EnumWithUnderScoresParcelable.class);
        data.setEnumProperty(l_parcelableenumProperty != null ? l_parcelableenumProperty.data : null);
    }

    public static final Creator<NamEsParcelable> CREATOR = new Creator<NamEsParcelable>() {
        @Override
        public NamEsParcelable createFromParcel(Parcel in) {
            return new NamEsParcelable(in);
        }

        @Override
        public NamEsParcelable[] newArray(int size) {
            return new NamEsParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data.getSwitch());
        dest.writeInt(data.getSomeProperty());
        dest.writeInt(data.getSomePoperty2());
        dest.writeParcelable(new EnumWithUnderScoresParcelable(data.getEnumProperty()), flags);


    }
        public static NamEsParcelable[] wrapArray(INamEs[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(NamEsParcelable::new)
           .toArray(NamEsParcelable[]::new);
    }

    public static INamEs[] unwrapArray(NamEsParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(NamEsParcelable::getNamEs)
           .toArray(INamEs[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
