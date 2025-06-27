package {{dot .Module.Name}}.api;

{{- range .Module.Structs }}
import {{dot .Module.Name}}.api.{{Camel .Name}};
{{- end }}
{{- range .Module.Enums }}
import {{dot .Module.Name}}.api.{{Camel .Name}};
{{- end }}

  public interface I{{Camel .Interface.Name }}EventListener {
  {{- range .Interface.Properties }}
    void on{{Camel .Name}}Changed({{javaType "" .}} newValue);
  {{- end }}
  {{- range .Interface.Signals }}
    void on{{Camel .Name}}({{javaParams "" .Params}});
  {{- end }}
  }
