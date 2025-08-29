package {{camel .Module.Name}}.{{camel .Module.Name}}_api;

import com.fasterxml.jackson.annotation.JsonProperty;

public  class {{Camel .Struct.Name}} {

    public {{Camel .Struct.Name}}({{javaParams "" .Struct.Fields}})
    {
      {{- range .Struct.Fields }}
      this.{{camel .Name}} = {{camel .Name}};
      {{- end }}
    }  

    public {{Camel .Struct.Name}}() 
    {
{{- range .Struct.Fields }}
{{- if .IsArray}}
        this.{{camel .Name}} = new {{javaElementType "" . }}[0];
{{- else  if not .IsPrimitive }}
{{-  if (eq .KindType "enum") }}
        this.{{camel .Name}} = {{javaType "" . }}.values()[0];
{{- else }}
        this.{{camel .Name}} = new {{javaType "" . }}();
{{- end}}
{{- end }}
{{- end }}
    }

  {{- range .Struct.Fields }}
    @JsonProperty("{{snake .Name}}")
    public {{javaType "" .}} {{camel .Name}};
  {{- end }}

    public {{Camel .Struct.Name}}({{Camel .Struct.Name}} other)
    {
{{- range .Struct.Fields }}
{{- if .IsArray}}
{{- if or .IsPrimitive ((eq .KindType "enum"))}}
        this.{{camel .Name}} = java.util.Arrays.copyOf(other.{{camel .Name}}, other.{{camel .Name}}.length);
{{- else }}
        this.{{camel .Name}} = new {{javaElementType "" . }}[other.{{camel .Name}}.length];
        for (int i = 0; i < other.{{camel .Name}}.length; i++)
        {
            this.{{camel .Name}}[i] = new {{javaElementType "" . }}(other.{{camel .Name}}[i]);
        }
{{- end }}
{{- else }}
{{- if or .IsPrimitive ((eq .KindType "enum"))}}
        this.{{camel .Name}} = other.{{camel .Name}};
{{- else }}
        this.{{camel .Name}} = new {{javaType "" . }}(other.{{camel .Name}});
{{- end }}
{{- end }}
{{- end }}
    }

  }
