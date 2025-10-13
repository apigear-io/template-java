package {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name}};
import android.os.Parcel;
import android.os.Parcelable;


{{- define "getParcelable"}}
    {{- $ImportSchema:= printf "%s" ( camel .Schema.Import ) }}
    {{- $parcelableTypeName :=  Camel .Type -}}
    {{- if not (eq $ImportSchema  "" ) -}}
        {{$ImportSchema}}.{{$ImportSchema}}_android_messenger.{{$parcelableTypeName}}Parcelable
    {{- else -}}
            {{$parcelableTypeName}}Parcelable
    {{- end -}}
{{- end }}
{{- $typesToImport := getEmptyStringList}}
{{- $interfacesToImport := getEmptyStringList}}
{{- $module := camel .Module.Name}}
{{- range .Interface.Properties }}
    {{- if and (not .Schema.Import)  (not .IsPrimitive)  }}
{{- $type :=  Camel .Type }}
        {{- if eq .KindType "interface" }}
{{- $interfacesToImport = (appendList $typesToImport $type) }}
        {{- else }}
{{- $typesToImport = (appendList $typesToImport $type) }}
        {{- end }}
    {{- end }}
{{- end }}
{{- range .Interface.Operations }}
    {{- range .Params }}
    {{- if and (not .Schema.Import)  (not .IsPrimitive)  }}
{{- $type :=  Camel .Type }}
        {{- if eq .KindType "interface" }}
{{- $interfacesToImport = (appendList $typesToImport $type) }}
        {{- else }}
{{- $typesToImport = (appendList $typesToImport $type) }}
        {{- end }}
    {{- end }}
    {{- end }}
    {{- if and (and (not .Return.Schema.Import)  (not .Return.IsPrimitive))  (not .Return.IsVoid) }}
{{- $type :=  Camel .Return.Type }}
        {{- if eq .Return.KindType "interface" }}
{{- $interfacesToImport = (appendList $typesToImport $type) }}
        {{- else }}
{{- $typesToImport = (appendList $typesToImport $type) }}
        {{- end }}
    {{- end }}
{{- end }}
{{- range .Interface.Signals }}
    {{- range .Params }}
    {{- if and (not .Schema.Import)  (not .IsPrimitive)  }}
{{- $type :=  Camel .Type }}
        {{- if eq .KindType "interface" }}
{{- $interfacesToImport = (appendList $typesToImport $type) }}
        {{- else }}
{{- $typesToImport = (appendList $typesToImport $type) }}
        {{- end }}
    {{- end }}
    {{- end }}
{{- end }}
{{- $typesToImport = unique $typesToImport }}
{{- $interfacesToImport = unique $interfacesToImport }}
{{- range $typesToImport}}
import {{$module}}.{{$module}}_api.{{.}};
{{- end}}
{{- range $interfacesToImport}}
import {{$module}}.{{$module}}_api.I{{.}};
{{- end}}

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
{{- range .Interface.Properties }}
{{- if .IsArray}}
{{- if (eq .KindType "enum") }}
        {{template "getParcelable" .}}[] l_parcelable{{camel .Name}} = in.createTypedArray({{template "getParcelable" .}}.CREATOR);
        data.set{{Camel .Name}}({{template "getParcelable" .}}.unwrapArray(l_parcelable{{camel .Name}}));
{{- else if .IsPrimitive }}
        data.set{{Camel .Name}}(in.create{{ ( Camel  (javaElementType "" .) ) }}Array());
{{- else }}
        {{template "getParcelable" .}}[] l_parcelable{{camel .Name}} = in.createTypedArray({{template "getParcelable" .}}.CREATOR);
        data.set{{Camel .Name}}({{template "getParcelable" .}}.unwrapArray(l_parcelable{{camel .Name}}));
{{- end }}
{{- else }}
{{- if (eq .KindType "enum") }}
        {{template "getParcelable" .}} l_parcelable{{camel .Name}} = in.readParcelable({{template "getParcelable" .}}.class.getClassLoader(), {{template "getParcelable" .}}.class);
        data.set{{Camel .Name}}(l_parcelable{{camel .Name}} != null ? l_parcelable{{camel .Name}}.data : null);
{{- else if .IsPrimitive }}
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
    {{- range .Interface.Properties }}
{{- if .IsArray}}
{{- if (eq .KindType "enum") }}
        dest.writeTypedArray({{template "getParcelable" .}}.wrapArray(data.get{{Camel .Name}}()), flags);
{{- else if .IsPrimitive }}
        dest.write{{ ( Camel  (javaElementType "" .) ) }}Array(data.get{{Camel .Name}}());
{{- else }}
        dest.writeTypedArray({{template "getParcelable" .}}.wrapArray(data.get{{Camel .Name}}()), flags);
{{- end }}
{{- else }}
{{- if (eq .KindType "enum") }}
        dest.writeParcelable(new {{template "getParcelable" .}}(data.get{{Camel .Name}}()), flags);
{{- else if .IsPrimitive }}
        dest.write{{ ( Camel  (javaType "" .) ) }}(data.get{{Camel .Name}}());
{{- else }}
        dest.writeParcelable(new {{template "getParcelable" .}}(data.get{{Camel .Name}}()), flags);
{{- end }}
{{- end }}

{{- end}}


    }
        public static {{Camel .Interface.Name}}Parcelable[] wrapArray(I{{Camel .Interface.Name}}[] elements) {
        if (elements == null) return null;
        {{Camel .Interface.Name}}Parcelable[] out = new {{Camel .Interface.Name}}Parcelable[elements.length];
        for (int i = 0; i < elements.length; i++) {
            out[i] = new {{Camel .Interface.Name}}Parcelable(elements[i]);
        }
        return out;
    }

    public static I{{Camel .Interface.Name}}[] unwrapArray({{Camel .Interface.Name}}Parcelable[] parcelables) {
        if (parcelables == null) return null;
        I{{Camel .Interface.Name}}[] out = new I{{Camel .Interface.Name}}[parcelables.length];
        for (int i = 0; i < parcelables.length; i++) {
            out[i] = parcelables[i].get{{Camel .Interface.Name}}();
        }
        return out;
    }

    @Override
    public int describeContents() {
        return 0;
    }
  }
