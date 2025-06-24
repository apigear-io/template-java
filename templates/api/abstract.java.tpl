package {{dot .Module.Name}}.api;

import {{dot .Module.Name}}.api.I{{Camel .Interface.Name }}EventListener;
import {{dot .Module.Name}}.api.I{{Camel .Interface.Name }};
//TODO imported/extern modules
{{- range .Module.Structs }}
import {{dot .Module.Name}}.api.types.{{Camel .Name}}
{{- end }}
{{- range .Module.Enums }}
import {{dot .Module.Name}}.api.types.{{Camel .Name}}
{{- end }}


  public static class Abstract{{Camel .Interface.Name}} implements I{{Camel .Interface.Name }} {
    public Collection<I{{Camel .Interface.Name }}EventListener> events = new HashSet<>();

    public void addEventListener(I{{Camel .Interface.Name }}EventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(I{{Camel .Interface.Name }}EventListener listener) {
      listeners.remove(listener);
    }
  {{- range .Interface.Properties }}
    @Override
    public void fire{{Camel .Name}}Changed({{javaType "" .}} newValue) {
      for (I{{Camel .Name }}EventListener listener : events) {
        listener.on{{Camel .Name}}Changed(newValue);
      }
    }
  {{ end }}
  {{- range .Interface.Signals }}
    @Override
    public void fire{{Camel .Name}}({{javaParams "" .Params}}) {
      for (I{{Camel .Name }}EventListener listener : events) {
        listener.on{{Camel .Name}}({{ javaVars .Params}});
      }
    }
  {{ end }}
    
  }
