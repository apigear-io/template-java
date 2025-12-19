package testbed1.testbed1_android_messenger;

import testbed1.testbed1_api.StructString;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

  public  class StructStringParcelable implements Parcelable {

    public StructString data;

    public StructStringParcelable(StructString data) {
        this.data = new StructString(data);
    }

    public StructString getStructString()
    {
        return new StructString(data);
    }

    protected StructStringParcelable(Parcel in) {
    this.data = new StructString();
        data.fieldString = in.readString();
    }

    public static final Creator<StructStringParcelable> CREATOR = new Creator<StructStringParcelable>() {
        @Override
        public StructStringParcelable createFromParcel(Parcel in) {
            return new StructStringParcelable(in);
        }

        @Override
        public StructStringParcelable[] newArray(int size) {
            return new StructStringParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data.fieldString);


    }
        public static StructStringParcelable[] wrapArray(StructString[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map(StructStringParcelable::new)
           .toArray(StructStringParcelable[]::new);
    }

    public static StructString[] unwrapArray(StructStringParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(StructStringParcelable::getStructString)
           .toArray(StructString[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
