package {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger;

{{- $externInfo := javaExtern .Extern }}
import {{$externInfo.Package}}.{{$externInfo.Name}};
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

  public  class {{Camel .Extern.Name}}Parcelable implements Parcelable {
    public {{$externInfo.Name}} data;

    public {{Camel .Extern.Name}}Parcelable({{$externInfo.Name}} data) {
        // WARNING Copy if not simple type. Remember about nulls.
        this.data = data;
    }

    public {{$externInfo.Name}} get{{Camel .Extern.Name}}()
    {
        // WARNING Copy if not simple type. Remember about nulls.
        return data;
    }

    protected {{Camel .Extern.Name}}Parcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

        //WARNING Fill the data field by field with in.createTypedArray, in. read[dataType] or in.readParcelable, depending on type.
    }

    public static final Creator<{{Camel .Extern.Name}}Parcelable> CREATOR = new Creator<{{Camel .Extern.Name}}Parcelable>() {
        @Override
        public {{Camel .Extern.Name}}Parcelable createFromParcel(Parcel in) {
            return new {{Camel .Extern.Name}}Parcelable(in);
        }

        @Override
        public {{Camel .Extern.Name}}Parcelable[] newArray(int size) {
            return new {{Camel .Extern.Name}}Parcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(this.data != null);
        if (this.data == null) {
            return;
        }

    // WARNING Fill dest field by field with dest.write[TypedArray/Type/Parcelabe](data.field, flags);
    }

    // Helpers for arrays of this type
    public static {{Camel .Extern.Name}}Parcelable[] wrapArray({{$externInfo.Name}}[] elements)
    {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map({{Camel .Extern.Name}}Parcelable::new)
           .toArray({{Camel .Extern.Name}}Parcelable[]::new);
    }

    public static {{$externInfo.Name}}[] unwrapArray({{Camel .Extern.Name}}Parcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map({{Camel .Extern.Name}}Parcelable::get{{Camel .Extern.Name}})
           .toArray({{$externInfo.Name}}[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
