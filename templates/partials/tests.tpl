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
		{{- if and .IsPrimitive (not .IsArray) }}
			{{javaListReturn "" .}} received{{javaVar .}} = data.get{{ ( Camel  (javaElementType "" .) ) }}("{{.Name}}", {{javaDefault "" .}});
		{{- else if and .IsPrimitive .IsArray }}
			{{javaListReturn "" .}} received{{javaVar .}} = Conversions.toList(data.get{{ ( Camel  (javaElementType "" .) ) }}Array("{{.Name}}"));
		{{- else if .IsArray }}
            {{javaListReturn "" .}} received{{javaVar .}} = Conversions.toList({{template "getParcelable" . }}.unwrapArray(({{template "getParcelable" . }}[])data.getParcelableArray("{{.Name}}", {{template "getParcelable" . }}.class)));
        {{- else }}
			{{javaListReturn "" .}} received{{javaVar .}} = data.getParcelable("{{.Name}}", {{template "getParcelable" . }}.class).get{{Camel (.Type)}}();
		{{- end }}
{{- end }}

{{- define "putTestDataIntoBundle"}}
		{{- if and .IsPrimitive (not .IsArray) }}
		data.put{{ ( Camel  (javaElementType "" .) ) }}("{{.Name}}", test{{ javaVar .}});
		{{- else if and .IsPrimitive .IsArray }}
		data.put{{ ( Camel  (javaElementType "" .) ) }}Array("{{.Name}}", Conversions.toArray(test{{ javaVar .}}, new {{javaElementType "" .}}[0]));
		{{- else if .IsArray }}
		data.putParcelableArray("{{.Name}}", {{template "getParcelable" . }}.wrapArray(Conversions.toArray(test{{javaVar .}}, new {{javaElementType "" .}}[0])));
        {{- else }}
		data.putParcelable("{{.Name}}", new {{template "getParcelable" . }}(test{{javaVar .}}));
		{{- end }}
{{- end }}

{{- define "prepareTestValue"}}
        {{- if .IsArray }}
            {{- if or  (.IsPrimitive) (eq .KindType "enum")}}
        {{javaListType "" .}} test{{ javaVar .}} = new java.util.ArrayList<>();
        test{{ javaVar .}}.add({{javaTestValue "" . }});
            {{- else }}
        {{javaListType "" .}} test{{ javaVar .}} = new java.util.ArrayList<>();
                {{- if (eq .KindType "extern") }}
		test{{ javaVar .}}.add({{javaTestValue "" .}});
                {{- else }}
        test{{ javaVar .}}.add({{template "getMakeTestHelper" . }}({{-  if (eq .KindType "interface")}}{{javaTestValue "" .}}{{end}}));
                {{- end }}
            {{- end}}
		{{- else if or  (.IsPrimitive) (eq .KindType "enum") }}
		{{javaListReturn "" . }} test{{ javaVar .}} = {{javaTestValue "" . }};
		{{- else if (eq .KindType "extern") }}
		{{javaListReturn "" . }} test{{ javaVar .}} = {{javaTestValue "" .}};
        {{- else }}
        {{javaListReturn "" . }} test{{ javaVar .}} = {{template "getMakeTestHelper" . }}({{-  if (eq .KindType "interface")}}{{javaTestValue "" .}}{{end}});
		{{- end }}
{{- end }}
