package {{camel .Module.Name}}.{{camel .Module.Name}}_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;


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
	    testStruct.{{camel .Name}}[0] = makeTest{{Camel (javaElementType "" .) }}();
    {{- end }}
{{- else  if or (.IsPrimitive) (eq .KindType "enum") }}
        testStruct.{{camel .Name}} = {{javaTestValue "" . }};
{{- else }}
        testStruct.{{camel .Name}} = makeTest{{javaType "" . }}();
{{- end}}
{{- end }}
        return testStruct;
    }

{{- end}}

}