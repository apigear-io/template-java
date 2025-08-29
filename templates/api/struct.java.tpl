package {{camel .Module.Name}}.{{camel .Module.Name}}_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof {{Camel .Struct.Name}})) return false;
        {{Camel .Struct.Name}} other = ({{Camel .Struct.Name}}) o;

        return {{- if not (len (.Struct.Fields)) }} true{{else -}}
{{- range $idx, $s :=.Struct.Fields }}
{{- if .IsArray}}
        {{ if $idx}}&&{{ end }} Arrays.equals(this.{{camel .Name}}, other.{{camel .Name}})
{{- else if or .IsPrimitive ((eq .KindType "enum"))}}
        {{ if $idx}}&&{{ end }} this.{{camel .Name}} == other.{{camel .Name}}
{{- else }}
        {{ if $idx}}&&{{ end }} Objects.equals(this.{{camel .Name}}, other.{{camel .Name}})
{{- end }}
{{- end }}
{{- end }};
    }

    @Override
    public int hashCode() {
        int result = 7;
{{- range .Struct.Fields }}
{{- if .IsArray}}
        result = 31 * result + Arrays.hashCode({{camel .Name}});
{{- else if or (eq .KindType "int") (eq .KindType "int32") }}
        result = 31 * result + Integer.hashCode({{camel .Name}});
{{- else if (eq .KindType "string")}}
        result = 31 * result + ({{camel .Name}} != null ? {{camel .Name}}.hashCode() : 0);

{{- else if .IsPrimitive}}
        result = 31 * result + {{Camel (javaType "" .)}}.hashCode({{camel .Name}});
{{- else }}
        result = 31 * result + Objects.hashCode({{camel .Name}});
{{- end }}
{{- end }}
        return result;
    }


}
