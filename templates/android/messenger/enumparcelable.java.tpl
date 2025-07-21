package {{dot .Module.Name}}.{{dot .Module.Name}}_android_messenger;

import {{dot .Module.Name}}.{{dot .Module.Name}}_api.{{Camel .Enum.Name}};
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

    @Override
    public int describeContents() {
        return 0;
    }
  }
