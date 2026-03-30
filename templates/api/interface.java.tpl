package {{camel .Module.Name}}.{{camel .Module.Name}}_api;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
{{- range .Module.Structs }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Name}};
{{- end }}
{{- range .Module.Enums }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Name}};
{{- end }}

import java.util.List;
import java.util.concurrent.CompletableFuture;


  public interface I{{Camel .Interface.Name }} {
    // properties
  {{- range .Interface.Properties }}
    void set{{Camel .Name}}({{javaListParam "" .}});
    {{javaListReturn "" . }} get{{Camel .Name}}();
    void fire{{Camel .Name}}Changed({{javaListType "" .}} newValue);
  {{ end }}
    // methods
  {{- range .Interface.Operations }}
    {{javaListReturn "" .Return}} {{camel .Name}}({{javaListParams "" .Params}});
    {{javaListAsyncReturn "" .Return}} {{camel .Name}}Async({{javaListParams "" .Params}});
  {{- end }}
  {{- range .Interface.Signals }}
    public void fire{{Camel .Name}}({{javaListParams "" .Params}});
  {{- end }}
    boolean _isReady();
    void _shutdown();
    // signal listeners
    public void fire_readyStatusChanged(boolean isReady);
    void addEventListener(I{{Camel .Interface.Name }}EventListener listener);
    void removeEventListener(I{{Camel .Interface.Name }}EventListener listener);
  }
