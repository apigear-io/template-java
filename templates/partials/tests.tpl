{{- define "getMakeTestHelper"}}
    {{- $ImportSchema:= printf "%s" ( camel .Schema.Import ) }}
    {{- $TypeName :=  Camel .Type -}}
    {{- if not (eq $ImportSchema  "" ) -}}
        {{- $ClassName := printf "%sTestHelper" ( Camel .Schema.Import ) -}}
        {{$ImportSchema}}.{{$ImportSchema}}_api.{{$ClassName}}.makeTest{{$TypeName}}
    {{- else -}}
            {{- $ClassName := printf "%sTestHelper" ( Camel .Schema.Module.Name ) -}}
            {{$ClassName}}.makeTest{{$TypeName}}
    {{- end -}}
{{- end }}

{{- define "getReceivedFromBundle"}}
		{{- if .IsPrimitive }}
			{{javaReturn "" .}} received{{javaVar .}} = data.get{{ ( Camel  (javaElementType "" .) ) }}{{if .IsArray}}Array{{end}}("{{.Name}}"{{if not .IsArray}}, {{javaDefault "" .}}{{end}});
		{{- else if .IsArray }}
            {{javaReturn "" .}} received{{javaVar .}} =  {{template "getParcelable" . }}.unwrapArray(({{template "getParcelable" . }}[])data.getParcelableArray("{{.Name}}", {{template "getParcelable" . }}.class));
        {{- else }}
			{{javaReturn "" .}} received{{javaVar .}} = data.getParcelable("{{.Name}}", {{template "getParcelable" . }}.class).get{{Camel (.Type)}}();
		{{- end }}
{{- end }}

{{- define "putTestDataIntoBundle"}}
		{{- if .IsPrimitive }}
		data.put{{ ( Camel  (javaElementType "" .) ) }}{{if .IsArray}}Array{{end}}("{{.Name}}", test{{ javaVar .}});
		{{- else if .IsArray }}
		data.putParcelableArray("{{.Name}}", {{template "getParcelable" . }}.wrapArray(test{{javaVar .}}));
        {{- else }}
		data.putParcelable("{{.Name}}", new {{template "getParcelable" . }}(test{{javaVar .}}));
		{{- end }}
{{- end }}

{{- define "prepareTestValue"}}
        {{- if .IsArray }}
            {{- if or  (.IsPrimitive) (eq .KindType "enum")}}
        {{javaType "" .}} test{{ javaVar .}} = new {{javaElementType "" .}}[1];
        test{{ javaVar .}}[0] = {{javaTestValue "" . }};
            {{- else }}
        {{javaElementType "" .}}[] test{{ javaVar .}} = new {{javaElementType "" .}}[1];
                {{- if (eq .KindType "extern") }}
		test{{ javaVar .}}[0] = {{javaTestValue "" .}};
                {{- else }}
        test{{ javaVar .}}[0] = {{template "getMakeTestHelper" . }}({{-  if (eq .KindType "interface")}}{{javaTestValue "" .}}{{end}});
                {{- end }}
            {{- end}}
		{{- else if or  (.IsPrimitive) (eq .KindType "enum") }}
		{{javaReturn "" . }} test{{ javaVar .}} = {{javaTestValue "" . }};
		{{- else if (eq .KindType "extern") }}
		{{javaReturn "" . }} test{{ javaVar .}} = {{javaTestValue "" .}};
        {{- else }}
        {{javaReturn "" . }} test{{ javaVar .}} = {{template "getMakeTestHelper" . }}({{-  if (eq .KindType "interface")}}{{javaTestValue "" .}}{{end}});
		{{- end }}
{{- end }}
