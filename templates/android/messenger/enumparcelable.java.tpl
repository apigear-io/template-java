package {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Enum.Name}};
import android.os.Parcel;
import android.os.Parcelable;



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
        {{Camel .Enum.Name }}Parcelable[] result = new {{Camel .Enum.Name }}Parcelable[enums.length];
        for (int i = 0; i < enums.length; i++) {
            result[i] = new {{Camel .Enum.Name }}Parcelable(enums[i]);
        }
        return result;
    }

    public static {{Camel .Enum.Name }}[] unwrapArray({{Camel .Enum.Name }}Parcelable[] parcelables) {
        if (parcelables == null) return null;
        {{Camel .Enum.Name }}[] out = new {{Camel .Enum.Name }}[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].get{{Camel .Enum.Name}}();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
