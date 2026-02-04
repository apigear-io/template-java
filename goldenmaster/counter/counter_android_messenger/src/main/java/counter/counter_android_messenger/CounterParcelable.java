package counter.counter_android_messenger;

import counter.counter_api.ICounter;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

  public  class CounterParcelable implements Parcelable {

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
        customTypes.customTypes_android_messenger.Vector3DParcelable l_parcelablevector = in.readParcelable(customTypes.customTypes_android_messenger.Vector3DParcelable.class.getClassLoader(), customTypes.customTypes_android_messenger.Vector3DParcelable.class);
        data.setVector(l_parcelablevector != null ? l_parcelablevector.data : null);
        externTypes.externTypes_android_messenger.MyVector3DParcelable l_parcelableexternVector = in.readParcelable(externTypes.externTypes_android_messenger.MyVector3DParcelable.class.getClassLoader(), externTypes.externTypes_android_messenger.MyVector3DParcelable.class);
        data.setExternVector(l_parcelableexternVector != null ? l_parcelableexternVector.data : null);
        customTypes.customTypes_android_messenger.Vector3DParcelable[] l_parcelablevectorArray = in.createTypedArray(customTypes.customTypes_android_messenger.Vector3DParcelable.CREATOR);
        data.setVectorArray(customTypes.customTypes_android_messenger.Vector3DParcelable.unwrapArray(l_parcelablevectorArray));
        externTypes.externTypes_android_messenger.MyVector3DParcelable[] l_parcelableexternVectorArray = in.createTypedArray(externTypes.externTypes_android_messenger.MyVector3DParcelable.CREATOR);
        data.setExternVectorArray(externTypes.externTypes_android_messenger.MyVector3DParcelable.unwrapArray(l_parcelableexternVectorArray));
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
        dest.writeTypedArray(customTypes.customTypes_android_messenger.Vector3DParcelable.wrapArray(data.getVectorArray()), flags);
        dest.writeTypedArray(externTypes.externTypes_android_messenger.MyVector3DParcelable.wrapArray(data.getExternVectorArray()), flags);


    }
        public static CounterParcelable[] wrapArray(ICounter[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map(CounterParcelable::new)
           .toArray(CounterParcelable[]::new);
    }

    public static ICounter[] unwrapArray(CounterParcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map(CounterParcelable::getCounter)
           .toArray(ICounter[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
