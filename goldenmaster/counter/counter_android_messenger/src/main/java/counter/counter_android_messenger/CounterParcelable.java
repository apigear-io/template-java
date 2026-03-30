package counter.counter_android_messenger;

import counter.counter_api.ICounter;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

  public  class CounterParcelable implements Parcelable {

    private static final String TAG = "CounterParcelable";

    public ICounter data;

    public CounterParcelable(ICounter data) {
        this.data = data;
    }

    public ICounter getCounter()
    {
        return data;
    }

    protected CounterParcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
    }

    public static final Creator<CounterParcelable> CREATOR = new Creator<CounterParcelable>() {
        @Override
        public CounterParcelable createFromParcel(Parcel in) {
            return new CounterParcelable(in);
        }

        @Override
        public CounterParcelable[] newArray(int size) {
            return new CounterParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
        dest.writeParcelable(new customTypes.customTypes_android_messenger.Vector3DParcelable(data.getVector()), flags);
        dest.writeParcelable(new externTypes.externTypes_android_messenger.MyVector3DParcelable(data.getExternVector()), flags);
        dest.writeTypedArray(customTypes.customTypes_android_messenger.Vector3DParcelable.wrapArray(Conversions.toArray(data.getVectorArray(), new customTypes.customTypes_api.Vector3D[0])), flags);
        dest.writeTypedArray(externTypes.externTypes_android_messenger.MyVector3DParcelable.wrapArray(Conversions.toArray(data.getExternVectorArray(), new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[0])), flags);


    }
        public static CounterParcelable[] wrapArray(ICounter[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(CounterParcelable::new)
           .toArray(CounterParcelable[]::new);
    }

    public static ICounter[] unwrapArray(CounterParcelable[] parcelables) {
        if (parcelables == null) return new ICounter[0];
        return Arrays.stream(parcelables)
           .map(CounterParcelable::getCounter)
           .toArray(ICounter[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
