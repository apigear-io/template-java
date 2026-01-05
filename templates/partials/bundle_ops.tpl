{{- define "getDataFromBundle"}}
		        {{- if .IsPrimitive }}
			        {{javaReturn "" .}} {{javaVar .}} = data.get{{ ( Camel  (javaElementType "" .) ) }}{{if .IsArray}}Array{{end}}("{{.Name}}"{{if not .IsArray}}, {{javaDefault "" .}}{{end}});
		        {{- else if .IsArray }}
                    {{javaReturn "" .}} {{javaVar .}} =  {{template "getParcelable" . }}.unwrapArray(({{template "getParcelable" . }}[])data.getParcelableArray("{{.Name}}", {{template "getParcelable" . }}.class));
                {{- else }}
			        {{javaReturn "" .}} {{javaVar .}} = data.getParcelable("{{.Name}}", {{template "getParcelable" . }}.class).get{{Camel .Type}}();
		        {{- end }}
{{- end }}

{{- define "putDataIntoBundle"}}
		        {{- if and .IsPrimitive }}
		        data.put{{ ( Camel  (javaElementType "" .) ) }}{{if .IsArray}}Array{{end}}("{{.Name}}", {{ javaVar .}});
		        {{- else if .IsArray }}
		        data.putParcelableArray("{{.Name}}", {{template "getParcelable" . }}.wrapArray({{javaVar .}}));
                {{- else }}
		        data.putParcelable("{{.Name}}", new {{template "getParcelable" . }}({{javaVar .}}));
		        {{- end }}
{{- end }}

{{- define "getResultFromBundle"}}
        {{- if and .Return.IsPrimitive }}
		    {{javaReturn "" .Return }} result = bundle.get{{ ( Camel  (javaElementType "" .Return ) ) }}{{if .Return.IsArray}}Array{{end}}("result"{{if not .Return.IsArray}}, {{javaDefault "" .Return}}{{end}});
        {{- else if .Return.IsArray }}
            {{javaReturn "" .Return}} result =  {{template "getParcelable" .Return }}.unwrapArray(({{template "getParcelable" .Return }}[])bundle.getParcelableArray("result", {{template "getParcelable" .Return }}.class));
	    {{- else }}
		    {{javaReturn "" .Return }} result = bundle.getParcelable("result", {{template "getParcelable" .Return }}.class).get{{Camel .Return.Type}}();
	    {{- end }}
{{- end }}

{{- define "putResultIntoBundle"}}
		        {{- if and .Return.IsPrimitive }}
		        resp_data.put{{ ( Camel  (javaElementType "" .Return) ) }}{{if .Return.IsArray}}Array{{end}}("result", result);
		        {{- else if .Return.IsArray }}
		        resp_data.putParcelableArray("result",{{template "getParcelable" .Return }}.wrapArray(result));
                {{- else }}
		        resp_data.putParcelable("result", new {{template "getParcelable" .Return }}(result));
		        {{- end }}
{{- end }}
