package {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Struct.Name}};
import android.os.Parcel;
import android.os.Parcelable;

{{- $typesToImport := getEmptyStringList}}
{{- $module := camel .Module.Name}}
{{- range .Struct.Fields }}
{{- if and (and (not .IsArray) (not .Schema.Import))  (not (or (.IsPrimitive) (eq .KindType "enum")) ) }}
{{- $type :=  Camel (javaType "" .) }}
{{- $typesToImport = (appendList $typesToImport $type) }}
{{- end }}
{{- end }}
{{- $typesToImport = unique $typesToImport }}
{{- range $typesToImport}}
import {{$module}}.{{$module}}_api.{{.}};
{{- end}}

  public  class {{Camel .Struct.Name}}Parcelable implements Parcelable {

    public {{Camel .Struct.Name}} data;

    public {{Camel .Struct.Name}}Parcelable({{Camel .Struct.Name}} data) {
        this.data = new {{Camel .Struct.Name}}(data);
    }

    public {{Camel .Struct.Name}} get{{Camel .Struct.Name}}()
    {
        return new {{Camel .Struct.Name}}(data);
    }

    protected {{Camel .Struct.Name}}Parcelable(Parcel in) {
    this.data = new {{Camel .Struct.Name}}();
{{- range .Struct.Fields }}
{{- if .IsArray}}
{{- if (eq .KindType "enum") }}
        {{javaElementType "" . }}Parcelable[] l_parcelable{{camel .Name}} = in.createTypedArray({{javaElementType "" . }}Parcelable.CREATOR);
        data.{{camel .Name}} = {{javaElementType "" . }}Parcelable.unwrapArray(l_parcelable{{camel .Name}});
{{- else if .IsPrimitive }}
        data.{{camel .Name}} = in.create{{ ( Camel  (javaElementType "" .) ) }}Array();
{{- else }}
        {{javaElementType "" . }}Parcelable[] l_parcelable{{camel .Name}} = in.createTypedArray({{javaElementType "" . }}Parcelable.CREATOR);
        data.{{camel .Name}} = {{javaElementType "" . }}Parcelable.unwrapArray(l_parcelable{{camel .Name}});
{{- end }}
{{- else }}
{{- if (eq .KindType "enum") }}
        {{javaType "" .}}Parcelable l_parcelable{{camel .Name}} = in.readParcelable({{javaType "" .}}Parcelable.class.getClassLoader(), {{javaType "" .}}Parcelable.class);
        data.{{camel .Name}} = l_parcelable{{camel .Name}} != null ? l_parcelable{{camel .Name}}.data : null;
{{- else if .IsPrimitive }}
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
    {{- range .Struct.Fields }}
{{- if .IsArray}}
{{- if (eq .KindType "enum") }}
        dest.writeTypedArray({{javaElementType "" . }}Parcelable.wrapArray(data.{{camel .Name}}), flags);
{{- else if .IsPrimitive }}
        dest.write{{ ( Camel  (javaElementType "" .) ) }}Array(data.{{camel .Name}});
{{- else }}
        dest.writeTypedArray({{javaElementType "" . }}Parcelable.wrapArray(data.{{camel .Name}}), flags);
{{- end }}
{{- else }}
{{- if (eq .KindType "enum") }}
        dest.writeParcelable(new {{javaType "" .}}Parcelable(data.{{camel .Name}}), flags);
{{- else if .IsPrimitive }}
        dest.write{{ ( Camel  (javaType "" .) ) }}(data.{{camel .Name}});
{{- else }}
        dest.writeParcelable(new {{javaType "" .}}Parcelable(data.{{camel .Name}}), flags);
{{- end }}
{{- end }}

{{- end}}


    }
        public static {{Camel .Struct.Name}}Parcelable[] wrapArray({{Camel .Struct.Name}}[] structs) {
        if (structs == null) return null;
        {{Camel .Struct.Name}}Parcelable[] out = new {{Camel .Struct.Name}}Parcelable[structs.length];
        for (int i = 0; i < structs.length; i++) {
            out[i] = new {{Camel .Struct.Name}}Parcelable(structs[i]);
        }
        return out;
    }

    public static {{Camel .Struct.Name}}[] unwrapArray({{Camel .Struct.Name}}Parcelable[] parcelables) {
        if (parcelables == null) return null;
        {{Camel .Struct.Name}}[] out = new {{Camel .Struct.Name}}[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].get{{Camel .Struct.Name}}();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
