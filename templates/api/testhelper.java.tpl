package {{camel .Module.Name}}.{{camel .Module.Name}}_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

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

public class {{Camel .Module.Name}}TestHelper
{
{{- range .Module.Structs}}

    static public {{Camel .Name}} makeTest{{Camel .Name}}() 
    {
        {{Camel .Name}} testStruct = new {{Camel .Name}}();
{{- range .Fields }}
{{- if .IsArray}}
        testStruct.{{camel .Name}} = new {{javaElementType "" . }}[1];
    {{- if or (.IsPrimitive) (eq .KindType "enum") }}
	    testStruct.{{camel .Name}}[0] = {{ javaTestValue "" . }};
    {{- else }}
	    testStruct.{{camel .Name}}[0] = {{template "getMakeTestHelper" . }}();
    {{- end }}
{{- else  if or (.IsPrimitive) (eq .KindType "enum") }}
        testStruct.{{camel .Name}} = {{javaTestValue "" . }};
{{- else }}
        testStruct.{{camel .Name}} = {{template "getMakeTestHelper" . }}();
{{- end}}
{{- end }}
        return testStruct;
    }
{{- end}}

{{- range .Module.Interfaces}}

    static public I{{Camel .Name}} makeTest{{Camel .Name}}(I{{Camel .Name}} testObjToFill) 
    {
        if (testObjToFill == null){return testObjToFill;}
{{- range .Properties }}
{{- if .IsArray}}
        {{javaReturn "" .}} local{{camel .Name}} = new {{javaElementType "" . }}[1];
    {{- if or (.IsPrimitive) (eq .KindType "enum") }}
	    local{{camel .Name}}[0] = {{ javaTestValue "" . }};
    {{- else if and (ne .KindType "extern") (ne .KindType "interface") }}
	    local{{camel .Name}}[0] = {{template "getMakeTestHelper" . }}();
    {{- end }}
        testObjToFill.set{{Camel .Name}}(local{{camel .Name}});
{{- else  if or (.IsPrimitive) (eq .KindType "enum") }}
        testObjToFill.set{{Camel .Name}}({{javaTestValue "" . }});
{{- else if and (ne .KindType "extern") (ne .KindType "interface") }}
        {{javaReturn "" .}} local{{camel .Name}} = {{template "getMakeTestHelper" . }}();
        testObjToFill.set{{Camel .Name}}(local{{camel .Name}});
{{- end}}
{{- end }}
        return testObjToFill;
    }
{{- end}}
}