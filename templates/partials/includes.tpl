{{- define "importApi"}}
{{- $typesToImport := getEmptyStringList}}
{{- $interfacesToImport := getEmptyStringList}}
{{- $module := camel .Module.Name}}
{{- range .Interface.Properties }}
    {{- if and (not .Schema.Import)  (not .IsPrimitive)  }}
{{- $type :=  Camel .Type }}
        {{- if eq .KindType "interface" }}
{{- $interfacesToImport = (appendList $interfacesToImport $type) }}
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
{{- $interfacesToImport = (appendList $interfacesToImport $type) }}
        {{- else }}
{{- $typesToImport = (appendList $typesToImport $type) }}
        {{- end }}
    {{- end }}
    {{- end }}
    {{- if and (and (not .Return.Schema.Import)  (not .Return.IsPrimitive))  (not .Return.IsVoid) }}
{{- $type :=  Camel .Return.Type }}
        {{- if eq .Return.KindType "interface" }}
{{- $interfacesToImport = (appendList $interfacesToImport $type) }}
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
{{- $interfacesToImport = (appendList $interfacesToImport $type) }}
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
{{- end}}

{{- define "importApiWithParcelable"}}
{{- $typesToImport := getEmptyStringList}}
{{- $interfacesToImport := getEmptyStringList}}
{{- $module := camel .Module.Name}}
{{- range .Interface.Properties }}
    {{- if and (not .Schema.Import)  (not .IsPrimitive)  }}
{{- $type :=  Camel .Type }}
        {{- if eq .KindType "interface" }}
{{- $interfacesToImport = (appendList $interfacesToImport $type) }}
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
{{- $interfacesToImport = (appendList $interfacesToImport $type) }}
        {{- else }}
{{- $typesToImport = (appendList $typesToImport $type) }}
        {{- end }}
    {{- end }}
    {{- end }}
    {{- if and (and (not .Return.Schema.Import)  (not .Return.IsPrimitive))  (not .Return.IsVoid) }}
{{- $type :=  Camel .Return.Type }}
        {{- if eq .Return.KindType "interface" }}
{{- $interfacesToImport = (appendList $interfacesToImport $type) }}
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
{{- $interfacesToImport = (appendList $interfacesToImport $type) }}
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
import {{$module}}.{{$module}}_android_messenger.{{.}}Parcelable;
{{- end}}
{{- range $interfacesToImport}}
import {{$module}}.{{$module}}_api.I{{.}};
import {{$module}}.{{$module}}_android_messenger.{{.}}Parcelable;
{{- end}}
{{- end}}

{{- define "importApiWithService"}}
{{- $typesToImport := getEmptyStringList}}
{{- $interfacesToImport := getEmptyStringList}}
{{- $module := camel .Module.Name}}
{{- range .Interface.Properties }}
    {{- if and (not .Schema.Import)  (not .IsPrimitive)  }}
{{- $type :=  Camel .Type }}
        {{- if eq .KindType "interface" }}
{{- $interfacesToImport = (appendList $interfacesToImport $type) }}
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
{{- $interfacesToImport = (appendList $interfacesToImport $type) }}
        {{- else }}
{{- $typesToImport = (appendList $typesToImport $type) }}
        {{- end }}
    {{- end }}
    {{- end }}
    {{- if and (and (not .Return.Schema.Import)  (not .Return.IsPrimitive))  (not .Return.IsVoid) }}
{{- $type :=  Camel .Return.Type }}
        {{- if eq .Return.KindType "interface" }}
{{- $interfacesToImport = (appendList $interfacesToImport $type) }}
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
{{- $interfacesToImport = (appendList $interfacesToImport $type) }}
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
import {{$module}}.{{$module}}_impl.{{.}}Service;
{{- end}}
{{- end}}

{{- define "importApiForStructTypes"}}
{{- $typesToImport := getEmptyStringList}}
{{- $interfacesToImport := getEmptyStringList}}
{{- $module := camel .Module.Name}}
{{- range .Struct.Fields }}
{{- if and (not .Schema.Import)  (not .IsPrimitive) }}
{{- $type :=  Camel .Type }}
{{- if eq .KindType "interface" }}
{{- $interfacesToImport = (appendList $interfacesToImport $type) }}
{{- else }}
{{- $typesToImport = (appendList $typesToImport $type) }}
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
{{- end}}
