{{- define "getParcelable"}}
    {{- $ImportSchema:= printf "%s" ( camel .Schema.Import ) }}
    {{- $parcelableTypeName :=  Camel .Type -}}
    {{- if not (eq $ImportSchema  "" ) -}}
        {{$ImportSchema}}.{{$ImportSchema}}_android_messenger.{{$parcelableTypeName}}Parcelable
    {{- else -}}
            {{$parcelableTypeName}}Parcelable
    {{- end -}}
{{- end }}
