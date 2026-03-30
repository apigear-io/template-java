package tbNames.tbNames_android_messenger;

import tbNames.tbNames_api.INamEs;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import tbNames.tbNames_api.EnumWithUnderScores;

  public  class NamEsParcelable implements Parcelable {

    private static final String TAG = "NamEsParcelable";

    public INamEs data;

    public NamEsParcelable(INamEs data) {
        this.data = data;
    }

    public INamEs getNamEs()
    {
        return data;
    }

    protected NamEsParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
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
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
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
        if (parcelables == null) return new INamEs[0];
        return Arrays.stream(parcelables)
           .map(NamEsParcelable::getNamEs)
           .toArray(INamEs[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
