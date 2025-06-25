package {{dot .Module.Name}}.api;

import {{dot .Module.Name}}.api.I{{Camel .Interface.Name }}EventListener;
{{- range .Module.Structs }}
import {{dot .Module.Name}}.api.{{Camel .Name}}
{{- end }}
{{- range .Module.Enums }}
import {{dot .Module.Name}}.api.{{Camel .Name}}
{{- end }}


  public interface I{{Camel .Interface.Name }} {
    // properties
  {{- range .Interface.Properties }}
    void set{{Camel .Name}}({{javaParam "" .}});
    {{javaReturn "" . }} get{{Camel .Name}}();
    void fire{{Camel .Name}}Changed({{javaType "" .}} newValue);
  {{ end }}
    // methods
  {{- range .Interface.Operations }}
    {{javaReturn "" .Return}} {{camel .Name}}({{javaParams "" .Params}});
    {{javaAsyncReturn "" .Return}} {{camel .Name}}Async({{javaParams "" .Params}});
  {{- end }}
   {{- range .Interface.Signals }}
   public void fire{{Camel .Name}}({{javaParams "" .Params}});
   {{- end }}
    bool _isReady();
    // signal listeners
    int addEventListener(I{{Camel .Interface.Name }}EventListener listener);
    void removeEventListener(I{{Camel .Interface.Name }}EventListener listener);
  }
