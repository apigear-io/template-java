package {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Struct.Name}};
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

{{- template "importApiForStructTypes" .}}

  public  class {{Camel .Struct.Name}}Parcelable implements Parcelable {

    public {{Camel .Struct.Name}} data;

    public {{Camel .Struct.Name}}Parcelable({{Camel .Struct.Name}} data) {
        this.data = data != null ? new {{Camel .Struct.Name}}(data) : null;
    }

    public {{Camel .Struct.Name}} get{{Camel .Struct.Name}}()
    {
        return data != null ? new {{Camel .Struct.Name}}(data) : null;
    }

    protected {{Camel .Struct.Name}}Parcelable(Parcel in) {
        boolean dataIsValid = in.readBoolean();
        if (!dataIsValid) {
            this.data = null;
            return;
        }

        this.data = new {{Camel .Struct.Name}}();
{{- range .Struct.Fields }}
{{- if .IsArray}}
{{- if .IsPrimitive }}
        data.{{camel .Name}} = Conversions.toList(in.create{{ ( Camel  (javaElementType "" .) ) }}Array());
{{- else }}
        data.{{camel .Name}} = Conversions.toList({{javaElementType "" . }}Parcelable.unwrapArray(in.createTypedArray({{javaElementType "" . }}Parcelable.CREATOR)));
{{- end }}
{{- else }}
{{- if .IsPrimitive }}
        data.{{camel .Name}} = in.read{{ ( Camel  (javaType "" .) ) }}();
{{- else }}
        {{javaType "" .}}Parcelable l_parcelable{{camel .Name}} = in.readParcelable({{javaType "" .}}Parcelable.class.getClassLoader(), {{javaType "" .}}Parcelable.class);
        data.{{camel .Name}} = l_parcelable{{camel .Name}} != null ? l_parcelable{{camel .Name}}.data : null;
{{- end }}
{{- end }}

{{- end }}
    }

    public static final Creator<{{Camel .Struct.Name}}Parcelable> CREATOR = new Creator<{{Camel .Struct.Name}}Parcelable>() {
        @Override
        public {{Camel .Struct.Name}}Parcelable createFromParcel(Parcel in) {
            return new {{Camel .Struct.Name}}Parcelable(in);
        }

        @Override
        public {{Camel .Struct.Name}}Parcelable[] newArray(int size) {
            return new {{Camel .Struct.Name}}Parcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBoolean(data != null);
        if (data == null) {
            return;
        }
{{- nl }}
    {{- range .Struct.Fields }}
{{- if .IsArray}}
{{- if .IsPrimitive }}
        dest.write{{ ( Camel  (javaElementType "" .) ) }}Array(Conversions.toArray(data.{{camel .Name}}, new {{javaElementType "" .}}[0]));
{{- else }}
        dest.writeTypedArray({{javaElementType "" . }}Parcelable.wrapArray(Conversions.toArray(data.{{camel .Name}}, new {{javaElementType "" .}}[0])), flags);
{{- end }}
{{- else }}
{{- if .IsPrimitive }}
        dest.write{{ ( Camel  (javaType "" .) ) }}(data.{{camel .Name}});
{{- else }}
        dest.writeParcelable(new {{javaType "" .}}Parcelable(data.{{camel .Name}}), flags);
{{- end }}
{{- end }}

{{- end}}


    }
        public static {{Camel .Struct.Name}}Parcelable[] wrapArray({{Camel .Struct.Name}}[] structs) {
        if (structs == null) return null;
        return Arrays.stream(structs)
           .map({{Camel .Struct.Name}}Parcelable::new)
           .toArray({{Camel .Struct.Name}}Parcelable[]::new);
    }

    public static {{Camel .Struct.Name}}[] unwrapArray({{Camel .Struct.Name}}Parcelable[] parcelables) {
        if (parcelables == null) return new {{Camel .Struct.Name}}[0];
        return Arrays.stream(parcelables)
           .map({{Camel .Struct.Name}}Parcelable::get{{Camel .Struct.Name}})
           .toArray({{Camel .Struct.Name}}[]::new);
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
