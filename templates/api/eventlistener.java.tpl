package {{camel .Module.Name}}.{{camel .Module.Name}}_api;

{{- range .Module.Structs }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Name}};
{{- end }}
{{- range .Module.Enums }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Name}};
{{- end }}

  public interface I{{Camel .Interface.Name }}EventListener {
  {{- range .Interface.Properties }}
    void on{{Camel .Name}}Changed({{javaType "" .}} newValue);
  {{- end }}
  {{- range .Interface.Signals }}
    void on{{Camel .Name}}({{javaParams "" .Params}});
  {{- end }}
  void on_readyStatusChanged(boolean isReady);
  }
