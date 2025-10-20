package tbNames.tbNames_android_messenger;

import tbNames.tbNames_api.EnumWithUnderScores;
import android.os.Parcel;
import android.os.Parcelable;



//TODO imports - may need some struct from this or imported module

  public  class EnumWithUnderScoresParcelable implements Parcelable {

    public EnumWithUnderScores data;

    public EnumWithUnderScoresParcelable(EnumWithUnderScores data) {
        this.data = data;
    }

    public EnumWithUnderScores getEnumWithUnderScores()
    {
        return data;
    }

    protected EnumWithUnderScoresParcelable(Parcel in) {
        int intValue = in.readInt();
        this.data = EnumWithUnderScores.fromValue(intValue);
    }

    public static final Creator<EnumWithUnderScoresParcelable> CREATOR = new Creator<EnumWithUnderScoresParcelable>() {
        @Override
        public EnumWithUnderScoresParcelable createFromParcel(Parcel in) {
            return new EnumWithUnderScoresParcelable(in);
        }

        @Override
        public EnumWithUnderScoresParcelable[] newArray(int size) {
            return new EnumWithUnderScoresParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(data.getValue());
    }

    public static EnumWithUnderScoresParcelable[] wrapArray(EnumWithUnderScores[] enums) {
        if (enums == null) return null;
        EnumWithUnderScoresParcelable[] result = new EnumWithUnderScoresParcelable[enums.length];
        for (int i = 0; i < enums.length; i++) {
            result[i] = new EnumWithUnderScoresParcelable(enums[i]);
        }
        return result;
    }

    public static EnumWithUnderScores[] unwrapArray(EnumWithUnderScoresParcelable[] parcelables) {
        if (parcelables == null) return null;
        EnumWithUnderScores[] out = new EnumWithUnderScores[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].getEnumWithUnderScores();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
