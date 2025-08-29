package {{camel .Module.Name}}.{{camel .Module.Name}}_api;
import com.fasterxml.jackson.annotation.JsonProperty;


public enum {{Camel .Enum.Name}}
{
    {{ range  $idx, $m :=.Enum.Members }}
    {{- if $idx}},
    {{ end -}}
    @JsonProperty("{{.Value}}")
    {{Camel .Name}}({{.Value}})
{{- end -}};

    private final int value;

    {{Camel .Enum.Name}}(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static {{Camel .Enum.Name}} fromValue(int value) {
        for ({{Camel .Enum.Name}} e : values()) {
            if (e.value == value) return e;
        }
        throw new IllegalArgumentException("Unknown int value: " + value);
      }
}
