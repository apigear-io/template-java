package {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name}};
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

{{- template "importApi" .}}

  public  class {{Camel .Interface.Name}}Parcelable implements Parcelable {

    private static final String TAG = "{{Camel .Interface.Name}}Parcelable";

    public I{{Camel .Interface.Name}} data;

    public {{Camel .Interface.Name}}Parcelable(I{{Camel .Interface.Name}} data) {
        this.data = data;
    }

    public I{{Camel .Interface.Name}} get{{Camel .Interface.Name}}()
    {
        return data;
    }

    protected {{Camel .Interface.Name}}Parcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            data = null;
            return;
        }

        Log.w(TAG, "Unwrapping interfaces from parcel is currently not supported");
        return;
    }

    public static final Creator<{{Camel .Interface.Name}}Parcelable> CREATOR = new Creator<{{Camel .Interface.Name}}Parcelable>() {
        @Override
        public {{Camel .Interface.Name}}Parcelable createFromParcel(Parcel in) {
            return new {{Camel .Interface.Name}}Parcelable(in);
        }

        @Override
        public {{Camel .Interface.Name}}Parcelable[] newArray(int size) {
            return new {{Camel .Interface.Name}}Parcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }

    {{- range .Interface.Properties }}
{{- if .IsArray}}
{{- if .IsPrimitive }}
        dest.write{{ ( Camel  (javaElementType "" .) ) }}Array(Conversions.toArray(data.get{{Camel .Name}}(), new {{javaElementType "" .}}[0]));
{{- else }}
        dest.writeTypedArray({{template "getParcelable" .}}.wrapArray(Conversions.toArray(data.get{{Camel .Name}}(), new {{javaElementType "" .}}[0])), flags);
{{- end }}
{{- else }}
{{- if .IsPrimitive }}
        dest.write{{ ( Camel  (javaType "" .) ) }}(data.get{{Camel .Name}}());
{{- else }}
        dest.writeParcelable(new {{template "getParcelable" .}}(data.get{{Camel .Name}}()), flags);
{{- end }}
{{- end }}

{{- end}}


    }
        public static {{Camel .Interface.Name}}Parcelable[] wrapArray(I{{Camel .Interface.Name}}[] elements) {
        if (elements == null) return null;
        return Arrays.stream(elements)
           .map({{Camel .Interface.Name}}Parcelable::new)
           .toArray({{Camel .Interface.Name}}Parcelable[]::new);
    }

    public static I{{Camel .Interface.Name}}[] unwrapArray({{Camel .Interface.Name}}Parcelable[] parcelables) {
        if (parcelables == null) return new I{{Camel .Interface.Name}}[0];
        return Arrays.stream(parcelables)
           .map({{Camel .Interface.Name}}Parcelable::get{{Camel .Interface.Name}})
           .toArray(I{{Camel .Interface.Name}}[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
