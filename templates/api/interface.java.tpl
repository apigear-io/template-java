package {{camel .Module.Name}}.{{camel .Module.Name}}_api;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
{{- range .Module.Structs }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Name}};
{{- end }}
{{- range .Module.Enums }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Name}};
{{- end }}

import java.util.concurrent.CompletableFuture;


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
    boolean _isReady();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(I{{Camel .Interface.Name }}EventListener listener);
    void removeEventListener(I{{Camel .Interface.Name }}EventListener listener);
  }
