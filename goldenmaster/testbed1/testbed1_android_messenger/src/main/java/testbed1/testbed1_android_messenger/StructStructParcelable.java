package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructStruct;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import testbed1.testbed1_api.StructString;

  public  class StructStructParcelable implements Parcelable {

    public StructStruct data;

    public StructStructParcelable(StructStruct data) {
        this.data = data != null ? new StructStruct(data) : null;
    }

    public StructStruct getStructStruct()
    {
        return data != null ? new StructStruct(data) : null;
    }

    protected StructStructParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

        this.data = new StructStruct();
        StructStringParcelable l_parcelablefieldString = in.readParcelable(StructStringParcelable.class.getClassLoader(), StructStringParcelable.class);
        data.fieldString = l_parcelablefieldString != null ? l_parcelablefieldString.data : null;
    }

    public static final Creator<StructStructParcelable> CREATOR = new Creator<StructStructParcelable>() {
        @Override
        public StructStructParcelable createFromParcel(Parcel in) {
            return new StructStructParcelable(in);
        }

        @Override
        public StructStructParcelable[] newArray(int size) {
            return new StructStructParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }

        dest.writeParcelable(new StructStringParcelable(data.fieldString), flags);


    }
        public static StructStructParcelable[] wrapArray(StructStruct[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(StructStructParcelable::new)
           .toArray(StructStructParcelable[]::new);
    }

    public static StructStruct[] unwrapArray(StructStructParcelable[] parcelables) {
        if (parcelables == null) return new StructStruct[0];
        return Arrays.stream(parcelables)
           .map(StructStructParcelable::getStructStruct)
           .toArray(StructStruct[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
