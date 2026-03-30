{{- define "getDataFromBundle"}}
		        {{- if and .IsPrimitive (not .IsArray) }}
			        {{javaListReturn "" .}} {{javaVar .}} = data.get{{ ( Camel  (javaElementType "" .) ) }}("{{.Name}}", {{javaDefault "" .}});
		        {{- else if and .IsPrimitive .IsArray }}
			        {{javaListReturn "" .}} {{javaVar .}} = Conversions.toList(data.get{{ ( Camel  (javaElementType "" .) ) }}Array("{{.Name}}"));
		        {{- else if .IsArray }}
                    {{javaListReturn "" .}} {{javaVar .}} = Conversions.toList({{template "getParcelable" . }}.unwrapArray(({{template "getParcelable" . }}[])data.getParcelableArray("{{.Name}}", {{template "getParcelable" . }}.class)));
                {{- else }}
			        {{javaListReturn "" .}} {{javaVar .}} = data.getParcelable("{{.Name}}", {{template "getParcelable" . }}.class).get{{Camel .Type}}();
		        {{- end }}
{{- end }}

{{- define "putDataIntoBundle"}}
		        {{- if and .IsPrimitive (not .IsArray) }}
		        data.put{{ ( Camel  (javaElementType "" .) ) }}("{{.Name}}", {{ javaVar .}});
		        {{- else if and .IsPrimitive .IsArray }}
		        data.put{{ ( Camel  (javaElementType "" .) ) }}Array("{{.Name}}", Conversions.toArray({{ javaVar .}}, new {{javaElementType "" .}}[0]));
		        {{- else if .IsArray }}
		        data.putParcelableArray("{{.Name}}", {{template "getParcelable" . }}.wrapArray(Conversions.toArray({{javaVar .}}, new {{javaElementType "" .}}[0])));
                {{- else }}
		        data.putParcelable("{{.Name}}", new {{template "getParcelable" . }}({{javaVar .}}));
		        {{- end }}
{{- end }}

{{- define "getResultFromBundle"}}
        {{- if and .Return.IsPrimitive (not .Return.IsArray) }}
		    {{javaListReturn "" .Return }} result = bundle.get{{ ( Camel  (javaElementType "" .Return ) ) }}("result", {{javaDefault "" .Return}});
        {{- else if and .Return.IsPrimitive .Return.IsArray }}
		    {{javaListReturn "" .Return}} result = Conversions.toList(bundle.get{{ ( Camel  (javaElementType "" .Return ) ) }}Array("result"));
        {{- else if .Return.IsArray }}
            {{javaListReturn "" .Return}} result = Conversions.toList({{template "getParcelable" .Return }}.unwrapArray(({{template "getParcelable" .Return }}[])bundle.getParcelableArray("result", {{template "getParcelable" .Return }}.class)));
	    {{- else }}
		    {{javaListReturn "" .Return }} result = bundle.getParcelable("result", {{template "getParcelable" .Return }}.class).get{{Camel .Return.Type}}();
	    {{- end }}
{{- end }}

{{- define "putResultIntoBundle"}}
		        {{- if and .Return.IsPrimitive (not .Return.IsArray) }}
		        resp_data.put{{ ( Camel  (javaElementType "" .Return) ) }}("result", result);
		        {{- else if and .Return.IsPrimitive .Return.IsArray }}
		        resp_data.put{{ ( Camel  (javaElementType "" .Return) ) }}Array("result", Conversions.toArray(result, new {{javaElementType "" .Return}}[0]));
		        {{- else if .Return.IsArray }}
		        resp_data.putParcelableArray("result",{{template "getParcelable" .Return }}.wrapArray(Conversions.toArray(result, new {{javaElementType "" .Return}}[0])));
                {{- else }}
		        resp_data.putParcelable("result", new {{template "getParcelable" .Return }}(result));
		        {{- end }}
{{- end }}
