package tbNames.tbNames_android_messenger;

import tbNames.tbNames_api.EnumWithUnderScores;
import android.os.Parcel;
import android.os.Parcelable;


import java.util.Arrays;

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
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

        this.data = EnumWithUnderScores.fromValue(in.readInt());
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

        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }

        dest.writeInt(data.getValue());
    }

    public static EnumWithUnderScoresParcelable[] wrapArray(EnumWithUnderScores[] enums) {
        if (enums == null) return null;
        return Arrays.stream(enums)
           .map(EnumWithUnderScoresParcelable::new)
           .toArray(EnumWithUnderScoresParcelable[]::new);
    }

    public static EnumWithUnderScores[] unwrapArray(EnumWithUnderScoresParcelable[] parcelables) {
        if (parcelables == null) return new EnumWithUnderScores[0];
        return Arrays.stream(parcelables)
           .map(EnumWithUnderScoresParcelable::getEnumWithUnderScores)
           .toArray(EnumWithUnderScores[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
