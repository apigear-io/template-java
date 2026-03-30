package {{camel .Module.Name}}.{{camel .Module.Name}}_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class {{Camel .Module.Name}}TestHelper
{
{{- range .Module.Structs}}

    static public {{Camel .Name}} makeTest{{Camel .Name}}()
    {
        {{Camel .Name}} testStruct = new {{Camel .Name}}();
{{- range .Fields }}
{{- if .IsArray}}
        testStruct.{{camel .Name}} = new ArrayList<>();
    {{- if or (.IsPrimitive) (eq .KindType "enum") }}
	    testStruct.{{camel .Name}}.add({{ javaTestValue "" . }});
    {{- else }}
	    testStruct.{{camel .Name}}.add({{template "getMakeTestHelper" . }}());
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
        {{javaListReturn "" .}} local{{camel .Name}} = new ArrayList<>();
    {{- if or (.IsPrimitive) (eq .KindType "enum") }}
	    local{{camel .Name}}.add({{ javaTestValue "" . }});
    {{- else if and (ne .KindType "extern") (ne .KindType "interface") }}
	    local{{camel .Name}}.add({{template "getMakeTestHelper" . }}());
    {{- end }}
        testObjToFill.set{{Camel .Name}}(local{{camel .Name}});
{{- else  if or (.IsPrimitive) (eq .KindType "enum") }}
        testObjToFill.set{{Camel .Name}}({{javaTestValue "" . }});
{{- else if and (ne .KindType "extern") (ne .KindType "interface") }}
        {{javaListReturn "" .}} local{{camel .Name}} = {{template "getMakeTestHelper" . }}();
        testObjToFill.set{{Camel .Name}}(local{{camel .Name}});
{{- end}}
{{- end }}
        return testObjToFill;
    }
{{- end}}
}
