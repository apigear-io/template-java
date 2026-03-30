package {{camel .Module.Name}}.{{camel .Module.Name}}_api;

{{- range .Module.Structs }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Name}};
{{- end }}
{{- range .Module.Enums }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Name}};
{{- end }}

import java.util.List;

  public interface I{{Camel .Interface.Name }}EventListener {
  {{- range .Interface.Properties }}
    void on{{Camel .Name}}Changed({{javaListType "" .}} newValue);
  {{- end }}
  {{- range .Interface.Signals }}
    void on{{Camel .Name}}({{javaListParams "" .Params}});
  {{- end }}
  void on_readyStatusChanged(boolean isReady);
  }
