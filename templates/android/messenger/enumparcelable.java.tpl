package {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Enum.Name}};
import android.os.Parcel;
import android.os.Parcelable;


import java.util.Arrays;

//TODO imports - may need some struct from this or imported module

  public  class {{Camel .Enum.Name}}Parcelable implements Parcelable {

    public {{Camel .Enum.Name}} data;

    public {{Camel .Enum.Name}}Parcelable({{Camel .Enum.Name}} data) {
        this.data = data;
    }

    public {{Camel .Enum.Name}} get{{Camel .Enum.Name}}()
    {
        return data;
    }

    protected {{Camel .Enum.Name }}Parcelable(Parcel in) {
        int intValue = in.readInt();
        this.data = {{Camel .Enum.Name}}.fromValue(intValue);
    }

    public static final Creator<{{Camel .Enum.Name }}Parcelable> CREATOR = new Creator<{{Camel .Enum.Name}}Parcelable>() {
        @Override
        public {{Camel .Enum.Name}}Parcelable createFromParcel(Parcel in) {
            return new {{Camel .Enum.Name}}Parcelable(in);
        }

        @Override
        public {{Camel .Enum.Name}}Parcelable[] newArray(int size) {
            return new {{Camel .Enum.Name}}Parcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(data.getValue());
    }

    public static {{Camel .Enum.Name }}Parcelable[] wrapArray({{Camel .Enum.Name }}[] enums) {
        if (enums == null) return null;
        return Arrays.stream(enums)
           .map({{Camel .Enum.Name }}Parcelable::new)
           .toArray({{Camel .Enum.Name }}Parcelable[]::new);
    }

    public static {{Camel .Enum.Name }}[] unwrapArray({{Camel .Enum.Name }}Parcelable[] parcelables) {
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map({{Camel .Enum.Name}}Parcelable::get{{Camel .Enum.Name}})
           .toArray({{Camel .Enum.Name }}[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
