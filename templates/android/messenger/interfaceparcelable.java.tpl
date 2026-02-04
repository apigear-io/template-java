package {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name}};
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

{{- template "importApi" .}}

  public  class {{Camel .Interface.Name}}Parcelable implements Parcelable {

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

{{- range .Interface.Properties }}
{{- if .IsArray}}
{{- if .IsPrimitive }}
        data.set{{Camel .Name}}(in.create{{ ( Camel  (javaElementType "" .) ) }}Array());
{{- else }}
        {{template "getParcelable" .}}[] l_parcelable{{camel .Name}} = in.createTypedArray({{template "getParcelable" .}}.CREATOR);
        data.set{{Camel .Name}}({{template "getParcelable" .}}.unwrapArray(l_parcelable{{camel .Name}}));
{{- end }}
{{- else }}
{{- if .IsPrimitive }}
        data.set{{Camel .Name}}(in.read{{ ( Camel  (javaType "" .) ) }}());
{{- else }}
        {{template "getParcelable" .}} l_parcelable{{camel .Name}} = in.readParcelable({{template "getParcelable" .}}.class.getClassLoader(), {{template "getParcelable" .}}.class);
        data.set{{Camel .Name}}(l_parcelable{{camel .Name}} != null ? l_parcelable{{camel .Name}}.data : null);
{{- end }}
{{- end }}

{{- end }}
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
        dest.write{{ ( Camel  (javaElementType "" .) ) }}Array(data.get{{Camel .Name}}());
{{- else }}
        dest.writeTypedArray({{template "getParcelable" .}}.wrapArray(data.get{{Camel .Name}}()), flags);
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
        if (parcelables == null) return null;
        return Arrays.stream(parcelables)
           .map({{Camel .Interface.Name}}Parcelable::get{{Camel .Interface.Name}})
           .toArray(I{{Camel .Interface.Name}}[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
