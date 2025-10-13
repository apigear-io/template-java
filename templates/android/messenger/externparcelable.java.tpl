package {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger;

{{- $externInfo := javaExtern .Extern }}
import {{$externInfo.Package}}.{{$externInfo.Name}};
import android.os.Parcel;
import android.os.Parcelable;


  public  class {{Camel .Extern.Name}}Parcelable implements Parcelable {
    public {{$externInfo.Name}} data;

    public {{Camel .Extern.Name}}Parcelable({{$externInfo.Name}} data) {
        // WARNING Copy if not simple type
        this.data = data;
    }

    public {{$externInfo.Name}} get{{Camel .Extern.Name}}()
    {
        // WARNING Copy if not simple type.
        return data;
    }

    protected {{Camel .Extern.Name}}Parcelable(Parcel in) {
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
    // WARNING Fill dest field by field with dest.write[TypedArray/Type/Parcelabe](data.field, flags);
    }

    // Helpers for arrays of this type
    public static {{Camel .Extern.Name}}Parcelable[] wrapArray({{$externInfo.Name}}[] elements)
    {
        if (elements == null) return null;
        {{Camel .Extern.Name}}Parcelable[] out = new {{Camel .Extern.Name}}Parcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new {{Camel .Extern.Name}}Parcelable(elements[i]);
        }
        return out;
    }

    public static {{$externInfo.Name}}[] unwrapArray({{Camel .Extern.Name}}Parcelable[] parcelables) {
        if (parcelables == null) return null;
        {{$externInfo.Name}}[] out = new {{$externInfo.Name}}[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].get{{Camel .Extern.Name}}();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
